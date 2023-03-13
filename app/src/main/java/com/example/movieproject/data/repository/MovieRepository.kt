package com.example.movieproject.data.repository

interface MovieRepository {
    suspend fun refreshMovie()
}