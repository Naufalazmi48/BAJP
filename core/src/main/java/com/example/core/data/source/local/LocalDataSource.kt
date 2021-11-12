package com.example.core.data.source.local

import android.util.Log
import com.example.core.data.source.local.entity.CasterEntity
import com.example.core.data.source.local.entity.MovieEntity
import com.example.core.data.source.local.room.Dao

class LocalDataSource(private val dao: Dao) {
    suspend fun addMovie(movieEntity: MovieEntity): Boolean =
        try {
            dao.addMovie(movieEntity)
            true
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            false
        }

    suspend fun addCaster(casterEntity: CasterEntity): Boolean =
        try {
            dao.addCaster(casterEntity)
            true
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            false
        }

    suspend fun getMovies(tag: String): List<MovieEntity> =
        try {
            dao.getMovies(tag)
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            emptyList()
        }

    suspend fun getCaster(idMovie: Int): List<CasterEntity> =
        try {
            dao.getCaster(idMovie)
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            emptyList()
        }

    suspend fun deleteMovie(movieEntity: MovieEntity): Boolean =
        try {
            dao.deleteMovie(movieEntity)
            true
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            false
        }

    suspend fun deleteCaster(casterEntity: CasterEntity): Boolean =
        try {
            dao.deleteCaster(casterEntity)
            true
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            false
        }

    suspend fun isFavoriteMovie(idMovie: Int): Boolean =
        try {
            !dao.isFavoriteMovie(idMovie).isNullOrEmpty()
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message.toString())
            false
        }
}