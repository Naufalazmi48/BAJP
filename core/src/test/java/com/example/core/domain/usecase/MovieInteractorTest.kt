package com.example.core.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.repository.IRepository
import com.example.core.testing.DataDummy
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
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
class MovieInteractorTest : TestCase() {
    private lateinit var movieInteractor: MovieInteractor
    private val testDispatcher = TestCoroutineDispatcher()
    private val listMovie = DataDummy.generateDummyMovie()
    private val listCaster = DataDummy.generateDummyCaster()

    @Mock
    private lateinit var repository: IRepository

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        movieInteractor = MovieInteractor(repository)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Test
    fun testSetFavoriteMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                val value = Resource.Success(true)
                `when`(repository.setFavoriteMovie(listMovie[0],"Movie", listCaster)).thenReturn(flow { emit(value) })

                val result = movieInteractor.setFavoriteMovie(listMovie[0],"Movie", listCaster).first()
                verify(repository).setFavoriteMovie(listMovie[0],"Movie", listCaster)
                assertEquals(true, result.data)
            }
        }
    }

    @Test
    fun testGetMovies() {
        runBlocking {
            launch(Dispatchers.IO) {
                val movie = Resource.Success(PagingData.from(listMovie))
                `when`(repository.getMovies("Movie")).thenReturn(flow { emit(movie) })

                val resultMovie = movieInteractor.getMovies("Movie").first()
                verify(repository).getMovies("Movie")

                assertEquals(movie, resultMovie)
            }

        }
    }

    @Test
    fun testGetFavoriteMovies() {
        runBlocking {
            launch(Dispatchers.IO) {
                val movie = Resource.Success(PagingData.from(listMovie))
                `when`(repository.getFavoriteMovies("Movie")).thenReturn(flow { emit(movie) })

                val resultFavorite = movieInteractor.getFavoriteMovies("Movie").first()
                verify(repository).getFavoriteMovies("Movie")

                assertEquals(movie, resultFavorite)
            }

        }
    }

    @Test
    fun testGetCaster() {
        runBlocking {
            launch(Dispatchers.IO) {
                val caster = Resource.Success(PagingData.from(listCaster))
                `when`(repository.getCasters("Movie", 0)).thenReturn(flow { emit(caster) })

                val resultCaster = movieInteractor.getCaster("Movie", 0).first()
                verify(repository).getCasters("Movie", 0)

                assertEquals(caster, resultCaster)
            }

        }
    }

    @Test
    fun testSearchMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                val movie = Resource.Success(PagingData.from(listMovie))
                `when`(repository.searchMovie("Movie", "T")).thenReturn(flow { emit(movie) })

                val resultSearchMovie = movieInteractor.searchMovie("Movie", "T").first()
                verify(repository).searchMovie("Movie", "T")

                assertEquals(movie, resultSearchMovie)
            }

        }
    }

    @Test
    fun testIsFavoriteMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                val value = Resource.Success(true)
                `when`(repository.isFavoriteMovie(0)).thenReturn(flow { emit(value) })

                val result = movieInteractor.isFavoriteMovie(0).first()
                verify(repository).isFavoriteMovie(0)
                assertEquals(true, result.data)
            }
        }
    }
}