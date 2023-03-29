package com.example.movieproject.data.remote.api

import com.example.movieproject.BuildConfig
import com.example.movieproject.data.remote.remotedatasource.MoviesResponse
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.SearchMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    //k_psr6zcqm, k_w5xpu5vt, k_x7yw68hc
    @GET("MostPopularMovies/" + BuildConfig.API_TOKEN)
    suspend fun getMovies(): MoviesResponse

    @GET("Title/" + BuildConfig.API_TOKEN + "/{id}")
    suspend fun getFullCast(@Path("id")id: String): NetworkMovieById

    @GET("SearchTitle/" + BuildConfig.API_TOKEN + "/{title}")
    suspend fun searchMovies(@Path("title")title: String): SearchMovieResponse
}