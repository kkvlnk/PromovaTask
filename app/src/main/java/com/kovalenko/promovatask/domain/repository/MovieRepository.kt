package com.kovalenko.promovatask.domain.repository

import androidx.paging.PagingData
import com.kovalenko.promovatask.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun updateLikedStatus(movieId: Int, liked: Boolean)
    fun getLikedMovies(): Flow<List<Movie>>
    fun getMoviesPaging(): Flow<PagingData<Movie>>
}