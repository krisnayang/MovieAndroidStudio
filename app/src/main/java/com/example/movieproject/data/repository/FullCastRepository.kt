package com.example.movieproject.data.repository

import android.content.Context

interface FullCastRepository{
    suspend fun insertFullCast(id: String, context: Context)
}