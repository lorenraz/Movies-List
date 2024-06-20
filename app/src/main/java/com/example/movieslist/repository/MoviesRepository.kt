package com.example.movieslist.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.database.MovieEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesRepository(private val database: MovieDatabase) {

    private val _namesLiveData = MutableLiveData<List<String>>()
    val namesLiveData: LiveData<List<String>> get() = _namesLiveData

    private var names: List<String> = listOf()

    suspend fun getItems(context: Context): LiveData<List<String>> {
        //items store locally
        if (names.isNotEmpty()) {
            _namesLiveData.postValue(names)
        } else {
            //items store on Pref
            if (isExistOnPref(context)) {
                getFromPref(context)
            } else {
                //items store on db
                val roomData = withContext(Dispatchers.IO) {
                    database.movieDao().getAll().map { it.movieName }
                }
                if (roomData.isNotEmpty()) {
                    saveToPref(context)
                    _namesLiveData.postValue(roomData)
                } else {
                    fetchFromFirebase(context)
                }
            }
        }
        return namesLiveData
    }

    private suspend fun fetchFromFirebase(context: Context) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val namesRef = firebaseDatabase.getReference("name")

        namesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val namesList = mutableListOf<String>()
                    for (nameSnapshot in snapshot.children) {
                        val name = nameSnapshot.getValue(String::class.java)
                        name?.let { namesList.add(it) }
                    }
                    names = namesList.toList()
                    saveToPref(context)

                    // Use CoroutineScope to call suspend functions
                    CoroutineScope(Dispatchers.IO).launch {
                        saveDataToRoom()
                        _namesLiveData.postValue(namesList)
                    }
                } else {
                    println("No data exists at the specified path")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read names: ${error.message}")
            }
        })
    }

    //db
    private suspend fun saveDataToRoom() {
        withContext(Dispatchers.IO) {
            val nameEntities = names.map { MovieEntity(movieName = it) }
            database.movieDao().insertNames(nameEntities)
        }
    }

    //Pref
    private fun getFromPref(context: Context) {
        val sharedPreferences = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        val namesString = sharedPreferences.getString("names", "") ?: ""
        val namesList = namesString.split(",").toMutableList()

        names = namesList.toList()
        _namesLiveData.postValue(namesList)
    }

    private fun isExistOnPref(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        return sharedPref.contains("names")
    }

    private fun saveToPref(context: Context) {
        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val namesString = names.joinToString(separator = ",")
        editor.putString("names", namesString)
        editor.apply()
    }

    //for test
    fun setNames(list: List<String>) {
        names = list
    }
}