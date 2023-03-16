package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.asDatabaseModel
import com.example.movieproject.data.remote.remotedatasource.asDatabaseMovieDetail
import com.example.movieproject.data.remote.remotedatasource.asDomainModel
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FullCastRepositoryImpl(private val database: MovieDatabase): FullCastRepository  {
    override suspend fun insertFullCast(id: String, context: Context){
        if (checkInternet(context)){
            withContext(Dispatchers.IO){
                insertDataApiToDB(id)
            }
        }
    }

     suspend fun getFullCast(id: String, context: Context): UiState<List<FullCastEntity>>{
         val fullCast = database.movieDao.getFullCast(id).first()
         return UiState(isLoading = fullCast.isEmpty(), fullCast)
    }

    private fun checkInternet(context: Context): Boolean{
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkInfo != null){
            return true
        }
        return false
    }

    private suspend fun insertDataApiToDB(id: String){
        val fullCast = Api.retrofitService.getFullCast(id)
        database.movieDao.insertAllFullCast(fullCast.asDatabaseModel())
        database.movieDao.insertMovieDetail(fullCast.asDatabaseMovieDetail())
    }
}