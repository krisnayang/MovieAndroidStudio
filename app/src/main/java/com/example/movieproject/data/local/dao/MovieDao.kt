package com.example.movieproject.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    //movie
    @Query("SELECT * FROM movieentity")
    fun getMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovie(movie: List<MovieEntity>)

    //fullCast
    @Query("SELECT * FROM fullcastentity WHERE movieId = :id")
    fun getFullCast(id: String): Flow<List<FullCastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFullCast(fullCast: List<FullCastEntity>)

    //Movie Detail
    @Query("SELECT * FROM moviedetailentity WHERE id = :id")
    fun getMovieDetail(id: String): Flow<MovieDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetail(fullCast: List<MovieDetailEntity>)
}