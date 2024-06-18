package com.example.movieslist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "moviesTable")
data class MovieEntity(
    @PrimaryKey val uid: Int = 0,
    @ColumnInfo(name = "movie_name") val movieName: String?
)