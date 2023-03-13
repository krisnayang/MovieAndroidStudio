package com.example.movieproject.data.repository

import com.example.movieproject.data.local.localdatasource.FullCastDatabase
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.model.FullCast

interface FullCastRepository{
    suspend fun insertFullCast(id: String)
}