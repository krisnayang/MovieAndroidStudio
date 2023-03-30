package com.example.movieproject.data.repository

import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.remote.network.ConnectivityObserver
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovies(network: ConnectivityObserver.Status): Flow<List<MovieEntity>>
    suspend fun getMovie(network: ConnectivityObserver.Status, id: String): Flow<MovieDetailEntity?>

    suspend fun getFavouriteMovie(id: String): Flow<MoviesFavourite?>

    suspend fun searchMovies(network: ConnectivityObserver.Status, title: String): Flow<List<MovieLocal>?>?

    suspend fun insertFavourite(favourite: MoviesFavourite)

    suspend fun removeFavouriteMovie(movie: MoviesFavourite)

    suspend fun getFavouriteMovies(): Flow<List<MoviesFavourite>>
}