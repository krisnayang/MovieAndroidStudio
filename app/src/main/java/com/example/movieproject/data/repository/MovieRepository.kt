package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(): Flow<List<MovieEntity>>
    suspend fun getMovie(id: String): Flow<MovieDetailEntity?>

    suspend fun getFavouriteMovie(id: String): Flow<MoviesFavourite>

    suspend fun searchMovies(title: String): Flow<List<Movie>?>?

    suspend fun insertFavourite(favourite: MoviesFavourite)

    suspend fun removeFavouriteMovie(id: String)

    suspend fun getFavouriteMovies(): Flow<List<MoviesFavourite>>
}