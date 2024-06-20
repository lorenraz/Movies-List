package com.example.movieslist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieslist.R
import com.example.movieslist.adapter.MoviesListAdapter
import com.example.movieslist.database.MovieDatabase
import com.example.movieslist.repository.MoviesRepository
import com.example.movieslist.utils.Constants
import com.example.movieslist.viewmodel.MoviesViewModel
import com.example.movieslist.viewmodel.MoviesViewModelFactory

class MoviesListFragment : Fragment(), MoviesListAdapter.OnItemClickListener {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        recycler = view.findViewById(R.id.rv_movies_list)
        initRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun initRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun init() {
        val context = requireContext()
        val database = MovieDatabase.getDatabase(requireContext())
        val repository = MoviesRepository(database)

        //initialize the ViewModel
        viewModel = ViewModelProvider(this,
            MoviesViewModelFactory(repository))[MoviesViewModel::class.java]

        viewModel.namesLiveData.observe(viewLifecycleOwner) {
            val adapter = MoviesListAdapter(context, this, ArrayList(it))
            recycler.adapter = adapter
        }
        viewModel.getItems(context)


    }

    override fun onItemClick(name: String) {
        val bundle = Bundle()
        bundle.putString(Constants.MOVIE_NAME_KEY, name)
        val fragment = MovieFragment()
        fragment.arguments = bundle

        val activity = activity as? MainActivity
        activity?.initFragment(fragment, true)
    }
}