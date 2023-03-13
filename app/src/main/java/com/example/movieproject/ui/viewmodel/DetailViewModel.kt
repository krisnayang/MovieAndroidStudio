package com.example.movieproject.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.repository.FullCastRepositoryImpl
import com.example.movieproject.data.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class DetailViewModel (
    application: Application
): AndroidViewModel(application){
    private val movieRepositoryImpl = MovieRepositoryImpl(MovieDatabase.getDatabase(application))
    private val fullCastRepository = FullCastRepositoryImpl(MovieDatabase.getDatabase(application))

    private var _fullCast: MutableLiveData<List<FullCastEntity>> = MutableLiveData()
    val fullCast:LiveData<List<FullCastEntity>> = _fullCast

    private var _movieDetail: MutableLiveData<MovieDetailEntity> = MutableLiveData()
    val movieDetail: LiveData<MovieDetailEntity> = _movieDetail

    fun insertFullCast(id: String) = viewModelScope.launch {
        try {
            fullCastRepository.insertFullCast(id)
            _fullCast.value = fullCastRepository.getFullCast(id).first()
            _movieDetail.value = movieRepositoryImpl.getMovie(id).first()
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