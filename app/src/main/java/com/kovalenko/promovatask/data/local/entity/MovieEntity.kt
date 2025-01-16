package com.kovalenko.promovatask.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

@Entity(tableName = "movies")
data class MovieEntity(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    @PrimaryKey
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
    val isLiked: Boolean = false
)

class GenresTypeConverter {
    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>): String {
        return Json.encodeToString<List<Int>>(genreIds)
    }

    @TypeConverter
    fun toGenreIds(genreIdsString: String): List<Int> {
        return Json.decodeFromString<List<Int>>(genreIdsString)
    }
}