package com.example.movieproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.databinding.ListItemCastBinding
import com.example.movieproject.databinding.ListItemMovieBinding

class CastListAdapter(): ListAdapter<FullCastEntity, CastListAdapter.CastViewHolder>(DiffCallback) {
    private lateinit var context: Context

    companion object DiffCallback: DiffUtil.ItemCallback<FullCastEntity>() {
        override fun areItemsTheSame(oldItem: FullCastEntity, newItem: FullCastEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FullCastEntity, newItem: FullCastEntity): Boolean {
            return oldItem == newItem
        }
    }
    class CastViewHolder(val viewDataBinding: ListItemCastBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_cast
        }
        fun bind(fullCast: FullCastEntity){
            viewDataBinding.fullcast = fullCast
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val withDataBinding: ListItemCastBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CastViewHolder.LAYOUT,
            parent,
            false)
        context = parent.context
        return CastViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)
        Glide.with(context).load(cast.image).into(holder.viewDataBinding.castImage)
        holder.bind(cast)
    }
}