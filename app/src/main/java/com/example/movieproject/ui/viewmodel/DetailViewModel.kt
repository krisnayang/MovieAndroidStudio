package com.example.movieproject.ui.viewmodel

import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.data.repository.FullCastRepository
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.UiState
import com.example.movieproject.ui.state.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.movieproject.ui.state.Error
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val fullCastRepository: FullCastRepository
) : ViewModel() {

    private var _fullCast: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val fullCast: StateFlow<UiState> = _fullCast

    private var _movieDetail: MutableStateFlow<UiState?> = MutableStateFlow(Loading)
    val movieDetail: StateFlow<UiState?> = _movieDetail

    private var _favouriteMovie: MutableStateFlow<UiState?> = MutableStateFlow(Loading)
    val favouriteMovie: StateFlow<UiState?> = _favouriteMovie

    fun insertFavourite(id: String, image: String, title: String) {
        viewModelScope.launch(Dispatchers.IO){
            movieRepository.insertFavourite(MoviesFavourite(id, image, title))
        }
    }

    fun removeFavourite(movie: MoviesFavourite) {
        viewModelScope.launch(Dispatchers.IO){
            movieRepository.removeFavouriteMovie(movie)
        }
    }

    fun getFullCast(id: String) = viewModelScope.launch(Dispatchers.IO){
        _fullCast.value = Loading
        try {
            fullCastRepository.getFullCast(id).collect {
                _fullCast.value = Success(value = it)
            }
        } catch (e: Exception) {
            _fullCast.value = Error(errorMessage = e.toString())
        }

    }

    fun getMovieDetail(id: String) = viewModelScope.launch(Dispatchers.IO) {
        _movieDetail.value = Loading
        try {
            movieRepository.getMovie(id).collect {
                _movieDetail.value = Success(value = it)
            }
        } catch (e: Exception) {
            _movieDetail.value = Error(errorMessage = e.toString())
        }
    }

    fun getFavouriteMovie(id: String) = viewModelScope.launch(Dispatchers.IO){
        _favouriteMovie.value = Loading
        try {
            movieRepository.getFavouriteMovie(id).collect {
                _favouriteMovie.value = Success(value = it)
            }
        } catch (e: Exception) {
            _favouriteMovie.value = Error(errorMessage = e.toString())
        }
    }
}