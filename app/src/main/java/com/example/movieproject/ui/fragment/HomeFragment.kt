package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentHomeBinding
import com.example.movieproject.databinding.FragmentSearchBinding
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

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.getMovieList(requireContext())
        binding.swipeContainer.setOnRefreshListener {
            viewModel.getMovieList(requireContext())
            swipeContainer.isRefreshing = false
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.movies.collect{
                viewModelAdapter?.submitList(it.value)
                if (!it.isLoading){
                    stopShimmerEffect(binding)
                }else{
                    startShimmerEffect(binding)
                }
            }
        }
        viewModelAdapter = MovieListAdapter(){movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = HomeFragmentDirections
                .actionHomeFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }
        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        return binding.root
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