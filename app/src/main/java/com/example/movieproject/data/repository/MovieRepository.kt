package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.local.model.Movie

interface MovieRepository {
    suspend fun getMovies(context: Context): List<Movie>?
}