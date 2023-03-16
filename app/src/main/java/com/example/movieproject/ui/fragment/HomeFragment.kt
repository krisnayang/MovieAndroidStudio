package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.delay

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: MovieViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, MovieViewModel.Factory(activity.application))[MovieViewModel::class.java]
    }
    private var viewModelAdapter: MovieListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
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

        startShimmerEffect(binding)
        viewModel.getMovieList(requireContext())
        viewModel.movies.observe(viewLifecycleOwner){
            viewModelAdapter?.submitList(it)
            if (it.isNotEmpty()){
                stopShimmerEffect(binding)
            }else{
                startShimmerEffect(binding)
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
        binding.swipeContainer.setOnRefreshListener {
            context?.let { viewModel.getMovieList(it) }
            swipeContainer.isRefreshing = false
        }

        return binding.root
    }

    private fun getCurrentActivity(): MainActivity?{
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect(binding: FragmentHomeBinding){
        binding.shimmerContainer.startShimmer()
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