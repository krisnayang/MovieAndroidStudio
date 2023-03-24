package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.movieproject.databinding.FragmentHomeBinding
import com.example.movieproject.databinding.FragmentSearchBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        launch {
                            viewBinding.notFound.visibility = View.GONE
                            startShimmerEffect()
                            delay(3000)

                            viewModel.searchMovies(s.toString())
                            setupObserver(viewModel)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        return viewBinding.root
    }

    private fun setupUi() {
        viewModelAdapter = MovieListAdapter() { movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = SearchFragmentDirections
                .actionSearchFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }

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
    }

    private fun stopShimmerEffect() {
        lifecycleScope.launch {
            delay(1000)
            viewBinding.shimmerContainer.stopShimmer()
            viewBinding.shimmerContainer.visibility = View.GONE
            viewBinding.recyclerView.visibility = View.VISIBLE
            viewBinding.notFound.visibility = View.GONE
        }
    }

    private fun setupObserver(viewModel: SearchViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movies.collect {
                        viewModelAdapter?.submitList(it?.value)

                        if (it?.isLoading == false) {
                            stopShimmerEffect()
                            viewBinding.notFound.visibility = View.GONE
                        } else {
                            viewBinding.recyclerView.visibility = View.GONE
                            viewBinding.shimmerContainer.visibility = View.GONE
                            viewBinding.notFound.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}