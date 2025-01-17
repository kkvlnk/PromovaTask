package com.kovalenko.promovatask.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kovalenko.promovatask.data.local.LocalDataSource
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import com.kovalenko.promovatask.data.mappers.toDomainModel
import com.kovalenko.promovatask.data.remote.RemoteDataSource
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
    override suspend fun updateLikedStatus(movieId: Int, liked: Boolean) {
        localDataSource.updateLikedStatus(movieId, liked)
    }

    override fun getLikedMovies(): Flow<List<Movie>> {
        return localDataSource.getLikedMovies().map { entities ->
            entities.map(MovieWithGenres::toDomainModel)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMoviesPaging(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 65, enablePlaceholders = false),
            remoteMediator = null, // TODO: Implement RemoteMediator
            pagingSourceFactory = { localDataSource.getMoviesPaging() }
        )
            .flow
            .map { pagingData -> pagingData.map(MovieWithGenres::toDomainModel) }
    }
}