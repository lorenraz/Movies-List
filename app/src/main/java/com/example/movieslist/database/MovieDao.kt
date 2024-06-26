package com.example.movieslist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieslist.utils.Constants

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNames(names: List<MovieEntity>)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAll(): List<MovieEntity>
}