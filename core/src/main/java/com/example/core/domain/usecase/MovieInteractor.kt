package com.example.core.domain.usecase

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import com.example.core.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val repository: IRepository): MovieUseCase {
    override suspend fun setFavoriteMovie(
        movie: Movie,
        categoryMovie: String,
        listCaster: List<Caster>
    ): Flow<Resource<Boolean>> = repository.setFavoriteMovie(movie, categoryMovie, listCaster)

    override suspend fun getMovies(tag: String): Flow<Resource<PagingData<Movie>>> =
        repository.getMovies(tag)

    override suspend fun getFavoriteMovies(tag: String): Flow<Resource<PagingData<Movie>>> =
        repository.getFavoriteMovies(tag)

    override suspend fun getCaster(tag: String, idMovie: Int): Flow<Resource<PagingData<Caster>>> =
        repository.getCasters(tag, idMovie)

    override suspend fun searchMovie(tag: String, query: String): Flow<Resource<PagingData<Movie>>> =
        repository.searchMovie(tag, query)

    override suspend fun isFavoriteMovie(idMovie: Int): Flow<Resource<Boolean>> =
        repository.isFavoriteMovie(idMovie)


}