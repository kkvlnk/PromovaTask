package com.kovalenko.promovatask.data.mappers

import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
import com.kovalenko.promovatask.data.remote.model.MovieDto
import com.kovalenko.promovatask.domain.model.Genre
import com.kovalenko.promovatask.domain.model.Movie

fun MovieWithGenres.toDomainModel() = Movie(
    adult = movie.adult,
    backdropPath = movie.backdropPath,
    genres = genres.map(GenreEntity::toDomainModel),
    id = movie.movieId,
    originalLanguage = movie.originalLanguage,
    originalTitle = movie.title,
    overview = movie.overview,
    popularity = movie.popularity,
    posterPath = movie.posterPath,
    releaseDate = movie.releaseDate,
    title = movie.title,
    video = movie.video,
    voteAverage = movie.voteAverage,
    voteCount = movie.voteCount

)

fun GenreEntity.toDomainModel() = Genre(id = genreId, name = name)

fun MovieDto.toEntity() = MovieEntity(
    adult = adult,
    backdropPath = backdropPath,
    movieId = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)