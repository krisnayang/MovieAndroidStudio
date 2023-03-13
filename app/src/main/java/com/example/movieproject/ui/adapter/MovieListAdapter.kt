package com.example.movieproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.databinding.ListItemMovieBinding

class MovieListAdapter(
    private val clickListener: (Movie, View) -> Unit
): ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback){
    private lateinit var context: Context

    companion object DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val withDataBinding: ListItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MovieViewHolder.LAYOUT,
            parent,
            false)
        context = parent.context
        return MovieViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.itemView.setOnClickListener{
            clickListener(movie, it)
        }
        Glide.with(context).load(movie.image).into(holder.viewDataBinding.movieIcon)
        holder.bind(movie)
    }

    class MovieViewHolder(val viewDataBinding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_movie
        }
        fun bind(movie: Movie){
            viewDataBinding.movie = movie
            if(movie.imDbRating.isEmpty()) viewDataBinding.movieRatingBar.rating = 0.0F
            else viewDataBinding.movieRatingBar.rating = movie.imDbRating.toFloat()/2
            viewDataBinding.movieYear.text = movie.year.toString()
            viewDataBinding.executePendingBindings()
        }
    }
}