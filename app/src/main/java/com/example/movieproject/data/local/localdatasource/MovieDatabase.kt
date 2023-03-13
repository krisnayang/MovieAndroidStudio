package com.example.movieproject.data.local.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieproject.data.local.dao.MovieDao

@Database(entities = [MovieEntity::class, FullCastEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao

    companion object{
        @Volatile
        private var INSTANCE: MovieDatabase? =null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "valorant_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}