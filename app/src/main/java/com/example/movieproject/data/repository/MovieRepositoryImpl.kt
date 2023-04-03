package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.dao.MovieDao
import com.example.movieproject.data.local.localdatasource.*
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.network.ConnectivityObserver
import com.example.movieproject.data.remote.network.Network
import com.example.movieproject.data.remote.remotedatasource.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor(
    private val network: Network,
    private val movieDao: MovieDao,
    private val api: APIService
): MovieRepository{
    private fun insertMovies(movieResponse: MoviesResponse){
        movieDao.insertAllMovie(movieResponse.asDatabaseMovie())
    }

    override suspend fun insertFavourite(favourite: MoviesFavourite){
        movieDao.insertFavouriteMovie(favourite)
    }

    override suspend fun removeFavouriteMovie(movie: MoviesFavourite){
        movieDao.removeFavouriteMovie(movie.id)
    }

    override suspend fun getMovies(): Flow<List<MovieEntity>>{
//            throw NullPointerException()
        return if (network.checkInternet()) {
                getMoviesFromApi()
            } else {
                getMoviesFromDb()
            }

    }
    override suspend fun getMovie(id: String): Flow<MovieDetailEntity?> {
//            throw NullPointerException()
        return if (network.checkInternet()) {
                getMovieFromApi(id)
            } else {
                getMovieFromDb(id)
            }
    }

    override suspend fun getFavouriteMovies(): Flow<List<MoviesFavourite>> {
        return movieDao.getMoviesFavourite()
    }
    override suspend fun getFavouriteMovie(id: String): Flow<MoviesFavourite?> {
        return movieDao.getFavourite(id)
    }

    override suspend fun searchMovies(title: String): Flow<List<MovieLocal>>{
//            throw NullPointerException()
        return if (network.checkInternet()){
            getMovieSearchApi(title)
        }else{
            getMovieSearchDb()
        }
    }

    private suspend fun getMoviesFromApi(): Flow<List<MovieEntity>>{
        val response = api.getMovies()
        insertMovies(response)
        return flow{emit(response.asDatabaseMovie())}
    }

    private fun getMoviesFromDb(): Flow<List<MovieEntity>>{
        return movieDao.getMovies()
    }

    private suspend fun getMovieFromApi(id: String): Flow<MovieDetailEntity?> = flow{
        val response = api.getFullCast(id)
        emit(response.asMovieDetailEntity())
    }


    private fun getMovieFromDb(id: String): Flow<MovieDetailEntity?>{
        return movieDao.getMovieDetail(id)
    }

    private suspend fun getMovieSearchApi(title: String): Flow<List<MovieLocal>> = flow{
        emit(api.searchMovies(title).asList())
    }

    private suspend fun getMovieSearchDb(): Flow<List<MovieLocal>> = flow{
        emit(emptyList())
    }
}