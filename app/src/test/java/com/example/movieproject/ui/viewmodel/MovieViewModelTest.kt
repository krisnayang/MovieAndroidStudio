package com.example.movieproject.ui.viewmodel

import app.cash.turbine.test
import app.cash.turbine.testIn
import com.example.movieproject.MainCoroutineRule
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

@ExperimentalCoroutinesApi
internal class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MovieViewModel(repository)
    }

    //Success & data
    @Test
    fun `test getMovieList() data Success`() = mainCoroutineRule.dispatcher.runBlockingTest  {
        val list = emptyList<MovieEntity>()
        `when`(repository.getMovies()).thenReturn(flowOf(list))

        val res = viewModel.movies
        Assert.assertTrue(res.first() is Loading)

        viewModel.getMovieList()

        Assert.assertTrue(res.first() is Success<*>)
        Assert.assertEquals(list, (res.first() as Success<*>).value)
    }

    //Error & exception
    @Test
    fun `test getMovieList() data Error`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val expectedMovieList = NullPointerException()
//        `when`(repository.getMovies()).thenAnswer{expectedMovieList}

        viewModel.getMovieList()
        val res = viewModel.movies.value
        Assert.assertTrue(res is Error)
        Assert.assertEquals(expectedMovieList.toString() , (res as Error).errorMessage)
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

        val res = viewModel.movies
        backgroundScope.launch {
            viewModel.getMovieList()
        }
        Assert.assertTrue(res.first() is Loading)
        Assert.assertTrue(res.drop(1).first() is Error)
        Assert.assertEquals(expectedMoviesFavorite.toString() , (res.first() as Error).errorMessage)
    }
}