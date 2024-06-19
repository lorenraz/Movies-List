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
import com.example.movieslist.viewmodel.MoviesViewModel

class MoviesListFragment : Fragment(), MoviesListAdapter.OnItemClickListener {

    private lateinit var viewModel: MoviesViewModel
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        recycler = view.findViewById(R.id.rv_movies_list)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        viewModel.namesLiveData.observe(viewLifecycleOwner) {
            val adapter = MoviesListAdapter(context, this, ArrayList(it))
            recycler.adapter = adapter
        }
        viewModel.getItems(context)
    }

    override fun onItemClick(name: String) {
        val bundle = Bundle()
        bundle.putString("name_key", name)

        val fragmentManager = parentFragmentManager
        val fragment = MovieFragment()
        fragment.arguments = bundle
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}