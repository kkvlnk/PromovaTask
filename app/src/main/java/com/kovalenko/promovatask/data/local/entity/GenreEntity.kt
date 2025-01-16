package com.kovalenko.promovatask.data.local.entity

import androidx.room.Entity

@Entity(tableName = "genres")
data class GenreEntity(
    val id: Int,
    val name: String
)