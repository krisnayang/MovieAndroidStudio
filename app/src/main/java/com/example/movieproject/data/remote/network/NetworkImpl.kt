package com.example.movieproject.data.remote.network

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

class NetworkImpl@Inject constructor(
    private val context: Context
): Network {
    override suspend fun checkInternet(): Boolean {
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (networkInfo != null){
            return true
        }
        return false
    }
}