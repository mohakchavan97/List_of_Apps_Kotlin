package com.mohakchavan.applists.alarmExample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Alarm", "onReceive: Alarm Received.")
        Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()
    }
}