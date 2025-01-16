package com.kovalenko.promovatask.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(val genres: List<GenreDto>)