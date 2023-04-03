package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.VisibleForTesting
import com.example.movieproject.data.local.dao.MovieDao
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.asDatabaseFullCast
import com.example.movieproject.data.remote.remotedatasource.asDatabaseMovieDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.movieproject.data.remote.network.Network

class FullCastRepositoryImpl @Inject constructor(
    private val network: Network,
    private val movieDao: MovieDao,
    private val api: APIService
    ): FullCastRepository {
    override suspend fun insertFullCast(fullCastResponse: NetworkMovieById){
            movieDao.insertAllFullCast(fullCastResponse.asDatabaseFullCast())
            movieDao.insertMovieDetail(fullCastResponse.asDatabaseMovieDetail())
    }

     override suspend fun getFullCast(id: String): Flow<List<FullCastEntity>>{
//             throw NullPointerException()
         return if (network.checkInternet()) {
             getFullCastFromApi(id)
         } else {
             getFullCastFromDB(id)
         }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getFullCastFromDB(id: String): Flow<List<FullCastEntity>> {
        return movieDao.getFullCast(id)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getFullCastFromApi(id: String): Flow<List<FullCastEntity>> = flow{
        val response = api.getFullCast(id)
        insertFullCast(response)
        emit(response.asDatabaseFullCast())
    }
}