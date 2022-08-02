package com.mohakchavan.applists.marsphotos

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.mohakchavan.applists.R
import com.mohakchavan.applists.marsphotos.overview.ApiStatus
import com.mohakchavan.applists.marsphotos.overview.PhotoGridAdapter

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        it.toUri().buildUpon().scheme("https").build()
    }?.let {
        imageView.load(it) {
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MarsPhoto>?) {
    data?.let { list ->
        recyclerView.adapter?.let { adapter ->
            (adapter as PhotoGridAdapter).submitList(list)
        }
    }
}

@BindingAdapter("apiStatus")
fun bindStatus(imageView: ImageView, status: ApiStatus) {
    when (status) {
        ApiStatus.LOADING -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.loading_animation)
        }

        ApiStatus.SUCCESS -> imageView.visibility = View.GONE

        ApiStatus.ERROR -> {
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}