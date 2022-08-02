/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mohakchavan.applists.marsphotos.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohakchavan.applists.marsphotos.MarsPhoto
import com.mohakchavan.applists.marsphotos.network.RetrofitClientInstance
import kotlinx.coroutines.launch


enum class ApiStatus {
    LOADING,
    SUCCESS,
    ERROR
}


/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus> = _status

    private val _photos = MutableLiveData<List<MarsPhoto>>()
    val photos: LiveData<List<MarsPhoto>> = _photos

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    private fun getMarsPhotos() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                _photos.value = RetrofitClientInstance.client.getPhotos()
                _status.value = ApiStatus.SUCCESS
                Log.e("OrderViewModel", "getMarsPhotos: Size:${_photos.value?.size}")
            } catch (e: Exception) {
                _photos.value = listOf()
                _status.value = ApiStatus.ERROR
            }
        }

    }
}
