package com.example.movieslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieslist.repository.MoviesRepository

class MoviesViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MoviesRepository::class.java).newInstance(repository)
    }
}