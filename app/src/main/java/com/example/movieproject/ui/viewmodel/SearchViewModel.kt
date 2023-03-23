package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel(){
    private var _movies: MutableStateFlow<UiState<List<Movie>?>?> = MutableStateFlow(UiState(value = emptyList()))
    val movies: StateFlow<UiState<List<Movie>?>?> = _movies

    fun searchMovies(title: String) = viewModelScope.launch {
        _movies.value = movieRepository.searchMovies(title)
    }
}