package com.example.movieslist.repository

import android.content.Context
import androidx.room.Room
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.database.MovieEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MoviesListRepository {

    private var items: ArrayList<String>? = null
    private var movieDatabase: MovieDatabase? = null

//    private val URL = "https://movieslist-a9fc4-default-rtdb.firebaseio.com/name"
    private lateinit var database: FirebaseDatabase
    private lateinit var namesRef: DatabaseReference

//    val items = MutableLiveData<List<String>>()

    companion object {
        private var instance: MoviesListRepository? = null

        // Static function to get the instance of UserRepository
        fun getInstance(): MoviesListRepository {
            if (instance == null) {
                instance = MoviesListRepository()
            }
            return instance!!
        }
    }

    fun getItems(context: Context): ArrayList<String> {
        println("loren repository getItems")
        return arrayListOf("alice", "bob")
        if (items != null) return items!!
        if (isItemsExistOnPref(context)) return getItemsFromPref(context)
        initDb(context)
        if (isItemsExistOnDb()) {
            val names: ArrayList<String> = getItemsFromDb()
            saveItemsToSharedPref(context, names)
            items = names
            return names
        }

        //TODO change return value

        getNames()
        //saveItemsToDb()
//        return items!!

        println("loren repo getItems return")
        return arrayListOf("alice", "bob")
    }

    //CACHE
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

    private fun getItemsFromPref(context: Context): ArrayList<String> {
        val sharedPreferences = context.getSharedPreferences("moviesPref", Context.MODE_PRIVATE)
        val arrayListString = sharedPreferences.getString("names", "") ?: ""
        val arrayList = arrayListString.split(",").toMutableList()
        return ArrayList(arrayList.filter { it.isNotBlank() })
    }


    //DB
    private fun initDb(context: Context) {
        movieDatabase = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "name_database"
        ).build()
    }

    private suspend fun saveItemsToDb(items: ArrayList<String>) {
        //convert
        val movieEntities = items.map { MovieEntity(movieName = it) }

        movieDatabase!!.movieDao().insertNames(movieEntities)
    }

    private fun isItemsExistOnDb(): Boolean {
//        return movieDatabase!!.movieDao().getCount() > 0
        val entityItems = movieDatabase!!.movieDao().getAll()
        return entityItems.isNotEmpty()
    }

    private fun getItemsFromDb(): ArrayList<String> {
        val entityItems = movieDatabase!!.movieDao().getAll()
        val stringItems = ArrayList(entityItems.map { it.movieName!! })
        return stringItems
    }

    //api
    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        namesRef = database.getReference("name")

        namesRef.get().addOnSuccessListener { snapshot ->
            val namesList = mutableListOf<String>()
            snapshot.children.forEach { child ->
                val name = child.getValue(String::class.java)
                name?.let {
                    namesList.add(it)
                }
            }
            println("loren names list: $namesList")
        }.addOnFailureListener { exception ->
            println("loren Failed to retrieve names: ${exception.message}")
        }
    }

    private fun getNames() {
        database = FirebaseDatabase.getInstance()
        namesRef = database.getReference("name")

        namesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val namesList = mutableListOf<String>()
                for (nameSnapshot in snapshot.children) {
                    val name = nameSnapshot.getValue(String::class.java)
                    name?.let { namesList.add(it) }
                }
                // Handle the list of names here
                println("loren firebase api. Names: $namesList")
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
                println("loren firebase api. Failed to read names: ${error.message}")
            }
        })
    }


}