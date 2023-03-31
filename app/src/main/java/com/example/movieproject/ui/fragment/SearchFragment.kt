package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.databinding.FragmentSearchBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    val viewModel by viewModels<SearchViewModel>()
    private var viewModelAdapter: MovieListAdapter? = null

    private var searchJob: Job? = null

    private val viewBinding: FragmentSearchBinding
        get() = _viewBinding!!

    private var _viewBinding: FragmentSearchBinding? = null


    override fun onStart() {
        super.onStart()
        getCurrentActivity()?.getBottomNav()?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSearchBinding.inflate(inflater, container, false)

        setupUi()
        viewBinding.etSearchMovie.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            startShimmerEffect()
                            delay(3000)
//                            connectivityObserver.observe().onEach {
//                                viewModel.searchMovies(it, s.toString())
//                            }.launchIn(lifecycleScope)
                            viewModel.searchMovies(s.toString())
                        }
                    }
                }
            }
        })
        return viewBinding.root
    }

    private fun setupUi() {
        viewModelAdapter = MovieListAdapter { movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = SearchFragmentDirections
                .actionSearchFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }

        setupObserver(viewModel)

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect() {
        viewBinding.shimmerContainer.visibility = View.VISIBLE
        viewBinding.shimmerContainer.startShimmer()
        viewBinding.recyclerView.visibility = View.GONE
        viewBinding.notFound.visibility = View.GONE
        viewBinding.errorFound.visibility = View.GONE
    }

    private fun stopShimmerEffect() {
        lifecycleScope.launch {
            viewBinding.shimmerContainer.stopShimmer()
            viewBinding.shimmerContainer.visibility = View.GONE
            viewBinding.errorFound.visibility = View.GONE
        }
    }

    private fun setupObserver(viewModel: SearchViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movies.collect { state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> startShimmerEffect()
                            is Success<*> -> {
                                stopShimmerEffect()
                                dataLoaded(state.value as List<MovieLocal>?)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dataLoaded(data: List<MovieLocal>?){
        if (data?.isEmpty() == true) {
            viewBinding.notFound.visibility = View.VISIBLE
        } else {
            viewModelAdapter?.submitList(data)
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.notFound.visibility = View.GONE
        }
    }

    private fun errorFound(){
        stopShimmerEffect()
        viewBinding.errorFound.visibility = View.VISIBLE
    }
}