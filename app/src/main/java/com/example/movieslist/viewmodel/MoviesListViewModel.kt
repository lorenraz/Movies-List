package com.example.movieslist.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieslist.repository.MoviesListRepository

class MoviesListViewModel: ViewModel() {

    private val repository = MoviesListRepository()

    fun getItems(context: Context): LiveData<ArrayList<String>> {
        val names = repository.getItems(context)
        return names
    }
}