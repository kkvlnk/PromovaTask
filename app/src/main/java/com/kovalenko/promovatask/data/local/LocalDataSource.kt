package com.kovalenko.promovatask.data.local

import androidx.paging.PagingSource
import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import com.kovalenko.promovatask.data.local.entity.MovieGenreCrossRef
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveMovies(movies: List<MovieEntity>)
    suspend fun updateLikedStatus(movieId: Int, liked: Boolean)
    fun getLikedMovies(): Flow<List<MovieWithGenres>>
    fun getMoviesPaging(): PagingSource<Int, MovieWithGenres>
    suspend fun saveGenres(genres: List<GenreEntity>)
    suspend fun getGenres(): List<GenreEntity>
    suspend fun saveMovieGenreCrossRefs(crossRefs: List<MovieGenreCrossRef>)
    suspend fun getLikedIds(): List<Int>
}