package com.example.movieproject.data.remote.network

import javax.inject.Inject

interface Network{
    suspend fun checkInternet(): Boolean
}