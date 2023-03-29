package com.example.movieproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.databinding.ListItemCastBinding
import com.example.movieproject.ui.wrapper.GlideWrapper

class CastListAdapter : ListAdapter<FullCastEntity, CastListAdapter.CastViewHolder>(DiffCallback) {
    private lateinit var context: Context
    private val viewBinding: ListItemCastBinding
        get() = _viewBinding!!

    private var _viewBinding: ListItemCastBinding? = null

    companion object DiffCallback : DiffUtil.ItemCallback<FullCastEntity>() {
        override fun areItemsTheSame(oldItem: FullCastEntity, newItem: FullCastEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FullCastEntity, newItem: FullCastEntity): Boolean {
            return oldItem == newItem
        }
    }

    class CastViewHolder(val viewDataBinding: ListItemCastBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(fullCast: FullCastEntity) {
            viewDataBinding.actorName.text = fullCast.name
            viewDataBinding.charName.text = fullCast.asCharacter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        _viewBinding =
            ListItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return CastViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = getItem(position)
        GlideWrapper().addImage(context, holder.viewDataBinding.castImage, cast.image)
        holder.bind(cast)
    }
}