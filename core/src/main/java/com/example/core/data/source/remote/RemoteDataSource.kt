package com.example.core.data.source.remote

import android.util.Log
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.ApiService
import com.example.core.data.source.remote.response.CastItem
import com.example.core.data.source.remote.response.ResultsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun getPopularMovie(tag: String): Flow<ApiResponse<List<ResultsItem>>> = flow {
        try {
            var result: List<ResultsItem?> = arrayListOf()
            if (tag == TAG_FILM) {
                result = apiService.getPopularFilm().results ?: arrayListOf()
            } else if (tag == TAG_TV) {
                result = apiService.getPopularTv().results ?: arrayListOf()
            }
            if (result.isNullOrEmpty()) {
                emit(ApiResponse.Empty)
            } else {
                emit(ApiResponse.Success(result.filterNotNull()))
            }
        } catch (e: Exception) {
            Log.e("javaClass.name", e.message.toString())
            emit(ApiResponse.Error("Failed to get movie data"))
        }
    }

    suspend fun getCasterMovie(tag: String, idMovie: Int): Flow<ApiResponse<List<CastItem>>> =
        flow {
            try {
                var result: List<CastItem?> = arrayListOf()
                if (tag == TAG_FILM) {
                    result = apiService.getFilmCaster(idMovie).cast ?: arrayListOf()
                } else if (tag == TAG_TV) {
                    result = apiService.getTvCaster(idMovie).cast ?: arrayListOf()
                }
                if (result.isNullOrEmpty()) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Success(result.filterNotNull()))
                }
            } catch (e: Exception) {
                Log.e(javaClass.name, e.message.toString())
                emit(ApiResponse.Error("Failed to get caster data"))
            }
        }

    suspend fun searchMovie(tag: String, query: String): Flow<ApiResponse<List<ResultsItem>>> =
        flow {
            try {
                var result: List<ResultsItem?> = arrayListOf()
                if (tag == TAG_FILM) {
                    result = apiService.searchFilm(query = query).results ?: arrayListOf()
                } else if (tag == TAG_TV) {
                    result = apiService.searchTv(query = query).results ?: arrayListOf()
                }
                if (result.isNullOrEmpty()) {
                    emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Success(result.filterNotNull()))
                }
            } catch (e: Exception) {
                Log.e(javaClass.name, e.message.toString())
                emit(ApiResponse.Error("Failed to search movie data"))
            }
        }

    companion object {
        const val TAG_FILM = "FILM"
        const val TAG_TV = "TV"
    }
}