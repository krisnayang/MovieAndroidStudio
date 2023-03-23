package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.remotedatasource.*
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: MovieDatabase,
    private val api: APIService
): MovieRepository{
    private fun insertMovies(movieResponse: MoviesResponse){
        database.movieDao.insertAllMovie(movieResponse.asDatabaseFullCast())
    }

    override suspend fun getMovies(): UiState<List<Movie>>{
        return withContext(Dispatchers.IO) {
            if (checkInternet()) {
                getMoviesFromApi()
            } else {
                getMoviesFromDb()
            }
        }
    }
    override suspend fun getMovie(id: String): Flow<MovieDetailEntity?> {
        return withContext(Dispatchers.IO) {
            if (checkInternet()) {
                getMovieFromApi(id)
            } else {
                getMovieFromDb(id)
            }
        }
    }

    override suspend fun searchMovies(title: String): UiState<List<Movie>?>?{
        return withContext(Dispatchers.IO) {
            if (checkInternet()){
                getMovieSearch(title)
            }else{
                val movies: List<Movie> = listOf()
                UiState(false, value = movies)
            }
        }
    }

    private fun checkInternet(): Boolean{
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkInfo != null){
            return true
        }
        return false
    }

    private suspend fun getMoviesFromApi(): UiState<List<Movie>>{
        val response = api.getMovies()
        insertMovies(response)
        return UiState(isLoading = response.asDomainModel().isEmpty(), value = response.asDomainModel())
    }

    private suspend fun getMoviesFromDb(): UiState<List<Movie>>{
        val movies = database.movieDao.getMovies().first().asDomainModel()
        return UiState(isLoading = movies.isEmpty(), movies)
    }


    private suspend fun getMovieFromApi(id: String): Flow<MovieDetailEntity?> = flow{
        val response = api.getFullCast(id)
        emit(response.asMovieDetailEntity())
    }


    private fun getMovieFromDb(id: String): Flow<MovieDetailEntity?>{
        return database.movieDao.getMovieDetail(id)
    }

    private suspend fun getMovieSearch(title: String): UiState<List<Movie>?>?{
        val response = api.searchMovies(title).asList()
        return UiState(isLoading = response.isEmpty(), response)
    }
}