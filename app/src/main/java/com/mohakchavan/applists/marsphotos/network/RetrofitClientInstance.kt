package com.mohakchavan.applists.marsphotos.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

object RetrofitClientInstance {
    val client: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}