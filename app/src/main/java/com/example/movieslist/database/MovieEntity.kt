package com.example.movieslist.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "moviesTable")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieName: String
)