package com.mohakchavan.applists.marsphotos.network

import com.mohakchavan.applists.marsphotos.MarsPhoto
import retrofit2.http.GET

interface MarsApiService {

//    @GET("photos")
    @GET("realestate")
    suspend fun getPhotos(): List<MarsPhoto>

}
