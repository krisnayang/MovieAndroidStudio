package com.example.movieproject.di

import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideMyApi(): APIService{
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            ))
            .baseUrl("https://imdb-api.com/en/API/")
            .build()
            .create(APIService::class.java)
    }

}