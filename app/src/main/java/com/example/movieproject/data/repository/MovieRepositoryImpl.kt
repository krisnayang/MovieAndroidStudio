package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.*
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.network.ConnectivityObserver
import com.example.movieproject.data.remote.remotedatasource.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    override suspend fun insertFavourite(favourite: MoviesFavourite){
        withContext(Dispatchers.IO){
            database.movieDao.insertFavouriteMovie(favourite)
        }
    }

    override suspend fun removeFavouriteMovie(movie: MoviesFavourite){
        withContext(Dispatchers.IO){
            database.movieDao.removeFavouriteMovie(movie.id)
        }
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

    override suspend fun getFavouriteMovies(): Flow<List<MoviesFavourite>> {
        return withContext(Dispatchers.IO){
            database.movieDao.getMoviesFavourite()
        }
    }
    override suspend fun getFavouriteMovie(id: String): Flow<MoviesFavourite?> {
        return withContext(Dispatchers.IO) {
            database.movieDao.getFavourite(id)
        }
    }

    override suspend fun searchMovies(title: String): Flow<List<MovieLocal>?>{
        return withContext(Dispatchers.IO) {
//            throw NullPointerException()
            if (checkInternet()){
                getMovieSearchApi(title)
            }else{
                getMovieSearchDb()
            }
        }
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

    private suspend fun getMovieSearchApi(title: String): Flow<List<MovieLocal>?> = flow{
        emit(api.searchMovies(title).asList())
    }

    private suspend fun getMovieSearchDb(): Flow<List<MovieLocal>?> = flow{
        emit(emptyList())
    }

    private fun checkInternet(): Boolean{
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkInfo != null){
            return true
        }
        return false
    }
}