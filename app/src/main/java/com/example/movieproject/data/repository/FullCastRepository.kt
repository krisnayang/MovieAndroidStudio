package com.example.movieproject.data.repository

interface FullCastRepository{
    suspend fun insertFullCast(id: String)
}