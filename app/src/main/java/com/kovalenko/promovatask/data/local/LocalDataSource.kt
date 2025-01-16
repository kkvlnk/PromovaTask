package com.kovalenko.promovatask.data.local

import androidx.paging.PagingSource
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveMovies(movies: List<MovieEntity>)
    suspend fun updateLikedStatus(movieId: Int, liked: Boolean)
    fun getLikedMovies(): Flow<List<MovieEntity>>
    fun getMoviesPaging(): PagingSource<Int, MovieEntity>
}