package com.example.core.data.source

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.Resource.Error
import com.example.core.data.Resource.Success
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.utils.LocalMapper
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.utils.RemoteMapper
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import com.example.core.domain.repository.IRepository
import com.example.core.helper.EspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {
    override suspend fun setFavoriteMovie(
        movie: Movie,
        categoryMovie: String,
        listCaster: List<Caster>
    ): Flow<Resource<Boolean>> = flow {
        EspressoIdlingResource.increment()
        emit(Resource.Loading())

        val isFavoriteMovie = localDataSource.isFavoriteMovie(movie.id)

        if (isFavoriteMovie) {
            localDataSource.deleteMovie(LocalMapper.movieDomainToMovieEntity(movie, categoryMovie))
            listCaster.forEach {
                localDataSource.deleteCaster(LocalMapper.casterDomainToCasterEntity(it, movie.id))
            }
            emit(Success(false))
        } else {
            localDataSource.addMovie(LocalMapper.movieDomainToMovieEntity(movie, categoryMovie))
            listCaster.forEach {
                localDataSource.addCaster(LocalMapper.casterDomainToCasterEntity(it, movie.id))
            }
            emit(Success(true))
        }
        EspressoIdlingResource.decrement()
    }

    override suspend fun getMovies(tag: String): Flow<Resource<PagingData<Movie>>> = flow {
        EspressoIdlingResource.increment()
        emit(Resource.Loading())
        remoteDataSource.getPopularMovie(tag).collect {
            when (it) {
                is ApiResponse.Empty -> emit(Success<PagingData<Movie>>(PagingData.empty()))
                is ApiResponse.Error -> emit(Error<PagingData<Movie>>(it.errorMessage))
                is ApiResponse.Success -> {
                    emit(
                        Success(PagingData.from(RemoteMapper.mapResponseMovieToDomainMovie(it.data)))
                    )
                }
            }
        }
        EspressoIdlingResource.decrement()
    }

    override suspend fun getFavoriteMovies(tag: String): Flow<Resource<PagingData<Movie>>> = flow {
        emit(Resource.Loading())
        EspressoIdlingResource.increment()
        val domainData = LocalMapper.movieEntityToMovieDomain(localDataSource.getMovies(tag))
        emit(Success(PagingData.from(domainData)))
        EspressoIdlingResource.decrement()
    }

    override suspend fun isFavoriteMovie(idMovie: Int): Flow<Resource<Boolean>> = flow {
        EspressoIdlingResource.increment()
        emit(Resource.Loading())
        emit(Success(localDataSource.isFavoriteMovie(idMovie)))
        EspressoIdlingResource.decrement()
    }

    override suspend fun getCasters(tag: String, idMovie: Int): Flow<Resource<PagingData<Caster>>> =
        flow {
            emit(Resource.Loading())
            EspressoIdlingResource.increment()
            val domainData = arrayListOf<Caster>()
                domainData.addAll(LocalMapper.casterEntityToCasterDomain(localDataSource.getCaster(idMovie)))

            if (domainData.isNullOrEmpty()) {
                remoteDataSource.getCasterMovie(tag, idMovie).collect {
                    when (it) {
                        is ApiResponse.Empty -> emit(Error<PagingData<Caster>>("Data is empty"))
                        is ApiResponse.Error -> emit(Error<PagingData<Caster>>(it.errorMessage))
                        is ApiResponse.Success -> {
                            domainData.addAll(RemoteMapper.mapResponseCasterToDomainCaster(it.data))
                            emit(
                                Success(PagingData.from(domainData))
                            )
                        }
                    }
                }
            } else {
                emit(Success(PagingData.from(domainData)))
            }

            EspressoIdlingResource.decrement()
        }

    override suspend fun searchMovie(
        tag: String,
        query: String
    ): Flow<Resource<PagingData<Movie>>> = flow {
        emit(Resource.Loading())
        EspressoIdlingResource.increment()
        remoteDataSource.searchMovie(tag, query).collect {
            when (it) {
                is ApiResponse.Empty -> emit(Success<PagingData<Movie>>(PagingData.empty()))
                is ApiResponse.Error -> emit(Error<PagingData<Movie>>(it.errorMessage))
                is ApiResponse.Success -> {
                    val domainData = RemoteMapper.mapResponseMovieToDomainMovie(it.data)
                    emit(Success(PagingData.from(domainData)))
                }
            }
        }
        EspressoIdlingResource.decrement()
    }
}