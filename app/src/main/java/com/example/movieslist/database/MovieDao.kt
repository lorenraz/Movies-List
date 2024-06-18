package com.example.movieslist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert
    suspend fun insertNames(names: List<MovieEntity>)

    @Query("SELECT * FROM moviesTable")
    fun getAll(): List<MovieEntity>

    //TODO delete
    @Query("SELECT COUNT(*) FROM moviesTable")
    suspend fun getCount(): Int

    @Query("DELETE FROM moviesTable")
    suspend fun deleteAllNames()
}