package com.example.movieproject.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.repository.FullCastRepository
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.NewUiState
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.movieproject.ui.state.Error

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val fullCastRepository: FullCastRepository
) : ViewModel() {

    private var _fullCastNew: MutableStateFlow<NewUiState> =
        MutableStateFlow(Loading)
    val fullCastNew: StateFlow<NewUiState> = _fullCastNew


    private var _movieDetail: MutableStateFlow<NewUiState?> =
        MutableStateFlow(Loading)
    val movieDetail: StateFlow<NewUiState?> = _movieDetail

    fun getFullCast(id: String) = viewModelScope.launch {
        _fullCastNew.value = Loading
        fullCastRepository.getFullCast(id).collect{
            try {
                _fullCastNew.value = Success(value = it)
            } catch (e: Exception) {
                _fullCastNew.value = Error(errorMessage = e.toString())
            }
        }
    }

    fun getMovieDetail(id: String) = viewModelScope.launch {
        _movieDetail.value = Loading
        movieRepository.getMovie(id).collect {
            try {
                _movieDetail.value = Success(value = it)
            } catch (e: Exception) {
                _movieDetail.value = Error(errorMessage = e.toString())
            }
        }
    }
}