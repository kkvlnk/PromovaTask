package com.kovalenko.promovatask.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Upsert
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Transaction
    @Query("SELECT * FROM movies ORDER BY releaseDate DESC")
    fun getMoviesPaging(): PagingSource<Int, MovieWithGenres>

    @Query("UPDATE movies SET isLiked = :isLiked WHERE movieId = :movieId")
    suspend fun updateLikedStatus(movieId: Int, isLiked: Boolean)

    @Transaction
    @Query("SELECT * FROM movies WHERE isLiked = 1")
    fun getLikedMovies(): Flow<List<MovieWithGenres>>

    @Query("SELECT movieId from movies WHERE isLiked = 1")
    suspend fun getLikedIds(): List<Int>

    @Query("DELETE FROM movies")
    suspend fun clearAll()
}