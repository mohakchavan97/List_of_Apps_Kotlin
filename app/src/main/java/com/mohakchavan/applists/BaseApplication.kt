package com.mohakchavan.applists

import android.app.Application
import com.mohakchavan.applists.database.AppDatabase
import com.mohakchavan.applists.database.DatabaseHelper

class BaseApplication : Application() {

    private val appDatabase: AppDatabase by lazy { AppDatabase(this) }
    val databaseHelper:DatabaseHelper by lazy {
        DatabaseHelper(appDatabase)
    }
}