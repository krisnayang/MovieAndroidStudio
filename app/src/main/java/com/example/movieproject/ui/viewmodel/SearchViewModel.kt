package com.example.movieproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))


    private var _movies: List<Movie> = listOf()
    val movies: List<Movie> = _movies

    fun searchMovies(title: String) = viewModelScope.launch {
        try {
            _movies = movieRepositoryImpl.searchMovies(title)
        }catch (networkError: IOException) {

        }
    }

    fun refreshMovies() = viewModelScope.launch {
        try {
            movieRepositoryImpl.refreshMovie()
        } catch (networkError: IOException) {

        }
    }

    init {
        refreshMovies()
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