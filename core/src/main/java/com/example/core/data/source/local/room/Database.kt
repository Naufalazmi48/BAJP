package com.example.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.local.entity.CasterEntity
import com.example.core.data.source.local.entity.MovieEntity

@Database(entities = [CasterEntity::class,MovieEntity::class],version = 1,exportSchema = false)
abstract class Database: RoomDatabase() {
abstract fun dao(): Dao
}