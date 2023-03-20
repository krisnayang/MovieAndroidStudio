package com.example.movieproject.di

import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.repository.FullCastRepository
import com.example.movieproject.data.repository.FullCastRepositoryImpl
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieDatabase: MovieDatabase,
        api: APIService): MovieRepository {
        return MovieRepositoryImpl(movieDatabase, api)
    }

    @Provides
    @Singleton
    fun provideFullCastRepository(
        movieDatabase: MovieDatabase,
        api: APIService): FullCastRepository {
        return FullCastRepositoryImpl(movieDatabase, api)
    }
}