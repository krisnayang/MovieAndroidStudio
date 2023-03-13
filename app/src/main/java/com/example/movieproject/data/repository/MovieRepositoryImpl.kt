package com.example.movieproject.data.repository

import androidx.lifecycle.asLiveData
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl (private val database: MovieDatabase): MovieRepository{
    override suspend fun refreshMovie(){
        withContext(Dispatchers.IO){
            val movie = Api.retrofitService.getMovies()
            database.movieDao.insertAll(movie.asDatabaseModel())
        }
    }

    fun getMovies() = database.movieDao.getMovies().asLiveData()
    fun getMovie(id: String) = database.movieDao.getMovie(id)
}