package com.mohakchavan.applists.database

import com.mohakchavan.applists.database.entity.busschedule.Schedule
import com.mohakchavan.applists.database.entity.inventory.Item
import kotlinx.coroutines.flow.Flow

class DatabaseHelper(private val appDatabase: AppDatabase) : DatabaseHelperImpl {

    override fun getAllSchedules(): Flow<List<Schedule>> {
        return appDatabase.scheduleDao().getAllSchedules()
    }

    override fun getAllByStopName(stopName: String): Flow<List<Schedule>> =
        appDatabase.scheduleDao().getAllByStopName(stopName)

    override suspend fun insertItem(item: Item) {
        appDatabase.itemDao().insertItem(item)
    }

    override fun getItem(id: Int): Flow<Item> {
        return appDatabase.itemDao().getItem(id)
    }

    override fun getAllItems(): Flow<List<Item>> {
        return appDatabase.itemDao().getAllItems()
    }

    override suspend fun updateItem(item: Item) {
        appDatabase.itemDao().updateItem(item)
    }

    override suspend fun deleteItem(item: Item) {
        appDatabase.itemDao().deleteItem(item)
    }
}