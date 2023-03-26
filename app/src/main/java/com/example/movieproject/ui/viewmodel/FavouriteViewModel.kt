package com.example.movieproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.NewUiState
import com.example.movieproject.ui.state.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var _movies: MutableStateFlow<NewUiState> = MutableStateFlow(Loading)
    val movies: StateFlow<NewUiState> = _movies

    fun getMovieList() = viewModelScope.launch {
        _movies.value = Loading
        try {
            movieRepository.getFavouriteMovies().collect {
                _movies.value = Success(value = it.asDomainModel())
            }
        } catch (e: Exception) {
            _movies.value = Error(errorMessage = e.toString())
        }
    }
}