package com.kovalenko.promovatask.data.local

import androidx.paging.PagingSource
import com.kovalenko.promovatask.data.local.dao.GenreDao
import com.kovalenko.promovatask.data.local.dao.MovieDao
import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class LocalDataSourceImpl(
    private val movieDao: MovieDao,
    private val genreDao: GenreDao
): LocalDataSource {
    override suspend fun saveMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    override suspend fun updateLikedStatus(movieId: Int, liked: Boolean) {
        movieDao.updateLikedStatus(movieId, liked)
    }

    override fun getLikedMovies(): Flow<List<MovieWithGenres>> {
        return movieDao.getLikedMovies()
    }

    override fun getMoviesPaging(): PagingSource<Int, MovieWithGenres> {
        return movieDao.getMoviesPaging()
    }

    override suspend fun saveGenres(genres: List<GenreEntity>) {
        genreDao.insertGenres(genres)
    }
}