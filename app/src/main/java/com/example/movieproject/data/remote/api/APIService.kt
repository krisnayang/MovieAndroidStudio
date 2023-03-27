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


interface APIService {
    //k_psr6zcqm, k_w5xpu5vt, k_x7yw68hc
    @GET("MostPopularMovies/k_psr6zcqm")
    suspend fun getMovies(): MoviesResponse

    @GET("Title/k_psr6zcqm/{id}")
    suspend fun getFullCast(@Path("id")id: String): NetworkMovieById

    @GET("SearchTitle/k_psr6zcqm/{title}")
    suspend fun searchMovies(@Path("title")title: String): SearchMovieResponse
}