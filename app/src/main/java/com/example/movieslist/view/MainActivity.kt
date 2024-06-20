package com.example.movieslist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.movieslist.R
import com.example.movieslist.database.MovieDatabase
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var database: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()

        initFragment(MoviesListFragment(), false)
    }

    private fun initData() {
        FirebaseApp.initializeApp(this)

        database = Room.databaseBuilder(
            this,
            MovieDatabase::class.java, "movies_database"
        ).build()
    }

    fun initFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        if (addToBackStack) fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}