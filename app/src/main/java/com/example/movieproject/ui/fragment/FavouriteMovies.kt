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
import com.example.movieproject.databinding.FragmentFavouriteBinding
import com.example.movieproject.databinding.FragmentHomeBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.viewmodel.FavouriteViewModel
import com.example.movieproject.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavouriteMovies : Fragment(R.layout.fragment_favourite) {
    private var viewModelAdapter: MovieListAdapter? = null

    private val viewBinding: FragmentFavouriteBinding
        get() = _viewBinding!!

    private var _viewBinding: FragmentFavouriteBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private val viewModel by viewModels<FavouriteViewModel>()

    override fun onStart() {
        super.onStart()
        getCurrentActivity()?.getBottomNav()?.visibility = View.VISIBLE
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentFavouriteBinding.inflate(inflater, container, false)

        setupUi()

        viewModel.getMovieList()

        return viewBinding.root
    }

    private fun setupUi(){
        viewModelAdapter = MovieListAdapter(){movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = FavouriteMoviesDirections
                .actionInfoFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }

        setupObserver(viewModel)

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun setupObserver(viewModel: FavouriteViewModel){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movies.collect{ state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> startShimmerEffect()
                            is Success<*> -> {
                                stopShimmerEffect()
                                viewModelAdapter?.submitList(state.value as List<Movie>)
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

    private fun startShimmerEffect(){
        viewBinding.shimmerContainer.startShimmer()
        viewBinding.shimmerContainer.visibility = View.VISIBLE
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.errorFound.visibility = View.GONE
    }

    private fun stopShimmerEffect(){
        lifecycleScope.launch {
            viewBinding.shimmerContainer.stopShimmer()
            viewBinding.shimmerContainer.visibility = View.GONE
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.errorFound.visibility = View.GONE
        }
    }

    private fun errorFound(){
        viewBinding.shimmerContainer.stopShimmer()
        viewBinding.shimmerContainer.visibility = View.GONE
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.errorFound.visibility = View.VISIBLE
    }
}