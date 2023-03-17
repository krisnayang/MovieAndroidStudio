package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById

interface FullCastRepository{
    suspend fun insertFullCast(fullCastResponse: NetworkMovieById)
}