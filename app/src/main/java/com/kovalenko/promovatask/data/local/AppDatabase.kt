package com.kovalenko.promovatask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kovalenko.promovatask.data.local.dao.GenreDao
import com.kovalenko.promovatask.data.local.dao.MovieDao
import com.kovalenko.promovatask.data.local.dao.MovieGenreCrossRefDao
import com.kovalenko.promovatask.data.local.entity.GenreEntity
import com.kovalenko.promovatask.data.local.entity.GenresTypeConverter
import com.kovalenko.promovatask.data.local.entity.MovieEntity
import com.kovalenko.promovatask.data.local.entity.MovieGenreCrossRef

@Database(
    entities = [MovieEntity::class, GenreEntity::class, MovieGenreCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GenresTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieGenreDao(): MovieGenreCrossRefDao
}