package com.example.movieslist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.movieslist.R
import com.example.movieslist.database.MovieDatabase
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var database: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        database = Room.databaseBuilder(
            this,
            MovieDatabase::class.java, "movies_database"
        ).build()

        val fragmentManager = supportFragmentManager
        val fragment = MoviesListFragment()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}