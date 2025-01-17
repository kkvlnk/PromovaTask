package com.kovalenko.promovatask.data.mappers

import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.MovieWithGenres
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