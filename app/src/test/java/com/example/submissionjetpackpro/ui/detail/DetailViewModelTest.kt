package com.example.submissionjetpackpro.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Caster
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
class DetailViewModelTest : TestCase() {
    private lateinit var detailViewModel: DetailViewModel
    private val listMovie = DataDummy.generateDummyMovie()
    private val listCaster = DataDummy.generateDummyCaster()
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var useCase: MovieUseCase

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        detailViewModel = DetailViewModel(useCase)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observerCaster: Observer<Resource<PagingData<Caster>>>

    @Mock
    private lateinit var observerBoolean: Observer<Resource<Boolean>>

    @Test
    fun getCasters(){
        runBlocking {
            launch(Dispatchers.IO) {
                val caster = Resource.Success(PagingData.from(listCaster))
                `when`(useCase.getCaster("movie", 0)).thenReturn(flow { emit(caster) })

                detailViewModel.getCasters("movie", 0)
                verify(useCase).getCaster("movie", 0)
                val result = detailViewModel.casters.value
                assertNotNull(result)

                detailViewModel.casters.observeForever(observerCaster)
                verify(observerCaster).onChanged(caster)
            }
        }
    }

    @Test
    fun setFavoriteMovie(){
        runBlocking {
            launch(Dispatchers.IO) {
                val value = Resource.Success(true)
                `when`(useCase.setFavoriteMovie(listMovie[0], "movie", listCaster)).thenReturn(flow { emit(value) })

                detailViewModel.setFavoriteMovie(listMovie[0], "movie", listCaster)
                verify(useCase).setFavoriteMovie(listMovie[0], "movie", listCaster)
                val result = detailViewModel.favorite.value
                assertNotNull(result)

                detailViewModel.favorite.observeForever(observerBoolean)
                verify(observerBoolean).onChanged(value)
            }
        }
    }

    @Test
    fun isFavoriteMovie(){
        runBlocking {
            launch(Dispatchers.IO) {
                val value = Resource.Success(true)
                `when`(useCase.isFavoriteMovie(0)).thenReturn(flow { emit(value) })

                detailViewModel.isFavoriteMovie(0)
                verify(useCase).isFavoriteMovie(0)
                val result = detailViewModel.favorite.value
                assertNotNull(result)

                detailViewModel.favorite.observeForever(observerBoolean)
                verify(observerBoolean).onChanged(value)
            }
        }
    }
}