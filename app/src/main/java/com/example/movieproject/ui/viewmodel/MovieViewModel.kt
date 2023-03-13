package com.example.movieproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch
import java.io.IOException

class MovieViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))

    private val movies: LiveData<List<MovieEntity>> = movieRepositoryImpl.getMovies()

    fun getMovies(): LiveData<List<Movie>> = Transformations.map(movies){
        it.asDomainModel()
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
            if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(app) as T
            }

            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}