package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.NewUiState
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private var _moviesNew: MutableStateFlow<NewUiState> =
        MutableStateFlow(Success<List<Movie>>(emptyList()))
    val moviesNew: StateFlow<NewUiState> = _moviesNew

    fun searchMovies(title: String) = viewModelScope.launch {
        _moviesNew.value = Loading
        try {
            movieRepository.searchMovies(title)?.collect {
                _moviesNew.value = Success(value = it)
            }
        } catch (e: Exception) {
            _moviesNew.value = Error(errorMessage = e.toString())
        }

    }
}