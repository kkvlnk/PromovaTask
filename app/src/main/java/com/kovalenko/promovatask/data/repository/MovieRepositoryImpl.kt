package com.kovalenko.promovatask.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kovalenko.promovatask.data.MoviesRemoteMediator
import com.kovalenko.promovatask.data.local.LocalDataSource
import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import com.kovalenko.promovatask.data.mappers.toDomainModel
import com.kovalenko.promovatask.data.mappers.toEntity
import com.kovalenko.promovatask.data.remote.RemoteDataSource
import com.kovalenko.promovatask.data.remote.model.GenreDto
import com.kovalenko.promovatask.domain.model.Genre
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class MovieRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : MovieRepository {
    override suspend fun updateLikedStatus(movieId: Int, liked: Boolean): Result<Unit> {
        return runCatching { localDataSource.updateLikedStatus(movieId, liked) }
    }

    override fun getLikedMovies(): Flow<List<Movie>> {
        return localDataSource.getLikedMovies().map { entities ->
            entities.map(MovieWithGenres::toDomainModel)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 65,
                initialLoadSize = 65,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            remoteMediator = MoviesRemoteMediator(
                remoteDataSource,
                localDataSource
            ),
            pagingSourceFactory = { localDataSource.getMoviesPaging() }
        )
            .flow
            .map { pagingData -> pagingData.map(MovieWithGenres::toDomainModel) }
    }

    override suspend fun refreshGenres(): Result<Unit> {
        return runCatching {
            remoteDataSource.getGenres()
                .map(GenreDto::toEntity)
                .let { localDataSource.saveGenres(it) }
        }
    }

    override suspend fun getGenres(refresh: Boolean): Result<List<Genre>> {
        return runCatching {
            if (refresh) {
                refreshGenres().getOrThrow()
            }
            localDataSource.getGenres().map(GenreEntity::toDomainModel)
        }
    }
}