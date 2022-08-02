package com.mohakchavan.applists.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohakchavan.applists.R
import com.mohakchavan.applists.affirmations.model.AffirmationModel

class AffirmationItemAdapter(private val context: Context) : RecyclerView.Adapter<AffirmationItemAdapter.ItemViewHolder>() {

    private val listOfAffirmations: MutableList<AffirmationModel> = mutableListOf()

    constructor(context: Context, data: List<AffirmationModel>) : this(context) {
        this.listOfAffirmations.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.affirmation_single_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentData = listOfAffirmations[position]
        holder.textView.text = context.getString(currentData.stringResId)
        holder.imageView.setImageResource(currentData.imageResId)
    }

    override fun getItemCount(): Int {
        return listOfAffirmations.size
    }

    fun addData(data: List<AffirmationModel>) {
        this.listOfAffirmations.addAll(data)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.affirmation_text)
        val imageView: ImageView = itemView.findViewById(R.id.affirmation_image)
    }
}

