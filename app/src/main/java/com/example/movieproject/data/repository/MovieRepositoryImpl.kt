package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


class MovieRepositoryImpl (private val database: MovieDatabase): MovieRepository{
    private fun insertMovies(movieResponse: MoviesResponse){
        database.movieDao.insertAllMovie(movieResponse.asDatabaseModel())
    }

    override suspend fun getMovies(context: Context): List<Movie>?{
        return withContext(Dispatchers.IO) {
            if (checkInternet(context)) {
                val response = Api.retrofitService.getMovies()
                insertMovies(response)
                response.asDomainModel()
            } else {
                database.movieDao.getMovies().first().asDomainModel()
            }
        }
    }
    fun getMovie(id: String) = database.movieDao.getMovieDetail(id)

    suspend fun searchMovies(title: String): List<Movie>{
        return withContext(Dispatchers.IO) {
            delay(3000)
            Api.retrofitService.searchMovies(title).asList()
        }
    }

    fun checkInternet(context: Context): Boolean{
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkInfo != null){
            return true
        }
        return false
    }
}