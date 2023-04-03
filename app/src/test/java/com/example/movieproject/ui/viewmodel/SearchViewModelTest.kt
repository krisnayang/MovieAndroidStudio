package com.example.movieproject.ui.viewmodel

import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class SearchViewModelTest{
    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var repository: MovieRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SearchViewModel(repository)
    }

    //Success data found
    @Test
    fun `test search data with keyword Success`() = runTest{
        val list = emptyList<MovieLocal>()
        `when`(repository.searchMovies("")).thenReturn(flowOf(list))

        val res = viewModel.movies

        backgroundScope.launch {
            viewModel.searchMovies("")
        }

        Assert.assertTrue(res.drop(2).first() is Success<*>)
        Assert.assertEquals(list, (res.first() as Success<*>).value)
    }

    //Error
    @Test
    fun `test search data with keyword Error`() = runTest{
        val expectedMovieList = NullPointerException()
        `when`(repository.searchMovies("")).thenAnswer{throw expectedMovieList}

        val res = viewModel.movies
        backgroundScope.launch {
            viewModel.searchMovies("")
        }
        Assert.assertTrue(res.drop(2).first() is Error)
        Assert.assertEquals(expectedMovieList.toString() , (res.first() as Error).errorMessage)
    }
}