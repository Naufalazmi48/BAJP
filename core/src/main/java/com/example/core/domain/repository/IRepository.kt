package com.example.core.domain.repository

import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun setFavoriteMovie(
        movie: Movie,
        categoryMovie: String,
        listCaster: List<Caster>
    ): Flow<Resource<Boolean>>

    suspend fun getMovies(tag: String): Flow<Resource<PagingData<Movie>>>
    suspend fun getFavoriteMovies(tag: String): Flow<Resource<PagingData<Movie>>>
    suspend fun isFavoriteMovie(idMovie: Int): Flow<Resource<Boolean>>
    suspend fun getCasters(tag: String, idMovie: Int): Flow<Resource<PagingData<Caster>>>
    suspend fun searchMovie(tag: String, query: String): Flow<Resource<PagingData<Movie>>>
}