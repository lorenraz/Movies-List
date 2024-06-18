package com.example.movieslist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieslist.R
import com.example.movieslist.adapter.MoviesListAdapter
import com.example.movieslist.model.Movie
import com.example.movieslist.repository.MoviesListRepository
import com.example.movieslist.viewmodel.MoviesListViewModel
import com.google.firebase.database.DatabaseReference

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesListFragment : Fragment(), MoviesListAdapter.OnItemClickListener {

    private lateinit var viewModel: MoviesListViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("loren MoviesListFragment onCreateView")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        recycler = view.findViewById(R.id.rv_movies_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("loren MoviesListFragment onViewCreated")

        viewModel = ViewModelProvider(this).get(MoviesListViewModel::class.java)

        val context = requireContext()

        viewModel.items.observe(viewLifecycleOwner, Observer {
            println("loren MoviesListFragment. observe to items in view model")
            val adapter = MoviesListAdapter(context, this)
            recycler.adapter = adapter
            adapter.setMoviesList(it)
            println("loren itemsCount:" + adapter.itemCount)
        })

        viewModel.getItems(context)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            MoviesListFragment().apply {

            }
    }


//    //TODO implement onItemClick - when clicking the movie name
//    fun onItemClick() {
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.main_activity, MovieFragment())
//            .addToBackStack(null)
//            .commit()
//    }

    override fun onItemClick(name: String) {
        //TODO("Not yet implemented")
        //TODO add argument name
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MovieFragment())
            .addToBackStack(null)
            .commit()
    }

}