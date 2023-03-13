package com.example.movieproject.data.local.localdatasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieproject.data.local.dao.FullCastDao

@Database(entities = [FullCastEntity::class], version = 2, exportSchema = false)
abstract class FullCastDatabase: RoomDatabase() {
    abstract val fullCastDao: FullCastDao

    companion object{
        @Volatile
        private var INSTANCE: FullCastDatabase? =null

        fun getDatabase(context: Context): FullCastDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FullCastDatabase::class.java,
                    "valorant_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}