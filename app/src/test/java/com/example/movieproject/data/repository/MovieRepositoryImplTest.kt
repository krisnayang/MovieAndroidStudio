package com.example.movieproject.data.repository

import com.example.movieproject.data.local.dao.MovieDao
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.network.Network
import com.example.movieproject.data.remote.remotedatasource.MoviesResponse
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.SearchMovieResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


internal class MovieRepositoryImplTest {
    private lateinit var movieRepository: MovieRepositoryImpl

    @Mock
    private lateinit var movieDao: MovieDao
    @Mock
    private lateinit var api: APIService
    @Mock
    private lateinit var network: Network

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        movieRepository = MovieRepositoryImpl(network, movieDao, api)
    }

    @Test
    fun `test funGetMovies Success has Internet`() = runTest{
        val data = emptyList<MovieEntity>()
        `when`(network.checkInternet()).thenReturn(true)
        `when`(api.getMovies()).thenReturn(MoviesResponse())

        val result = movieRepository.getMovies()
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funGetMovies Success No Connection`() = runTest{
        val data = emptyList<MovieEntity>()
        `when`(network.checkInternet()).thenReturn(false)
        `when`(movieDao.getMovies()).thenReturn(flowOf(data))

        val result = movieRepository.getMovies()
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funGetMovie Success has Internet`() = runTest{
        val data = MovieDetailEntity()
        `when`(network.checkInternet()).thenReturn(true)
        `when`(api.getFullCast("t")).thenReturn(NetworkMovieById())

        val result = movieRepository.getMovie("t")
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funGetMovie Success No Connection`() = runTest{
        val data = MovieDetailEntity()
        `when`(network.checkInternet()).thenReturn(false)
        `when`(movieDao.getMovieDetail("t")).thenReturn(flowOf(data))

        val result = movieRepository.getMovie("t")
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funGetFavouriteMovies`() = runTest{
        val list = emptyList<MoviesFavourite>()
        `when`(movieDao.getMoviesFavourite()).thenReturn(flowOf(list))

        val result = movieRepository.getFavouriteMovies()
        Assert.assertEquals(list, result.first())
    }

    @Test
    fun `test funGetFavouriteMovie`() = runTest{
        val data = MoviesFavourite()
        `when`(movieDao.getFavourite("t")).thenReturn(flowOf(data))

        val result = movieRepository.getFavouriteMovie("t")
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funSearchMovies Success has Internet`() = runTest{
        val data = emptyList<MovieLocal>()
        `when`(network.checkInternet()).thenReturn(true)
        `when`(api.searchMovies("t")).thenReturn(SearchMovieResponse())

        val result = movieRepository.searchMovies("t")
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funSearchMovies Success No Connection`() = runTest{
        val data = emptyList<MovieLocal>()
        `when`(network.checkInternet()).thenReturn(false)

        val result = movieRepository.searchMovies("t")
        Assert.assertEquals(data, result.first())
    }
}