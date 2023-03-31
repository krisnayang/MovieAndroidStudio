package com.example.movieproject.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.UiState
import com.example.movieproject.ui.state.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var _movies: MutableStateFlow<UiState> =
        MutableStateFlow(Success<List<MovieLocal>>(emptyList()))
    val movies: StateFlow<UiState> = _movies

    fun searchMovies(title: String) = viewModelScope.launch {
        _movies.value = Loading
        try {
            movieRepository.searchMovies(title)?.collect {
                _movies.value = Success(value = it)
            }
        } catch (e: Exception) {
            _movies.value = Error(errorMessage = e.toString())
        }

    }
}