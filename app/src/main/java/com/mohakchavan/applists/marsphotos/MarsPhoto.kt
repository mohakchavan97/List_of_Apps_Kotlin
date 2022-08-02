package com.mohakchavan.applists.marsphotos

import com.google.gson.annotations.SerializedName

data class MarsPhoto(
    @SerializedName("id")
    val id: String,
    @SerializedName("img_src")
    val img_src: String
)
