package com.example.movieslist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movieslist.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        println("loren MoviesListActivity")
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, MoviesListFragment())
//            .addToBackStack(null)  // Add this transaction to the back stack
//            .commit()


//        val fragmentManager = supportFragmentManager
//        val fragment = MoviesListFragment.newInstance()
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.add(R.id.fragment_container, fragment)
//        fragmentTransaction.commit()
    }
}