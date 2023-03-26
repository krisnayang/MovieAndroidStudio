package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.remotedatasource.*
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
        database.movieDao.insertAllMovie(movieResponse.asDatabaseMovie())
    }

    override suspend fun getMovies(): Flow<List<MovieEntity>>{
        return withContext(Dispatchers.IO) {
//            throw NullPointerException()
            if (checkInternet()) {
                getMoviesFromApi()
            } else {
                getMoviesFromDb()
            }
        }
    }
    override suspend fun getMovie(id: String): Flow<MovieDetailEntity?> {
        return withContext(Dispatchers.IO) {
//            throw NullPointerException()
            if (checkInternet()) {
                getMovieFromApi(id)
            } else {
                getMovieFromDb(id)
            }
        }
    }

    override suspend fun searchMovies(title: String): Flow<List<Movie>?>{
        return withContext(Dispatchers.IO) {
//            throw NullPointerException()
            if (checkInternet()){
                getMovieSearchApi(title)
            }else{
                getMovieSearchDb()
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

    private suspend fun getMoviesFromApi(): Flow<List<MovieEntity>>{
        val response = api.getMovies()
        insertMovies(response)
        return flow{emit(response.asDatabaseMovie())}
    }

    private fun getMoviesFromDb(): Flow<List<MovieEntity>>{
        return database.movieDao.getMovies()
    }


    private suspend fun getMovieFromApi(id: String): Flow<MovieDetailEntity?> = flow{
        val response = api.getFullCast(id)
        emit(response.asMovieDetailEntity())
    }


    private fun getMovieFromDb(id: String): Flow<MovieDetailEntity?>{
        return database.movieDao.getMovieDetail(id)
    }

    private suspend fun getMovieSearchApi(title: String): Flow<List<Movie>?> = flow{
        emit(api.searchMovies(title).asList())
    }

    private suspend fun getMovieSearchDb(): Flow<List<Movie>?> = flow{
        emit(emptyList())
    }
}