package com.example.movieproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.FullCastDatabase
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.remote.model.FullCast
import com.example.movieproject.data.repository.FullCastRepositoryImpl
import com.example.movieproject.data.repository.MovieRepositoryImpl
import com.example.movieproject.databinding.FragmentDetailMovieBinding
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))
    private val fullCastRepository = FullCastRepositoryImpl(FullCastDatabase.getDatabase(application))

    fun retrieveMovie(id: String): LiveData<MovieEntity> = movieRepositoryImpl.getMovie(id).asLiveData()

    fun insertFullCast(id: String) = viewModelScope.launch {
        try {
            fullCastRepository.insertFullCast(id)
        }catch (networkError: IOException) {

        }
    }

    class Factory(
        val app: Application
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app) as T
            }

            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}