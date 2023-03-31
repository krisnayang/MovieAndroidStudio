package com.example.movieproject.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieproject.R
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MoviesFavourite
import com.example.movieproject.databinding.FragmentDetailMovieBinding
import com.example.movieproject.ui.adapter.CastListAdapter
import com.example.movieproject.ui.state.Error
import com.example.movieproject.ui.state.Loading
import com.example.movieproject.ui.state.Success
import com.example.movieproject.ui.viewmodel.DetailViewModel
import com.example.movieproject.ui.wrapper.GlideWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {
    private val navigationArgs: DetailMovieFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel>()

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!
    private var castAdapter: CastListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.slide_bottom)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id

        binding.icBackButton.setOnClickListener {
            view.findNavController().navigateUp()
        }

        setupUi()
        viewModel.getFavouriteMovie(id)
//        connectivityObserver.observe().onEach {
//            viewModel.getFullCast(it, id)
//            viewModel.getMovieDetail(it, id)
//        }.launchIn(lifecycleScope)
        viewModel.getFullCast(id)
        viewModel.getMovieDetail(id)
    }

    private fun setupUi() {
        castAdapter = CastListAdapter()

        setupObserver(viewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }
    }

    private fun setupObserver(viewModel: DetailViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.fullCast.collectLatest { state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> binding.internetConn.visibility = View.VISIBLE
                            is Success<*> -> {
                                setFullCast(state.value as List<FullCastEntity>)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movieDetail.collectLatest { state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> binding.internetConn.visibility = View.VISIBLE
                            is Success<*> -> {
                                setMovieDetail(state.value as MovieDetailEntity?)
                            }
                            else -> internetDisconnect()
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.favouriteMovie.collectLatest { state ->
                        when (state) {
                            is Error -> errorFound()
                            is Loading -> binding.internetConn.visibility = View.VISIBLE
                            is Success<*> -> {
                                binding.favourite.isChecked = !(state.value as MoviesFavourite?)?.id.isNullOrEmpty()
                            }
                            else -> internetDisconnect()
                        }
                    }
                }
            }
        }
    }

    private fun bindMovie(movie: MovieDetailEntity) {
        binding.apply {
            GlideWrapper().addImage(requireContext(), movieImage, movie.image)
            GlideWrapper().addImage(requireContext(), movieIcon, movie.image)
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieRating.text = movie.imDbRating.toString()
            movieRatingBar.rating = ((movie.imDbRating?.toFloat() ?: 0) as Float) / 2
            movieRatingCount.text = resources.getString(R.string.total_vote, movie.imDbRatingVotes.toString())
            movieGenre.text = movie.genres
            movieDuration.text = resources.getString(R.string.time_mins, movie.runtimeMins.toString())
            movieDirector.text = movie.directors
            plotDesc.text = movie.plot

            internetConn.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
            errorFound.visibility = View.GONE

            favourite.setOnCheckedChangeListener { it, _ ->
                if( it.isChecked){
                    viewModel.insertFavourite(
                        movie.id,
                        movie.title,
                        movie.image,
                    )
                }else{
                    viewModel.removeFavourite(MoviesFavourite(movie.id, movie.title, movie.image))
                }
            }
        }
    }

    private fun internetConnect(fullCast: List<FullCastEntity>){
        castAdapter?.submitList(fullCast)
        binding.internetConn.visibility = View.VISIBLE
        binding.noInternet.visibility = View.GONE
        binding.errorFound.visibility = View.GONE
    }

    private fun internetDisconnect(){
        binding.internetConn.visibility = View.GONE
        binding.noInternet.visibility = View.VISIBLE
        binding.errorFound.visibility = View.GONE
    }

    private fun errorFound(){
        binding.internetConn.visibility = View.GONE
        binding.noInternet.visibility = View.GONE
        binding.errorFound.visibility = View.VISIBLE
    }

    private fun setFullCast(fullCast: List<FullCastEntity>) {
        if (fullCast.isEmpty()) {
            internetDisconnect()
        } else {
            internetConnect(fullCast)
        }
    }

    private fun setMovieDetail(movieDetail: MovieDetailEntity?) {
        if (movieDetail?.id?.isNotEmpty() == true) {
            bindMovie(movieDetail)
        }
    }
}