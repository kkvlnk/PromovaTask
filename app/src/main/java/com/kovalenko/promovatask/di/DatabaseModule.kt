package com.kovalenko.promovatask.di

import android.app.Application
import androidx.room.Room
import com.kovalenko.promovatask.data.local.AppDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            name = "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Single
    fun provideMovieDao(database: AppDatabase) = database.movieDao()

    @Single
    fun provideGenreDao(database: AppDatabase) = database.genreDao()
}