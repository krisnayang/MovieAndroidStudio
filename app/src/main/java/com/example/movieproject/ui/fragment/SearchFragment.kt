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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.databinding.FragmentSearchBinding
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.viewmodel.MovieViewModel
import com.example.movieproject.ui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.list_item_movie.view.*

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, SearchViewModel.Factory(activity.application))[SearchViewModel::class.java]
    }
    private var viewModelAdapter: MovieListAdapter? = null

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

//        getCurrentActivity()?.getBottomNav()?.visibility = View.VISIBLE
            binding.etSearchMovie.addTextChangedListener(object: TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(title: Editable?) {
                    startShimmerEffect(binding)
                    viewModel.searchMovies(title.toString())
                    viewModel.movies.observe(viewLifecycleOwner){
                        viewModelAdapter?.submitList(it)
                        if (it.isNotEmpty()){
                            stopShimmerEffect(binding)
                        }
                    }

                    viewModelAdapter = MovieListAdapter(){movie, view ->
                        getCurrentActivity()?.getBottomNav()?.visibility = View.GONE
                        val extra = FragmentNavigatorExtras(view.movieIcon to "big_icon")
                        val action = SearchFragmentDirections
                            .actionSearchFragmentToDetailMovieFragment(movie.id)
                        findNavController().navigate(action, extra)
                    }
                    binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = viewModelAdapter
                    }
                }
            })


        return binding.root
    }

    private fun getCurrentActivity(): MainActivity?{
        return (activity as? MainActivity)
    }

    private fun startShimmerEffect(binding: FragmentSearchBinding){
        binding.shimmerContainer.startShimmer()
        binding.recyclerView.visibility = View.GONE
    }

    private fun stopShimmerEffect(binding: FragmentSearchBinding){
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerContainer.stopShimmer()
            binding.shimmerContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        },1000)
    }
    inline fun View.onDebouncedListener (delay: Long = 3000L, crossinline listener: (View)->Unit) {
        var enable = java.lang.Runnable { isEnabled = true }
        setOnClickListener{
            if (isEnabled) {
                isEnabled = false
                postDelayed(enable, delay)
                listener(it)
            }
        }
    }
}