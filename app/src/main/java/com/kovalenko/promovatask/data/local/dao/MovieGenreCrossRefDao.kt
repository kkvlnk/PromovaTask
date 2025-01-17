package com.kovalenko.promovatask.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kovalenko.promovatask.data.local.entity.MovieGenreCrossRef

@Dao
interface MovieGenreCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenreCrossRefs(crossRefs: List<MovieGenreCrossRef>)

    @Query("DELETE FROM moviegenrecrossref")
    suspend fun clearAllCrossRefs()
}