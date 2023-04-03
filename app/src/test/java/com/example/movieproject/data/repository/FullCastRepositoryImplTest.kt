package com.example.movieproject.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.example.movieproject.data.local.dao.MovieDao
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDatabase
import com.example.movieproject.data.remote.api.APIService
import com.example.movieproject.data.remote.network.Network
import com.example.movieproject.data.remote.remotedatasource.NetworkMovieById
import com.example.movieproject.data.remote.remotedatasource.asDatabaseFullCast
import com.example.movieproject.ui.state.Loading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.MockitoAnnotations.openMocks


@ExperimentalCoroutinesApi
internal class FullCastRepositoryImplTest {
    private lateinit var fullCastRepository: FullCastRepositoryImpl

    @Mock
    private lateinit var movieDao: MovieDao
    @Mock
    private lateinit var api: APIService
    @Mock
    private lateinit var network: Network

    @Before
    fun setup() {
        openMocks(this)
        fullCastRepository = FullCastRepositoryImpl(network, movieDao, api)
    }

    fun insertFullCast() {
    }

    @Test
    fun `test funGetFullCast Success has Internet`() = runTest{
        val data = emptyList<FullCastEntity>()
        `when`(network.checkInternet()).thenReturn(true)
        `when`(api.getFullCast("t")).thenReturn(NetworkMovieById())

        val result = fullCastRepository.getFullCast("t")
        Assert.assertEquals(data, result.first())
    }

    @Test
    fun `test funGetFullCast Success No Connection`() = runTest{
        val list = emptyList<FullCastEntity>()
        `when`(network.checkInternet()).thenReturn(false)
        `when`(movieDao.getFullCast("t")).thenReturn(flowOf(list))

        val result = fullCastRepository.getFullCast("t")
        Assert.assertEquals(list, result.first())
    }
}