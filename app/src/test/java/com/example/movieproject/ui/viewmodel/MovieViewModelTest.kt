package com.example.movieproject.ui.viewmodel

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
internal class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @Before
    fun setup() {
        openMocks(this)
        viewModel = MovieViewModel(repository)
    }

    //Success & data
    @Test
    fun `test getMovieList() data Success`() = runTest{
        val list = emptyList<MovieEntity>()
        `when`(repository.getMovies()).thenReturn(flowOf(list))

        val res = viewModel.movies

        backgroundScope.launch {
            viewModel.getMovieList()
        }

        Assert.assertTrue(res.first() is Loading)
        Assert.assertTrue(res.drop(1).first() is Success<*>)
        Assert.assertEquals(list, (res.first() as Success<*>).value)
    }

    //Error & exception
    @Test
    fun `test getMovieList() data Error`() = runTest{
        val expectedMovies = NullPointerException()
        `when`(repository.getMovies()).thenAnswer{throw expectedMovies}

        val res = viewModel.movies
        backgroundScope.launch {
            viewModel.getMovieList()
        }
        Assert.assertTrue(res.first() is Loading)
        Assert.assertTrue(res.drop(1).first() is Error)
        Assert.assertEquals(expectedMovies.toString() , (res.first() as Error).errorMessage)
    }

    //Success & data
    @Test
    fun `test getMoviesFavorite() data Success`() = runTest{
            val expectedMoviesFavorite = emptyList<MoviesFavourite>()
            `when`(repository.getFavouriteMovies()).thenReturn(flowOf(expectedMoviesFavorite))

            val res = viewModel.moviesFavorite

            backgroundScope.launch {
                viewModel.getMoviesFavorite()
            }
            Assert.assertTrue(res.first() is Loading)

            Assert.assertTrue(res.drop(1).first() is Success<*>)
            Assert.assertEquals(expectedMoviesFavorite, (res.first() as Success<*>).value)
        }
    //Error & exception
    @Test
    fun `test getMoviesFavorite() data Error`() = runTest {
        val expectedMoviesFavorite = NullPointerException()
        `when`(repository.getFavouriteMovies()).thenAnswer{throw expectedMoviesFavorite}

        val res = viewModel.moviesFavorite
        backgroundScope.launch {
            viewModel.getMoviesFavorite()
        }
        Assert.assertTrue(res.first() is Loading)
        Assert.assertTrue(res.drop(1).first() is Error)
        Assert.assertEquals(expectedMoviesFavorite.toString() , (res.first() as Error).errorMessage)
    }
}