package com.mohakchavan.applists.busschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohakchavan.applists.database.entity.busschedule.Schedule
import com.mohakchavan.applists.databinding.BusStopItemBinding
import java.text.SimpleDateFormat
import java.util.*

class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) : ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        return BusStopViewHolder(BusStopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClicked(getItem(position))
        }
    }

    class BusStopViewHolder(private var binding: BusStopItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat("HH:mm:ss.SSS")
                .format(Date(schedule.arrivalTime.toLong() * 1000))
        }

    }

}