package com.example.movieproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.databinding.ListItemCastBinding
import com.example.movieproject.databinding.ListItemMovieBinding

class MovieListAdapter(
    private val clickListener: (Movie, View) -> Unit
): ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback){
    private lateinit var context: Context
    private val viewBinding: ListItemMovieBinding
        get() = _viewBinding!!

    private var _viewBinding: ListItemMovieBinding? = null

    companion object DiffCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        _viewBinding = ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MovieViewHolder(viewBinding)
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
        fun bind(movie: Movie){
            viewDataBinding.movieName.text = movie.title
        }
    }
}