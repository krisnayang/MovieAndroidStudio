package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))

    private var _movies: MutableStateFlow<UiState<List<Movie>?>?> = MutableStateFlow(UiState(value = emptyList()))
    val movies: StateFlow<UiState<List<Movie>?>?> = _movies

    fun searchMovies(title: String, context: Context) = viewModelScope.launch {
        _movies.value = movieRepositoryImpl.searchMovies(title, context)
    }

    class Factory(
        val app: Application
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app) as T
            }

            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}