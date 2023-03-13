package com.example.movieproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieproject.data.local.localdatasource.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movieentity")
    fun getMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( movie: List<MovieEntity>)

    @Query("SELECT * FROM movieentity WHERE id = :id")
    fun getMovie(id: String): Flow<MovieEntity>
}