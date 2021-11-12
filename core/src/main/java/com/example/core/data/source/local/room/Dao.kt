package com.example.core.data.source.local.room

import androidx.room.*
import androidx.room.Dao
import com.example.core.data.source.local.entity.CasterEntity
import com.example.core.data.source.local.entity.MovieEntity

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMovie(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCaster(casterEntity: CasterEntity)

    @Query("SELECT * FROM movie WHERE category =:tag")
    suspend fun getMovies(tag: String): List<MovieEntity>

    @Query("SELECT * FROM caster WHERE id_movie =:idMovie")
    suspend fun getCaster(idMovie: Int): List<CasterEntity>

    @Query("SELECT * FROM movie WHERE id =:idMovie")
    suspend fun isFavoriteMovie(idMovie: Int): List<MovieEntity>

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteCaster(casterEntity: CasterEntity)
}