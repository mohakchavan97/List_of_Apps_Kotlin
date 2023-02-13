package com.mohakchavan.applists

import android.app.Application
import com.mohakchavan.applists.database.AppDatabase
import com.mohakchavan.applists.database.DatabaseHelper
import timber.log.Timber

class BaseApplication : Application() {

    private val appDatabase: AppDatabase by lazy { AppDatabase(this) }
    val databaseHelper:DatabaseHelper by lazy {
        DatabaseHelper(appDatabase)
    }
	
	/**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}