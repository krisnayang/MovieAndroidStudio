package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentDetailMovieBinding
import com.example.movieproject.databinding.FragmentHomeBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.viewmodel.DetailViewModel
import com.example.movieproject.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var viewModelAdapter: MovieListAdapter? = null

    private val viewBinding: FragmentHomeBinding
        get() = _viewBinding!!

    private var _viewBinding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    val viewModel by viewModels<MovieViewModel>()

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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.movies.collect{
                viewModelAdapter?.submitList(it.value)
                if (!it.isLoading){
                    stopShimmerEffect(viewBinding)
                }else{
                    startShimmerEffect(viewBinding)
                }
            }
        }
    }

    private fun getCurrentActivity(): MainActivity?{
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect(binding: FragmentHomeBinding){
        binding.shimmerContainer.startShimmer()
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun stopShimmerEffect(binding: FragmentHomeBinding){
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerContainer.stopShimmer()
            binding.shimmerContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        },1000)
    }
}