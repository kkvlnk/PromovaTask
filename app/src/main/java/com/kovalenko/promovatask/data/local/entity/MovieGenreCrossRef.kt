package com.kovalenko.promovatask.data.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRef(
    val movieId: Int,
    val genreId: Int
)