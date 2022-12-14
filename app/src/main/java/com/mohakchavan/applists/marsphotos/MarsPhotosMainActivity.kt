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

package com.mohakchavan.applists.marsphotos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohakchavan.applists.R

/**
 * MarsPhotosMainActivity sets the content view activity_main, a fragment container that contains
 * overviewFragment.
 */
class MarsPhotosMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_marsphotos)

        supportActionBar?.title = getString(R.string.mars_photos)


    }
}