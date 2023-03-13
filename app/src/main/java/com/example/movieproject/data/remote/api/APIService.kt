package com.example.movieproject.data.remote.api

import com.example.movieproject.data.remote.remotedatasource.MoviesResponse
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.SearchMovieResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private val BASE_URL = "https://imdb-api.com/en/API/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface APIService {
    @GET("MostPopularMovies/k_w5xpu5vt")
    suspend fun getMovies(): MoviesResponse

    @GET("Title/k_w5xpu5vt/{id}")
    suspend fun getFullCast(@Path("id")id: String): NetworkMovieById

    @GET("SearchTitle/k_w5xpu5vt/{title}")
    suspend fun searchMovies(@Path("title")title: String): SearchMovieResponse
}

object Api{
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}