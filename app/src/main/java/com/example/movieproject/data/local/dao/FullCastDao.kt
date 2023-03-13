package com.example.movieproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FullCastDao {
    @Query("SELECT * FROM fullcastentity WHERE id = :id")
    fun getFullCast(id: String): Flow<FullCastEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( fullCast: List<FullCastEntity>)
}