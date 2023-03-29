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
import com.example.movieproject.data.local.model.MovieLocal
import com.example.movieproject.databinding.ListItemMovieBinding

class MovieListAdapter(
    private val clickListener: (MovieLocal, View) -> Unit
): ListAdapter<MovieLocal, MovieListAdapter.MovieViewHolder>(DiffCallback){
    private lateinit var context: Context
    private val viewBinding: ListItemMovieBinding
        get() = _viewBinding!!

    private var _viewBinding: ListItemMovieBinding? = null

    companion object DiffCallback: DiffUtil.ItemCallback<MovieLocal>() {
        override fun areItemsTheSame(oldItem: MovieLocal, newItem: MovieLocal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieLocal, newItem: MovieLocal): Boolean {
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
        Glide.with(context)
            .load(movie.image)
            .placeholder(R.drawable.ic_image)
            .into(holder.viewDataBinding.movieIcon)
        holder.bind(movie)
    }

    class MovieViewHolder(val viewDataBinding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(movieLocal: MovieLocal){
            viewDataBinding.movieName.text = movieLocal.title
        }
    }
}