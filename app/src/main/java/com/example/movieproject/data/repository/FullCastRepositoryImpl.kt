package com.example.movieproject.data.repository

import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FullCastRepositoryImpl(private val database: MovieDatabase): FullCastRepository  {
    override suspend fun insertFullCast(id: String){
        withContext(Dispatchers.IO){
            val fullCast = Api.retrofitService.getFullCast(id)
            database.movieDao.insertAllFullCast(fullCast.asDatabaseModel())
        }
    }

    fun getFullCast(id: String) = database.movieDao.getFullCast(id)
}