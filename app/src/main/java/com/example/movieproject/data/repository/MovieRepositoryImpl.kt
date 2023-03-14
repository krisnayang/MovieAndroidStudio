package com.example.movieproject.data.repository

import androidx.lifecycle.asLiveData
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.asDatabaseModel
import com.example.movieproject.data.remote.remotedatasource.asList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MovieRepositoryImpl (private val database: MovieDatabase): MovieRepository{
    override suspend fun refreshMovie(){
        withContext(Dispatchers.IO){
            val movie = Api.retrofitService.getMovies()
            database.movieDao.insertAllMovie(movie.asDatabaseModel())
        }
    }

    fun getMovies() = database.movieDao.getMovies().asLiveData()
    fun getMovie(id: String) = database.movieDao.getMovieDetail(id)

    suspend fun searchMovies(title: String): List<Movie>{
        return withContext(Dispatchers.IO) {
            delay(3000)
            Api.retrofitService.searchMovies(title).asList()
        }
    }
}