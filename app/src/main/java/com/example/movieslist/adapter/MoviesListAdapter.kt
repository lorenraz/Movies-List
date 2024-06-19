package com.example.movieslist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieslist.R

class MoviesListAdapter(private val context: Context, private val listener: OnItemClickListener,
                        private val moviesList: ArrayList<String>):
    RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_view_child,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.name.text = moviesList[position]
        holder.name.setOnClickListener {
            listener.onItemClick(moviesList[position])
        }
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }
}


class MovieViewHolder(v: View): RecyclerView.ViewHolder(v) {
    val name: TextView = v.findViewById(R.id.rv_movie_name)
}
