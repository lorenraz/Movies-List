package com.example.movieslist.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieslist.repository.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModel(private val repository: MoviesRepository): ViewModel() {

    val namesLiveData: LiveData<List<String>> get() = repository.namesLiveData

    fun getItems(context: Context) {
        viewModelScope.launch {
            repository.getItems(context)
        }
    }
}