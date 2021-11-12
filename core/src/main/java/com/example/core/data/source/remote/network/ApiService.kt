package com.example.core.data.source.remote.network

import com.example.core.BuildConfig
import com.example.core.data.source.remote.response.CasterResponse
import com.example.core.data.source.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularFilm(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCaster(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CasterResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getFilmCaster(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CasterResponse

    @GET("search/movie")
    suspend fun searchFilm(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
    ): MovieResponse

    @GET("search/tv")
    suspend fun searchTv(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
    ): MovieResponse

}