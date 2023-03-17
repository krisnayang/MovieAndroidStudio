package com.example.movieproject.data.repository

import android.content.Context
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.ui.state.UiState

interface FullCastRepository{
    suspend fun insertFullCast(fullCastResponse: NetworkMovieById)
    suspend fun getFullCast(id: String, context: Context): UiState<List<FullCastEntity>>
}