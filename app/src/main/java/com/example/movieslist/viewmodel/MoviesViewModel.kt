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
















//    private val _namesLiveData = MutableLiveData<List<String>>()
//    val namesLiveData: LiveData<List<String>> get() = _namesLiveData
//
//    private var names: List<String> = listOf()
//
//    fun getItems(context: Context) {
//        //items store locally
//        if (names.isNotEmpty()) {
//            _namesLiveData.postValue(names)
//        }
//        else {
//            //items on prefs
//            if (isExistOnPref(context)) {
//                getFromPref(context)
//            } else {
//                //items on db
//                val database = MovieDatabase.getDatabase(context)
//                viewModelScope.launch {
//                    val roomData = withContext(Dispatchers.IO) {
//                        database.movieDao().getAll().map { it.movieName }
//                    }
//                    if (roomData.isNotEmpty()) {
//                        saveToPref(context)
//                        _namesLiveData.postValue(roomData)
//                    } else {
//                        //fetch
//                        val firebaseDatabase = FirebaseDatabase.getInstance()
//                        val namesRef = firebaseDatabase.getReference("name")
//
//                        namesRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                if (snapshot.exists()) {
//                                    val namesList = mutableListOf<String>()
//                                    for (nameSnapshot in snapshot.children) {
//                                        val name = nameSnapshot.getValue(String::class.java)
//                                        name?.let { namesList.add(it) }
//                                    }
//                                    names = namesList.toList()
//                                    saveToPref(context)
//                                    saveDataToRoom(database)
//
//                                    _namesLiveData.postValue(namesList)
//                                } else {
//                                    println("No data exists at the specified path")
//                                }
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                                println("Failed to read names: ${error.message}")
//                            }
//                        })
//                    }
//                }
//            }
//        }
//    }
//
//    //db
//    private fun saveDataToRoom(database: MovieDatabase) {
//        viewModelScope.launch {
//            val nameEntities = names.map { MovieEntity(movieName = it) }
//            database.movieDao().insertNames(nameEntities)
//        }
//    }
//
//    //Pref
//    private fun getFromPref(context: Context) {
//        val sharedPreferences = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
//        val namesString = sharedPreferences.getString("names", "") ?: ""
//        val namesList = namesString.split(",").toMutableList()
//
//        names = namesList.toList()
//        _namesLiveData.postValue(namesList)
//    }
//
//    private fun isExistOnPref(context: Context): Boolean {
//        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
//        return sharedPref.contains("names")
//    }
//
//    private fun saveToPref(context: Context) {
//        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        val namesString = names.joinToString(separator = ",")
//        editor.putString("names", namesString)
//        editor.apply()
//    }
//
//    //for test
//    fun setNames(list: List<String>) {
//        names = list
//    }



}