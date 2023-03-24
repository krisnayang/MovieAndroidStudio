package com.example.movieproject.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.repository.FullCastRepository
import com.example.movieproject.data.repository.FullCastRepositoryImpl
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val fullCastRepository: FullCastRepository
) : ViewModel() {

    private var _fullCast: MutableStateFlow<UiState<List<FullCastEntity>>> =
        MutableStateFlow(UiState(isLoading = true, value = emptyList()))
    val fullCast: StateFlow<UiState<List<FullCastEntity>>> = _fullCast

    private var _movieDetail: MutableStateFlow<UiState<MovieDetailEntity?>?> =
        MutableStateFlow(UiState(isLoading = true, value = MovieDetailEntity()))
    val movieDetail: StateFlow<UiState<MovieDetailEntity?>?> = _movieDetail

    fun getFullCast(id: String) = viewModelScope.launch {
        _fullCast.value = UiState(isLoading = true, value = emptyList())
        fullCastRepository.getFullCast(id).collect{
            _fullCast.value = UiState(isLoading = false, it)
        }
    }

    fun getMovieDetail(id: String) = viewModelScope.launch {
        _movieDetail.value = UiState(isLoading = true, value = MovieDetailEntity())
        movieRepository.getMovie(id).collect {
            _movieDetail.value = UiState(isLoading = false, value = it)
        }
    }
}