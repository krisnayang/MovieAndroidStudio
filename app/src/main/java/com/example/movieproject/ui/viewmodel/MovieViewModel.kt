package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel(){
    private var _movies: MutableStateFlow<UiState<List<Movie>>> = MutableStateFlow(UiState(value = emptyList()))
    val movies: MutableStateFlow<UiState<List<Movie>>> = _movies

    fun getMovieList() = viewModelScope.launch {
        _movies.value = UiState(isLoading = true, value = emptyList())
        _movies.value = movieRepository.getMovies()
    }

}