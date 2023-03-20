package com.example.movieproject.di

import android.content.Context
import androidx.room.Room
import com.example.movieproject.data.local.dao.MovieDao
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDatabaseModule {
    @Provides
    @Singleton
    fun movieDao(movieDatabase: MovieDatabase): MovieDao{
        return movieDatabase.movieDao
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase{
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}