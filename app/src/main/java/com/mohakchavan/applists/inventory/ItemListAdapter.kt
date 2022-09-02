package com.mohakchavan.applists.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohakchavan.applists.database.entity.inventory.Item
import com.mohakchavan.applists.databinding.ItemListItemBinding

class ItemListAdapter(private val onClicked: (Item) -> Unit) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClicked(item)
            }
        }
    }

    class ItemViewHolder(private val binding: ItemListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            item.let {
                binding.itemName.text = it.itemName
                binding.itemPrice.text = it.getFormattedPrice()
                binding.itemQuantity.text = it.quantityInStock.toString()
            }
        }

    }
}