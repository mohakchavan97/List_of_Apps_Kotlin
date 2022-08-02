package com.mohakchavan.applists.alarmExample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mohakchavan.applists.databinding.ActivityAlarmBinding
import java.util.*

class AlarmActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context = this@AlarmActivity

        supportActionBar?.title = "Alarm Activity"
        if (binding.aaTcClock.is24HourModeEnabled) {
            binding.aaTcClock.format24Hour="HH:mm:ss"
        }else{
            binding.aaTcClock.format12Hour="hh:mm:ss a"
        }
        binding.aaBtnStart.setOnClickListener {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(context, AlarmReceiver::class.java).let {
                PendingIntent.getBroadcast(context, 0, it, 0)
            }
            /*val cal = Calendar.getInstance()
            cal.timeInMillis = System.currentTimeMillis()
            *//*cal.set(Calendar.HOUR_OF_DAY,13)
            cal.set(Calendar.MINUTE,10)
            cal.set(Calendar.SECOND,0)*//*
            cal.add(Calendar.SECOND,5)

//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 5 * 1000, alarmIntent)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis,"Alarm",
                {

                },null)*/
            setAlarm()
            Log.e("Alarm", "onCreate: Alarm Set Repeating")
        }
    }

    private fun setAlarm() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(Calendar.HOUR_OF_DAY,23)
        cal.set(Calendar.MINUTE,59)
        cal.set(Calendar.SECOND,59)
//        cal.add(Calendar.SECOND, 10)
        /*alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, "Alarm",
            {
                Log.e("Alarm", "setAlarm: Alarm Received.")
                setAlarm()
            }, null
        )*/
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis,AlarmManager.INTERVAL_DAY, alarmIntent)
    }
}