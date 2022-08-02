package com.mohakchavan.applists.database

import com.mohakchavan.applists.database.dao.busschedule.ScheduleDao
import com.mohakchavan.applists.database.dao.inventory.ItemDao

interface DatabaseHelperImpl : ScheduleDao, ItemDao {
}