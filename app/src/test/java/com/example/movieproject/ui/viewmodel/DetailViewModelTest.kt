package com.example.movieproject.ui.viewmodel

import com.example.movieproject.MainCoroutineRule
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.ui.state.Error
import com.example.movieproject.data.repository.FullCastRepository
import com.example.movieproject.data.repository.MovieRepository
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class DetailViewModelTest{
    private lateinit var viewModel: DetailViewModel

    @Mock
    private lateinit var movieRepository: MovieRepository
    @Mock
    private lateinit var fullcastRepository: FullCastRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = DetailViewModel(movieRepository, fullcastRepository)
    }

    //Fullcast Success
    @Test
    fun `test getFullCast data success`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val list = emptyList<FullCastEntity>()
//        val list = listOf(FullCastEntity(
//            "nm1517976",
//            "tt2906216",
//            "https://m.media-amazon.com/images/M/MV5BMTM4OTQ4NTU3NV5BMl5BanBnXkFtZTcwNjEwNDU0OQ@@._V1_Ratio1.0000_AL_.jpg",
//            "Chris Pine",
//            "Edgin"))
        Mockito.`when`(fullcastRepository.getFullCast("tt6710474")).thenReturn(flowOf(list))

        val res = viewModel.fullCast
        Assert.assertTrue(res.first() is Loading)

        viewModel.getFullCast("tt6710474")

        Assert.assertTrue(res.first() is Success<*>)
        Assert.assertEquals(list, (res.first() as Success<*>).value)
    }

    //FullCast Error
    @Test
    fun `test getFullCast data error`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val list = NullPointerException()
        `when`(fullcastRepository.getFullCast("tt6710474")).thenAnswer{throw list}

        viewModel.getFullCast("tt6710474")
        val res = viewModel.fullCast.value

        Assert.assertTrue(res is Error)
        Assert.assertEquals(list.toString(), (res as Error).errorMessage)
    }


    //Movie Detail Success
    @Test
    fun `test getMovieDetail data success`() = mainCoroutineRule.dispatcher.runBlockingTest {
//        val data = MovieDetailEntity()
        val data = MovieDetailEntity(
            "tt2906216",
            "https://m.media-amazon.com/images/M/MV5BZjAyMGMwYTEtNDk4ZS00YmY0LThhZjUtOWI4ZjFmZmU4N2I3XkEyXkFqcGdeQXVyMTEyNzQ1MTk0._V1_Ratio0.6904_AL_.jpg",
            "Dungeons & Dragons: Honor Among Thieves",
            "2023",
            134,
            "A charming thief and a band of unlikely adventurers embark on an epic quest to retrieve a lost relic, but things go dangerously awry when they run afoul of the wrong people.",
            "John Francis Daley, Jonathan Goldstein",
            "Action, Adventure, Fantasy",
            0.0,
            0
        )
        `when`(movieRepository.getMovie("tt6710474")).thenReturn(flowOf(data))

        val res = viewModel.movieDetail
        Assert.assertTrue(res.first() is Loading)

        viewModel.getMovieDetail("tt6710474")

        Assert.assertTrue(res.first() is Success<*>)
        Assert.assertEquals(data, (res.first() as Success<*>).value)
    }

    //Movie Detail Error
    @Test
    fun `test getMovieDetail data error`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val list = NullPointerException()
        `when`(movieRepository.getMovie("tt6710474")).thenAnswer{throw list}

        viewModel.getMovieDetail("tt6710474")
        val res = viewModel.movieDetail.value

        Assert.assertTrue(res is Error)
        Assert.assertEquals(list.toString(), (res as Error).errorMessage)
    }


    //Favorite Movie Success
    @Test
    fun `test getFavoriteMovie data success`() = mainCoroutineRule.dispatcher.runBlockingTest {
//        val data = MoviesFavourite()
        val data = MoviesFavourite(
            "tt2560078",
            "Boston Strangler",
            "https://m.media-amazon.com/images/M/MV5BN2FmYmM1ODctNjkzNC00MzcyLTkyOWYtZmZjNjY2ZmU3MmI3XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_Ratio0.6762_AL_.jpg"
        )
        `when`(movieRepository.getFavouriteMovie("tt2560078")).thenReturn(flowOf(data))

        val res = viewModel.favouriteMovie
        Assert.assertTrue(res.first() is Loading)

        viewModel.getFavouriteMovie("tt2560078")

        Assert.assertTrue(res.first() is Success<*>)
        Assert.assertEquals(data, (res.first() as Success<*>).value)
    }

    //Favorite Movie Error
    @Test
    fun `test getFavoriteMovie data error`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val list = NullPointerException()
        `when`(movieRepository.getFavouriteMovie("tt2560078")).thenAnswer{throw list}

        viewModel.getFavouriteMovie("tt2560078")
        val res = viewModel.favouriteMovie.value

        Assert.assertTrue(res is Error)
        Assert.assertEquals(list.toString(), (res as Error).errorMessage)
    }

    //Insert data favorite
    @Test
    fun `test insertFavorite data`() = mainCoroutineRule.dispatcher.runBlockingTest {
        viewModel.insertFavourite("id", "https:/image.com", "Title")
    }

    //Remove Data Favorite
    @Test
    fun `test removeFavorite data`() = mainCoroutineRule.dispatcher.runBlockingTest {
        val movie = MoviesFavourite()
        viewModel.removeFavourite(movie)
    }
}