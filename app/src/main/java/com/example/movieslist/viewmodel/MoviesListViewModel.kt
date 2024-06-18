package com.example.movieslist.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieslist.repository.MoviesListRepository

class MoviesListViewModel: ViewModel() {

    private val repository = MoviesListRepository()

    val items = MutableLiveData<ArrayList<String>>()
//    private val itemsLiveData: LiveData<ArrayList<String>> = items

    fun getItems(context: Context) {
        val names = repository.getItems(context)
        items.value = names
    }

//    private val model = MoviesListRepository(application)
//    val items : LiveData<List<String>>
//
//    init {
//        this.items = model.items
//    }
//
//    fun getItems() {
//        model.getItems()
//    }
}