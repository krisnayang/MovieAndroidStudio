package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.ui.state.UiState

interface MovieRepository {
    suspend fun getMovies(context: Context): UiState<List<Movie>>
}