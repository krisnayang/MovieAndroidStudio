package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentSearchBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
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

        _viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        viewBinding.lifecycleOwner = viewLifecycleOwner
        viewBinding.viewModel = viewModel
        setupUi()
        viewBinding.etSearchMovie.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    viewBinding.notFound.visibility = View.GONE
                    startShimmerEffect(viewBinding)
                    delay(3000)

                    viewModel.searchMovies(s.toString(), requireContext())
                }
            }
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

        setupObserver(viewModel)

        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun getCurrentActivity(): MainActivity? {
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect(binding: FragmentSearchBinding) {
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.shimmerContainer.startShimmer()
        binding.recyclerView.visibility = View.GONE
        binding.notFound.visibility = View.GONE
    }

    private fun stopShimmerEffect(binding: FragmentSearchBinding) {
        lifecycleScope.launch{
            delay(1000)
            binding.shimmerContainer.stopShimmer()
            binding.shimmerContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.notFound.visibility = View.GONE
        }
    }

    private fun setupObserver(viewModel: SearchViewModel) {
        lifecycleScope.launch {
            viewModel.movies.collect {
                viewModelAdapter?.submitList(it?.value)

                if (it?.isLoading == false) {
                    stopShimmerEffect(viewBinding)
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