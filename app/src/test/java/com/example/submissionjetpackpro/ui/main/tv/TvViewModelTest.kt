package com.example.submissionjetpackpro.ui.main.tv

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
class TvViewModelTest : TestCase(){
    private lateinit var tvViewModel: TvViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    private val listTv = DataDummy.generateDummyMovie()

    @Mock
    private lateinit var useCase: MovieUseCase

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        tvViewModel = TvViewModel(useCase)
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
    fun testGetTv() {
        runBlocking {
            launch(Dispatchers.IO) {
                val tv = Resource.Success(PagingData.from(listTv))
                `when`(useCase.getMovies("Tv")).thenReturn(flow { emit(tv) })

                tvViewModel.getTv("Tv")
                verify(useCase).getMovies("Tv")

                val result = tvViewModel.tv.value
                assertNotNull(result)

                tvViewModel.tv.observeForever(observer)
                verify(observer).onChanged(tv)
            }

        }
    }

    @Test
    fun testSearchTv() {
        runBlocking {
            launch(Dispatchers.IO) {
                val tv = Resource.Success(PagingData.from(listTv))
                `when`(useCase.searchMovie("Tv", "T")).thenReturn(flow { emit(tv) })

                tvViewModel.searchTv("Tv", "T")
                verify(useCase).searchMovie("Tv", "T")
                val result = tvViewModel.tv.value
                assertNotNull(result)

                tvViewModel.tv.observeForever(observer)
                verify(observer).onChanged(tv)
            }

        }
    }
}