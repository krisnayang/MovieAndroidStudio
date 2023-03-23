package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(context: Context): UiState<List<Movie>>
    suspend fun getMovie(id: String, context: Context): Flow<UiState<MovieDetailEntity?>?>
    suspend fun searchMovies(title: String, context: Context): UiState<List<Movie>?>?
}