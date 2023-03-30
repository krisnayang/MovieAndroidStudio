package com.example.movieproject.ui.viewmodel

import app.cash.turbine.test
import com.example.movieproject.MainCoroutineRule
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.asDomainModel
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.remote.network.ConnectivityObserver
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class MovieViewModelTest{
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var repository: MovieRepository

//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        viewModel = MovieViewModel(repository)
    }

    @Test
    fun `test getMovieList()`() = mainCoroutineRule.dispatcher.runBlockingTest{
        val expectedMovieList = flow{
            emit(
                listOf(MovieEntity("tt10366206",
                    "John Wick: Chapter 4",
                    "https://m.media-amazon.com/images/M/MV5BMDExZGMyOTMtMDgyYi00NGIwLWJhMTEtOTdkZGFjNmZiMTEwXkEyXkFqcGdeQXVyMjM4NTM5NDY@._V1_UX128_CR0,12,128,176_AL_.jpg")))
        }
        `when`(repository.getMovies(ConnectivityObserver.Status.Available)).thenReturn(expectedMovieList)

        viewModel.getMovieList(ConnectivityObserver.Status.Available)
        val res = viewModel.movies.value
        Assert.assertTrue(res is Success<*>)
        Assert.assertEquals((res as Success<*>).value, listOf(MovieLocal("tt10366206",
            "John Wick: Chapter 4",
            "https://m.media-amazon.com/images/M/MV5BMDExZGMyOTMtMDgyYi00NGIwLWJhMTEtOTdkZGFjNmZiMTEwXkEyXkFqcGdeQXVyMjM4NTM5NDY@._V1_UX128_CR0,12,128,176_AL_.jpg")))
    }

    @Test
    fun `test getMoviesFavorite()`(){

    }
}