package com.mohakchavan.applists.marsphotos.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohakchavan.applists.databinding.GridViewItemBinding
import com.mohakchavan.applists.marsphotos.MarsPhoto

class PhotoGridAdapter : ListAdapter<MarsPhoto, PhotoGridAdapter.PhotoGridViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridViewHolder {
        return PhotoGridViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PhotoGridViewHolder(private var binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: MarsPhoto) {
            binding.photo = photo
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<MarsPhoto>() {
        override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
            return oldItem.img_src == newItem.img_src
        }

    }
}
