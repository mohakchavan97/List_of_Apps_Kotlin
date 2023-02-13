package com.mohakchavan.applists.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mohakchavan.applists.database.entity.devByteApp.DatabaseVideo
import com.mohakchavan.applists.database.dao.busschedule.ScheduleDao
import com.mohakchavan.applists.database.dao.devByteApp.VideoDao
import com.mohakchavan.applists.database.dao.inventory.ItemDao
import com.mohakchavan.applists.database.entity.busschedule.Schedule
import com.mohakchavan.applists.database.entity.inventory.Item

@Database(
    entities = [
        Schedule::class,
        Item::class,
        DatabaseVideo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    abstract fun itemDao(): ItemDao

    abstract fun videoDao(): VideoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "listofapps"
            )
//                .fallbackToDestructiveMigration() // Will destroy the data and rebuild the database in case of migration
                .createFromAsset("database/bus_schedule.db")
                .also {
                    Log.e("AppDatabase", "buildDatabase: ")
                }
                .build()

    }

}