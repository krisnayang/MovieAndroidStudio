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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val database: MovieDatabase,
    private val api: APIService
): MovieRepository{
    private fun insertMovies(movieResponse: MoviesResponse){
        database.movieDao.insertAllMovie(movieResponse.asDatabaseFullCast())
    }

    override suspend fun getMovies(context: Context): UiState<List<Movie>>{
        return withContext(Dispatchers.IO) {
            if (checkInternet(context)) {
                getMoviesFromApi()
            } else {
                getMoviesFromDb()
            }
        }
    }
    override suspend fun getMovie(id: String, context: Context): UiState<MovieDetailEntity?>? {
        return withContext(Dispatchers.IO) {
            if (checkInternet(context)) {
                getMovieFromApi(id)
            } else {
                getMovieFromDb(id)
            }
        }
    }

    override suspend fun searchMovies(title: String, context: Context): UiState<List<Movie>?>?{
        return withContext(Dispatchers.IO) {
            if (checkInternet(context)){
                getMovieSearch(title)
            }else{
                val movies: List<Movie> = listOf()
                UiState(false, value = movies)
            }
        }
    }

    private fun checkInternet(context: Context): Boolean{
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

    private suspend fun getMovieFromApi(id: String): UiState<MovieDetailEntity?>? {
        val response = api.getFullCast(id)
        return UiState(isLoading = response.asDatabaseMovieDetail().isEmpty(), value = response.asDatabaseMovieDetail().first())
    }

    private suspend fun getMovieFromDb(id: String): UiState<MovieDetailEntity?>? {
        val movie = database.movieDao.getMovieDetail(id).first()
        return movie?.id?.let { UiState(isLoading = it.isEmpty() , movie) }
    }

    private suspend fun getMovieSearch(title: String): UiState<List<Movie>?>?{
        val response = api.searchMovies(title).asList()
        return UiState(isLoading = response.isEmpty(), response)
    }
}