package com.example.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.utils.LocalMapper
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.response.CastItem
import com.example.core.data.source.remote.response.ResultsItem
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import com.example.core.testing.DataDummy
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
class RepositoryTest : TestCase() {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var repository: FakeRepository
    private val dummyMovie: List<Movie> = DataDummy.generateDummyMovie()
    private val listCaster: List<Caster> = DataDummy.generateDummyCaster()

    @Mock
    private lateinit var localDataSource: LocalDataSource

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeRepository(remoteDataSource, localDataSource)
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
                `when`(localDataSource.isFavoriteMovie(0)).thenReturn(true)
                `when`(localDataSource.isFavoriteMovie(1)).thenReturn(false)

                val result = repository.setFavoriteMovie(dummyMovie[0], "Movie", listCaster).take(2).toList()
                verify(localDataSource).isFavoriteMovie(0)
                verify(localDataSource).deleteMovie(LocalMapper.movieDomainToMovieEntity(dummyMovie[0], "Movie"))
                verify(localDataSource).deleteCaster(LocalMapper.casterDomainToCasterEntity(listCaster[0], 0))
                assert(result[0] is Resource.Loading<Boolean>)
                assertEquals(false, result[1].data)

                val result2 = repository.setFavoriteMovie(dummyMovie[1], "Movie", listCaster).take(2).toList()
                verify(localDataSource).isFavoriteMovie(1)
                verify(localDataSource).addMovie(LocalMapper.movieDomainToMovieEntity(dummyMovie[1], "Movie"))
                verify(localDataSource).addCaster(LocalMapper.casterDomainToCasterEntity(listCaster[0], 1))
                assert(result2[0] is Resource.Loading<Boolean>)
                assertEquals(true, result2[1].data)

            }

        }
    }

    @Test
    fun testGetMovies() {
        runBlocking {
            launch(Dispatchers.IO) {
                val dummyListResultItem = listOf(ResultsItem("28/10/2021", "Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum", 0))
                val errorMessage = "Error Tester"
                `when`(remoteDataSource.getPopularMovie("0")).thenReturn(flow { emit(ApiResponse.Empty) })
                `when`(remoteDataSource.getPopularMovie("1")).thenReturn(flow { emit(ApiResponse.Error(errorMessage)) })
                `when`(remoteDataSource.getPopularMovie("2")).thenReturn(flow { emit(ApiResponse.Success(dummyListResultItem)) })

                val data = repository.getMovies("0").take(2).toList()
                verify(remoteDataSource).getPopularMovie("0")
                assert(data[0] is Resource.Loading<PagingData<Movie>>)
                assertEquals(PagingData.empty<Movie>(), data[1].data)

                val data2 = repository.getMovies("1").take(2).toList()
                verify(remoteDataSource).getPopularMovie("1")
                assert(data2[0] is Resource.Loading<PagingData<Movie>>)
                assertEquals( errorMessage, data2[1].message)

                val data3 = repository.getMovies("2").take(2).toList()
                verify(remoteDataSource).getPopularMovie("2")
                assert(data3[0] is Resource.Loading<PagingData<Movie>>)
                assertNotNull(data3[1].data)
                assert(data3[1].data is PagingData<Movie>)
            }
        }
    }

    @Test
    fun testGetFavoriteMovies() {
        runBlocking {
            launch(Dispatchers.IO) {
                val dummyListEntityMovie = listOf(LocalMapper.movieDomainToMovieEntity(dummyMovie[0], "movie"))
                `when`(localDataSource.getMovies("Movie")).thenReturn(dummyListEntityMovie)

                val data = repository.getFavoriteMovies("Movie").take(2).toList()
                verify(localDataSource).getMovies("Movie")

                assert(data[0] is Resource.Loading<PagingData<Movie>>)
                assertNotNull(data[1].data)
            }
        }
    }

    @Test
    fun testIsFavoriteMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                `when`(localDataSource.isFavoriteMovie(0)).thenReturn(true)

                val data = repository.isFavoriteMovie(0).take(2).toList()
                verify(localDataSource).isFavoriteMovie(0)

                assert(data[0] is Resource.Loading<Boolean>)
                assertEquals(true, data[1].data)
            }
        }
    }

    @Test
    fun testGetCasters() {
        runBlocking {
            launch(Dispatchers.IO) {
                val dummyListCastItem = listOf(CastItem("Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum"))
                val dummyListCasterEntity = listCaster.map {
                    LocalMapper.casterDomainToCasterEntity(it, 0)
                }
                val errorMessage = "Error Tester"
                `when`(localDataSource.getCaster(0)).thenReturn(emptyList())
                `when`(localDataSource.getCaster(1)).thenReturn(dummyListCasterEntity)

                `when`(remoteDataSource.getCasterMovie("0", 0)).thenReturn(flow { emit(ApiResponse.Empty) })
                `when`(remoteDataSource.getCasterMovie("1", 0)).thenReturn(flow { emit(ApiResponse.Error(errorMessage)) })
                `when`(remoteDataSource.getCasterMovie("2", 0)).thenReturn(flow { emit(ApiResponse.Success(dummyListCastItem)) })

                val data = repository.getCasters("0", 0).take(2).toList()
                verify(localDataSource).getCaster(0)
                verify(remoteDataSource).getCasterMovie("0", 0)
                assert(data[0] is Resource.Loading<PagingData<Caster>>)
                assertEquals(PagingData.empty<Caster>(), data[1].data)

                val data2 = repository.getCasters("1", 0).take(2).toList()
                verify(remoteDataSource).getCasterMovie("1", 0)
                assert(data2[0] is Resource.Loading<PagingData<Caster>>)
                assertEquals( errorMessage, data2[1].message)

                val data3 = repository.getCasters("2", 0).take(2).toList()
                verify(remoteDataSource).getCasterMovie("2", 0)
                assert(data3[0] is Resource.Loading<PagingData<Caster>>)
                assertNotNull(data3[1].data)

                val data4 = repository.getCasters("0", 1).take(2).toList()
                verify(localDataSource).getCaster(1)
                assert(data4[0] is Resource.Loading<PagingData<Caster>>)
                assertNotNull(data4[1].data)
            }
        }
    }

    @Test
    fun testSearchMovie() {
        runBlocking {
            launch(Dispatchers.IO) {
                val dummyListResultItem = listOf(ResultsItem("28/10/2021", "Lorem Ipsum", "Lorem Ipsum", "Lorem Ipsum", 0))
                val errorMessage = "Error Tester"
                `when`(remoteDataSource.searchMovie("0", "t")).thenReturn(flow { emit(ApiResponse.Empty) })
                `when`(remoteDataSource.searchMovie("1", "t")).thenReturn(flow { emit(ApiResponse.Error(errorMessage)) })
                `when`(remoteDataSource.searchMovie("2", "t")).thenReturn(flow { emit(ApiResponse.Success(dummyListResultItem)) })

                val data = repository.searchMovie("0", "t").take(2).toList()
                verify(remoteDataSource).searchMovie("0", "t")
                assert(data[0] is Resource.Loading<PagingData<Movie>>)
                assertEquals(PagingData.empty<Movie>(), data[1].data)

                val data2 = repository.searchMovie("1", "t").take(2).toList()
                verify(remoteDataSource).searchMovie("1", "t")
                assert(data2[0] is Resource.Loading<PagingData<Movie>>)
                assertEquals( errorMessage, data2[1].message)

                val data3 = repository.searchMovie("2", "t").take(2).toList()
                verify(remoteDataSource).searchMovie("2", "t")
                assert(data3[0] is Resource.Loading<PagingData<Movie>>)
                assertNotNull(data3[1].data)
            }
        }
    }
}