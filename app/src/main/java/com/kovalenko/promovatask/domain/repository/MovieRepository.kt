package com.kovalenko.promovatask.domain.repository

import androidx.paging.PagingData
import com.kovalenko.promovatask.domain.model.Genre
import com.kovalenko.promovatask.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun updateLikedStatus(movieId: Int, liked: Boolean): Result<Unit>
    fun getLikedMovies(): Flow<List<Movie>>
    fun getMoviesPaging(): Flow<PagingData<Movie>>
    suspend fun getGenres(refresh: Boolean): Result<List<Genre>>
    suspend fun refreshGenres(): Result<Unit>
}