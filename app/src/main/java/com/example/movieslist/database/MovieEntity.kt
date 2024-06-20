package com.example.movieslist.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieslist.utils.Constants

@Entity (tableName = Constants.TABLE_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val movieName: String
)