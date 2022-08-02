package com.mohakchavan.applists.busschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohakchavan.applists.database.DatabaseHelper
import com.mohakchavan.applists.database.entity.busschedule.Schedule
import kotlinx.coroutines.flow.Flow

class BusScheduleViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    suspend fun fullSchedule(): Flow<List<Schedule>> =
        databaseHelper.getAllSchedules()

    suspend fun scheduleForStop(name: String): Flow<List<Schedule>> =
        databaseHelper.getAllByStopName(name)
}

class BusScheduleViewModelFactory(
    private val databaseHelper: DatabaseHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusScheduleViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}