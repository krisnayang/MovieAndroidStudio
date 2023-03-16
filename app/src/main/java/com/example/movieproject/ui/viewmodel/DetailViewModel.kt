package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.repository.FullCastRepositoryImpl
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))
    private val fullCastRepository = FullCastRepositoryImpl(MovieDatabase.getDatabase(application))

    private var _fullCast: MutableStateFlow<UiState<List<FullCastEntity>>> = MutableStateFlow(UiState(value = emptyList()))
    val fullCast: StateFlow<UiState<List<FullCastEntity>>> = _fullCast

    private var _movieDetail: MutableStateFlow<UiState<MovieDetailEntity>> = MutableStateFlow(UiState(value = MovieDetailEntity()))
    val movieDetail: StateFlow<UiState<MovieDetailEntity>> = _movieDetail

    fun getFullCast(id: String, context: Context) = viewModelScope.launch {
        try {
            fullCastRepository.insertFullCast(id, context)
            _fullCast.value = UiState(isLoading = true, value = emptyList())
            _movieDetail.value = UiState(isLoading = true, value = MovieDetailEntity())

            _fullCast.value = fullCastRepository.getFullCast(id, context)
            _movieDetail.value = movieRepositoryImpl.getMovie(id, context)
        }catch (networkError: IOException) {

        }
    }

    class Factory(
        val app: Application
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app) as T
            }

            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}