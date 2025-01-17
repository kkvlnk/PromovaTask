package com.kovalenko.promovatask.domain.model

data class Movie(
    val adult: Boolean,
    val backdropPath: String?,
    val genres: List<Genre>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isLiked: Boolean
)