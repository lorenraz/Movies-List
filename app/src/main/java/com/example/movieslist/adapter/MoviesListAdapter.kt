package com.example.movieslist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieslist.R

class MoviesListAdapter(private val context: Context, private val listener: OnItemClickListener):
    RecyclerView.Adapter<MovieViewHolder>() {

    private var moviesList: List<String> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setMoviesList(list: List<String>) {
        println("loren adapter setMoviesList")
        this.moviesList = list
        println("loren adapter setMoviesList. list size: "+moviesList.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        println("loren adapter onCreateViewHolder.")
        return MovieViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_view_child,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        println("loren adapter onBindViewHolder. value: "+moviesList[position])
        holder.name.text = moviesList[position]
        holder.name.setOnClickListener {
            listener.onItemClick(moviesList[position])
        }
//        holder.name.setOnClickListener {
//            val intent = Intent(context, MovieFragment::class.java)
//            intent.putExtra("name", list[position])
//            context.startService(intent)
//        }
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
