package com.example.movieproject.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.remote.network.ConnectivityObserver
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
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var _movies: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val movies: StateFlow<UiState> = _movies

    private var _moviesFavorite: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val moviesFavorite: StateFlow<UiState> = _moviesFavorite

    fun getMoviesFavorite() = viewModelScope.launch {
        _moviesFavorite.value = Loading
        try {
            movieRepository.getFavouriteMovies().collect {
                _moviesFavorite.value = Success(value = it.asDomainModel())
            }
        } catch (e: Exception) {
            _moviesFavorite.value = Error(errorMessage = e.toString())
        }
    }
    fun getMovieList(network: ConnectivityObserver.Status) = viewModelScope.launch {
        _movies.value = Loading
        try {
            movieRepository.getMovies(network).collect {
                _movies.value = Success(value = it.asDomainModel())
            }
        } catch (e: Exception) {
            _movies.value = Error(errorMessage = e.toString())
        }
    }
}