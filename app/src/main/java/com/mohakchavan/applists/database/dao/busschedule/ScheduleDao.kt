package com.mohakchavan.applists.database.dao.busschedule

import androidx.room.Dao
import androidx.room.Query
import com.mohakchavan.applists.database.entity.busschedule.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM Schedule ORDER BY arrival_time ASC")
     fun getAllSchedules(): Flow<List<Schedule>>

    @Query("SELECT * FROM Schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
     fun getAllByStopName(stopName: String): Flow<List<Schedule>>
}