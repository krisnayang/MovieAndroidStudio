package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.databinding.FragmentDetailMovieBinding
import com.example.movieproject.ui.adapter.CastListAdapter
import com.example.movieproject.ui.adapter.MovieListAdapter
import com.example.movieproject.ui.viewmodel.DetailViewModel

class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {
    private val navigationArgs: DetailMovieFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, DetailViewModel.Factory(activity.application))[DetailViewModel::class.java]
    }
    private lateinit var movie: MovieDetailEntity

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!
    private var castAdapter: CastListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id

        viewModel.insertFullCast(id)

        viewModel.fullCast.observe(this.viewLifecycleOwner){
            castAdapter?.submitList(it)
        }
        castAdapter = CastListAdapter()
        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }
        viewModel.movieDetail.observe(this.viewLifecycleOwner){
            movie = it
            bindMovie()
        }
    }

    private fun bindMovie() {
        binding.apply {
            context?.let {
                Glide.with(it).load(movie.image).into(movieImage)
                Glide.with(it).load(movie.image).into(movieIcon)
            }
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieRating.text = movie.imDbRating
            if(movie.imDbRating.isEmpty()) movieRatingBar.rating = 0.0F
            else movieRatingBar.rating = movie.imDbRating.toFloat()/2
            movieRatingCount.text = movie.imDbRatingVotes.toString() + " Vote"
            movieGenre.text = movie.genres
            movieDuration.text = movie.runtimeMins.toString() + " Mins"
            movieDirector.text = movie.directors
            plotDesc.text = movie.plot
        }
    }
}