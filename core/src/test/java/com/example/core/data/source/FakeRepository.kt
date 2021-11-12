package com.example.core.data.source

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.data.source.local.LocalDataSource
import com.example.core.data.source.local.utils.LocalMapper
import com.example.core.data.source.remote.RemoteDataSource
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.utils.RemoteMapper
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import com.example.core.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FakeRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {
    override suspend fun setFavoriteMovie(
        movie: Movie,
        categoryMovie: String,
        listCaster: List<Caster>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        val isFavoriteMovie = localDataSource.isFavoriteMovie(movie.id)

        if (isFavoriteMovie) {
            localDataSource.deleteMovie(LocalMapper.movieDomainToMovieEntity(movie, categoryMovie))
            listCaster.forEach {
                localDataSource.deleteCaster(LocalMapper.casterDomainToCasterEntity(it, movie.id))
            }
            emit(Resource.Success(false))
        } else {
            localDataSource.addMovie(LocalMapper.movieDomainToMovieEntity(movie, categoryMovie))
            listCaster.forEach {
                localDataSource.addCaster(LocalMapper.casterDomainToCasterEntity(it, movie.id))
            }
            emit(Resource.Success(true))
        }

    }

    override suspend fun getMovies(tag: String): Flow<Resource<PagingData<Movie>>> = flow {
        emit(Resource.Loading())

        remoteDataSource.getPopularMovie(tag).collect {
            when (it) {
                is ApiResponse.Empty -> emit(Resource.Success<PagingData<Movie>>(PagingData.empty()))
                is ApiResponse.Error -> emit(Resource.Error<PagingData<Movie>>(it.errorMessage))
                is ApiResponse.Success -> {
                    emit(
                        Resource.Success(PagingData.from(RemoteMapper.mapResponseMovieToDomainMovie(it.data)))
                    )
                }
            }
        }

    }

    override suspend fun getFavoriteMovies(tag: String): Flow<Resource<PagingData<Movie>>> = flow {
        emit(Resource.Loading())

        val domainData = LocalMapper.movieEntityToMovieDomain(localDataSource.getMovies(tag))
        emit(Resource.Success(PagingData.from(domainData)))

    }

    override suspend fun isFavoriteMovie(idMovie: Int): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        emit(Resource.Success(localDataSource.isFavoriteMovie(idMovie)))

    }

    override suspend fun getCasters(tag: String, idMovie: Int): Flow<Resource<PagingData<Caster>>> =
        flow {
            emit(Resource.Loading())

            val domainData = arrayListOf<Caster>()
            domainData.addAll(LocalMapper.casterEntityToCasterDomain(localDataSource.getCaster(idMovie)))

            if (domainData.isNullOrEmpty()) {
                remoteDataSource.getCasterMovie(tag, idMovie).collect {
                    when (it) {
                        is ApiResponse.Empty -> emit(Resource.Success<PagingData<Caster>>(PagingData.empty()))
                        is ApiResponse.Error -> emit(Resource.Error<PagingData<Caster>>(it.errorMessage))
                        is ApiResponse.Success -> {
                            domainData.addAll(RemoteMapper.mapResponseCasterToDomainCaster(it.data))
                            emit(
                                Resource.Success(PagingData.from(domainData))
                            )
                        }
                    }
                }
            } else {
                emit(Resource.Success(PagingData.from(domainData)))
            }


        }

    override suspend fun searchMovie(
        tag: String,
        query: String
    ): Flow<Resource<PagingData<Movie>>> = flow {
        emit(Resource.Loading())

        remoteDataSource.searchMovie(tag, query).collect {
            when (it) {
                is ApiResponse.Empty -> emit(Resource.Success<PagingData<Movie>>(PagingData.empty()))
                is ApiResponse.Error -> emit(Resource.Error<PagingData<Movie>>(it.errorMessage))
                is ApiResponse.Success -> {
                    val domainData = RemoteMapper.mapResponseMovieToDomainMovie(it.data)
                    emit(Resource.Success(PagingData.from(domainData)))
                }
            }
        }

    }
}