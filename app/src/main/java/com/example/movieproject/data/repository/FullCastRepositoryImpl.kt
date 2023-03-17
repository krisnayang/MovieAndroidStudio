package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.asDatabaseFullCast
import com.example.movieproject.data.remote.remotedatasource.asDatabaseMovieDetail
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FullCastRepositoryImpl(private val database: MovieDatabase): FullCastRepository  {
    override suspend fun insertFullCast(fullCastResponse: NetworkMovieById){
        withContext(Dispatchers.IO){
            database.movieDao.insertAllFullCast(fullCastResponse.asDatabaseFullCast())
            database.movieDao.insertMovieDetail(fullCastResponse.asDatabaseMovieDetail())
        }
    }

     override suspend fun getFullCast(id: String, context: Context): UiState<List<FullCastEntity>>{
         return withContext(Dispatchers.IO) {
             if (checkInternet(context)) {
                 getFullCastFromApi(id)
             } else {
                 getFullCastFromDB(id)
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

    private suspend fun getFullCastFromDB(id: String): UiState<List<FullCastEntity>>{
        val fullCast = database.movieDao.getFullCast(id).first()
        return UiState(isLoading = fullCast.isEmpty(), fullCast)
    }

    private suspend fun getFullCastFromApi(id: String): UiState<List<FullCastEntity>>{
        val response = Api.retrofitService.getFullCast(id)
        insertFullCast(response)
        return UiState(isLoading = response.asDatabaseFullCast().isEmpty(), value = response.asDatabaseFullCast())
    }
}