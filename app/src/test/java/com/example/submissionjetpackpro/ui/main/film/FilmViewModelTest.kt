package com.example.submissionjetpackpro.ui.main.film

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Movie
import com.example.core.domain.usecase.MovieUseCase
import com.example.core.testing.DataDummy
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FilmViewModelTest : TestCase() {
    private lateinit var filmViewModel: FilmViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private val listMovie = DataDummy.generateDummyMovie()

    @Mock
    private lateinit var useCase: MovieUseCase

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        filmViewModel = FilmViewModel(useCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<PagingData<Movie>>>

    @Test
    fun testGetMovies() {
        runBlocking {
            launch(Dispatchers.IO) {
                val movie = Resource.Success(PagingData.from(listMovie))
                `when`(useCase.getMovies("Movie")).thenReturn(flow { emit(movie) })

                filmViewModel.getMovies("Movie")
                verify(useCase).getMovies("Movie")
                val result = filmViewModel.movies.value
                assertNotNull(result)

                filmViewModel.movies.observeForever(observer)
                verify(observer).onChanged(movie)
            }

        }
    }

    @Test
    fun testSearchMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                val movie = Resource.Success(PagingData.from(listMovie))
                `when`(useCase.searchMovie("Movie", "T")).thenReturn(flow { emit(movie) })

                filmViewModel.searchMovie("Movie", "T")
                verify(useCase).searchMovie("Movie", "T")
                val result = filmViewModel.movies.value
                assertNotNull(result)

                filmViewModel.movies.observeForever(observer)
                verify(observer).onChanged(movie)

            }

        }
    }
}