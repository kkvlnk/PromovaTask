package com.kovalenko.promovatask.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kovalenko.promovatask.data.local.LocalDataSource
import com.kovalenko.promovatask.data.local.entity.MovieGenreCrossRef
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import com.kovalenko.promovatask.data.mappers.toEntity
import com.kovalenko.promovatask.data.remote.RemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : RemoteMediator<Int, MovieWithGenres>() {
    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieWithGenres>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                currentPage = 1
                currentPage
            }

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> currentPage + 1
        }
        try {
            val response = remoteDataSource.getMovies(loadKey)
            val likedIds = localDataSource.getLikedIds()
            val movies = response.results.map { it.toEntity(isLiked = likedIds.contains(it.id)) }

            val crossRefs = response.results.flatMap { movieDto ->
                movieDto.genreIds.map { genreId ->
                    MovieGenreCrossRef(movieId = movieDto.id, genreId = genreId)
                }
            }

            localDataSource.saveMovieGenreCrossRefs(crossRefs)
            localDataSource.saveMovies(movies)
            currentPage = loadKey
            return MediatorResult.Success(endOfPaginationReached = response.page == response.totalPages)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}