package com.example.movieproject.data.repository

import com.example.movieproject.data.local.localdatasource.FullCastDatabase
import com.example.movieproject.data.remote.api.Api
import com.example.movieproject.data.remote.remotedatasource.asDatabaseModel
import com.example.movieproject.data.remote.remotedatasource.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FullCastRepositoryImpl(private val database: FullCastDatabase): FullCastRepository  {
    override suspend fun insertFullCast(id: String){
        withContext(Dispatchers.IO){
            val fullCast = Api.retrofitService.getFullCast(id)
            database.fullCastDao.insertAll(fullCast.asDatabaseModel())
        }
    }
}