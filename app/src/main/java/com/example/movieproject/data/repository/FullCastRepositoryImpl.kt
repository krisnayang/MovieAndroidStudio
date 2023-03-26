package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.asDatabaseFullCast
import com.example.movieproject.data.remote.remotedatasource.asDatabaseMovieDetail
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FullCastRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: MovieDatabase,
    private val api: APIService
    ): FullCastRepository  {
    override suspend fun insertFullCast(fullCastResponse: NetworkMovieById){
        withContext(Dispatchers.IO){
            database.movieDao.insertAllFullCast(fullCastResponse.asDatabaseFullCast())
            database.movieDao.insertMovieDetail(fullCastResponse.asDatabaseMovieDetail())
        }
    }

     override suspend fun getFullCast(id: String): Flow<List<FullCastEntity>>{
         return withContext(Dispatchers.IO) {
//             throw NullPointerException()
             if (checkInternet()) {
                 getFullCastFromApi(id)
             } else {
                 getFullCastFromDB(id)
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

    private fun getFullCastFromDB(id: String): Flow<List<FullCastEntity>> {
        return database.movieDao.getFullCast(id)
    }

    private suspend fun getFullCastFromApi(id: String): Flow<List<FullCastEntity>> = flow{
        val response = api.getFullCast(id)
        insertFullCast(response)
        emit(response.asDatabaseFullCast())
    }
}