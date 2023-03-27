package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.databinding.FragmentHomeBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var viewModelAdapter: MovieListAdapter? = null

    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!

    private var _viewBinding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val viewModel by viewModels<MovieViewModel>()

    override fun onStart() {
        super.onStart()
        getCurrentActivity()?.getBottomNav()?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)

        setupUi()

        viewModel.getMovieList()
        viewBinding.swipeContainer.setOnRefreshListener {
            viewModel.getMovieList()
            swipeContainer.isRefreshing = false
        }

        return viewBinding.root
    }

    private fun setupUi(){
        viewModelAdapter = MovieListAdapter(){movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = HomeFragmentDirections
                .actionHomeFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }

        setupObserver(viewModel)

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun setupObserver(viewModel: MovieViewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movies.collect{ state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> startShimmerEffect()
                            is Success<*> -> {
                                stopShimmerEffect()
                                dataLoaded(state.value as List<Movie>)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentActivity(): MainActivity?{
        return (activity as? MainActivity)
    }

    private fun dataLoaded(data: List<Movie>?){
        if (data?.isEmpty() == true) {
            viewBinding.noDataFound.visibility = View.VISIBLE
            viewBinding.noDataFavorite.visibility = View.GONE
        } else {
            viewModelAdapter?.submitList(data)
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.noDataFound.visibility = View.GONE
            viewBinding.noDataFavorite.visibility = View.GONE
        }
    }

    private fun startShimmerEffect(){
        viewBinding.shimmerContainer.startShimmer()
        viewBinding.shimmerContainer.visibility = View.VISIBLE
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.errorFound.visibility = View.GONE
        viewBinding.noDataFound.visibility = View.GONE
        viewBinding.noDataFavorite.visibility = View.GONE
    }

    private fun stopShimmerEffect(){
        lifecycleScope.launch {
            viewBinding.shimmerContainer.stopShimmer()
            viewBinding.shimmerContainer.visibility = View.GONE
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.errorFound.visibility = View.GONE
            viewBinding.noDataFavorite.visibility = View.GONE
        }
    }

    private fun errorFound(){
        viewBinding.shimmerContainer.stopShimmer()
        viewBinding.shimmerContainer.visibility = View.GONE
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.errorFound.visibility = View.VISIBLE
        viewBinding.noDataFound.visibility = View.GONE
        viewBinding.noDataFavorite.visibility = View.GONE
    }
}