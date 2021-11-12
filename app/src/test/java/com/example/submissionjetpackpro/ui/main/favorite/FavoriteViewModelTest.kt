package com.example.submissionjetpackpro.ui.main.favorite

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
class FavoriteViewModelTest : TestCase() {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val listFavoriteMovie = DataDummy.generateDummyMovie()

    @Mock
    private lateinit var useCase: MovieUseCase

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        favoriteViewModel = FavoriteViewModel(useCase)
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
                val favoriteMovie = Resource.Success(PagingData.from(listFavoriteMovie))
                `when`(useCase.getFavoriteMovies("Movie")).thenReturn(flow { emit(favoriteMovie) })

                favoriteViewModel.getMovies("Movie")
                verify(useCase).getFavoriteMovies("Movie")
                val result = favoriteViewModel.movies.value
                assertNotNull(result)

                favoriteViewModel.movies.observeForever(observer)
                verify(observer).onChanged(favoriteMovie)
            }

        }
    }
}