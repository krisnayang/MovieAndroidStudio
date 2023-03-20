package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import com.example.movieproject.databinding.FragmentSearchBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.viewmodel.DetailViewModel
import com.example.movieproject.ui.viewmodel.MovieViewModel
import com.example.movieproject.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    val viewModel by viewModels<SearchViewModel>()
    private var viewModelAdapter: MovieListAdapter? = null

    override fun onStart() {
        super.onStart()
        getCurrentActivity()?.getBottomNav()?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.etSearchMovie.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(title: Editable?) {
                lifecycleScope.launch {
                    binding.notFound.visibility = View.GONE
                    startShimmerEffect(binding)
                    delay(3000)
                    viewModel.searchMovies(title.toString(), requireContext())
                    submitData(viewModel, binding)

                    listItem()

                    binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = viewModelAdapter
                    }
                }
            }
        })
        return binding.root
    }

    private fun getCurrentActivity(): MainActivity?{
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect(binding: FragmentSearchBinding){
        binding.shimmerContainer.visibility = View.VISIBLE
        binding.shimmerContainer.startShimmer()
        binding.recyclerView.visibility = View.GONE
        binding.notFound.visibility = View.GONE
    }

    private fun stopShimmerEffect(binding: FragmentSearchBinding){
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerContainer.stopShimmer()
            binding.shimmerContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.notFound.visibility = View.GONE
        },1000)
    }

    private fun submitData(viewModel: SearchViewModel, binding: FragmentSearchBinding){
        lifecycleScope.launchWhenStarted {
            viewModel.movies.collect{
                viewModelAdapter?.submitList(it?.value)
                if (it?.value?.isNotEmpty() == true) {
                    stopShimmerEffect(binding)
                    binding.notFound.visibility = View.GONE
                }else if (it?.value?.isEmpty() == true) {
                    binding.recyclerView.visibility = View.GONE
                    binding.shimmerContainer.visibility = View.GONE
                    binding.notFound.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun listItem(){
        viewModelAdapter = MovieListAdapter(){movie, view ->
            getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
            val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
            val action = SearchFragmentDirections
                .actionSearchFragmentToDetailMovieFragment(movie.id)
            findNavController().navigate(action, extra)
        }
    }
}