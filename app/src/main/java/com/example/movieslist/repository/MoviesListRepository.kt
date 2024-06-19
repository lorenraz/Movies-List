package com.example.movieslist.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.database.MovieEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesListRepository {

    private var items: ArrayList<String>? = null
    private var movieDatabase: MovieDatabase? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var namesRef: DatabaseReference

    private fun arrayListToLiveData(arrayList: ArrayList<String>): LiveData<ArrayList<String>> {
        val data = MutableLiveData<ArrayList<String>>()
        val array = arrayListOf<String>()
        array.addAll(arrayList)
        data.postValue(array)
        return data
    }

    fun getItems(context: Context): LiveData<ArrayList<String>> {
        if (items != null) return arrayListToLiveData(items!!)

        if (isItemsExistOnPref(context)) return getItemsFromPref(context)

//        val data = MutableLiveData<ArrayList<String>>()
//        initDb(context)
//        isItemsExistOnDb { exists ->
//            if (exists) {
//                getItemsFromDb { names ->
//                    saveItemsToSharedPref(context, names)
//                    items = names
//                    data.postValue(names)
//                }
//            } else {
//                data.postValue(getAllNames(context).value)
//            }
//        }
//
//        return data

        val namesListFromApi = getAllNames(context)
        return namesListFromApi

    }

    //Shared Preferences
    private fun saveItemsToSharedPref(context: Context, items: ArrayList<String>) {
        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val arrayListString = convertArrayListToString(items)
        editor.putString("names", arrayListString)
        editor.apply()
    }

    private fun convertArrayListToString(arrayList: ArrayList<String>): String {
        return arrayList.joinToString(",")
    }

    private fun isItemsExistOnPref(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        return sharedPref.contains("names")
    }

    private fun getItemsFromPref(context: Context): LiveData<ArrayList<String>> {
        val sharedPreferences = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        val namesString = sharedPreferences.getString("names", "") ?: ""
        val namesList = namesString.split(",").toMutableList()

        val data = MutableLiveData<ArrayList<String>>()
        val array = arrayListOf<String>()
        array.addAll(namesList)
        data.postValue(array)
        return data
    }

    //DB
    private fun initDb(context: Context) {
        movieDatabase = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "name_database"
        ).build()
    }

    private fun saveItemsToDb(items: ArrayList<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieEntities = items.map { MovieEntity(movieName = it) }
            movieDatabase!!.movieDao().insertNames(movieEntities)
        }
    }

    private fun isItemsExistOnDb(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val entityItems = movieDatabase!!.movieDao().getAll()
            val result = entityItems.isNotEmpty()
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }

    private fun getItemsFromDb(callback: (ArrayList<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val entityItems = movieDatabase!!.movieDao().getAll()
            val stringItems = ArrayList(entityItems.map { it.movieName!! })
            withContext(Dispatchers.Main) {
                callback(stringItems)
            }
        }
    }

    //api
    private fun getAllNames(context: Context): LiveData<ArrayList<String>> {
        database = FirebaseDatabase.getInstance()
        namesRef = database.getReference("name")

        val data = MutableLiveData<ArrayList<String>>()

        namesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val namesList = mutableListOf<String>()
                for (nameSnapshot in snapshot.children) {
                    val name = nameSnapshot.getValue(String::class.java)
                    name?.let { namesList.add(it) }
                }
                val array = arrayListOf<String>()
                array.addAll(namesList)
                data.postValue(array)

                items = array

                //save to pref
                saveItemsToSharedPref(context, array)

                //save to db
//                    CoroutineScope(Dispatchers.IO).launch {
//                        saveItemsToDb(array)
//                    }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                println("Firebase error: ${error.message}")
            }
        })
        return data
    }
}