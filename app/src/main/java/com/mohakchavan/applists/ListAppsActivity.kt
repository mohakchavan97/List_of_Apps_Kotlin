package com.mohakchavan.applists

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.mohakchavan.applists.affirmations.Affirmations
import com.mohakchavan.applists.alarmExample.AlarmActivity
import com.mohakchavan.applists.bluetooth.BluetoothActivity
import com.mohakchavan.applists.busschedule.BusScheduleMainActivity
import com.mohakchavan.applists.cupcake.CupCakeMainActivity
import com.mohakchavan.applists.databinding.ActivityListAppsBinding
import com.mohakchavan.applists.inventory.InventoryMainActivity
import com.mohakchavan.applists.marsphotos.MarsPhotosMainActivity
import com.mohakchavan.applists.sports.SportsMainActivity
import com.mohakchavan.applists.tipcalculator.TipCalculator
import com.mohakchavan.applists.unscramble.UnscrambleMainActivity
import com.mohakchavan.applists.wordsapp.WordsAppMainActivity

class ListAppsActivity : AppCompatActivity() {

    lateinit var binding: ActivityListAppsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listApps = listOf<String>(
            getString(R.string.tipCalculator),
            getString(R.string.affirmations),
            getString(R.string.wordsApp),
            getString(R.string.unscramble),
            getString(R.string.cupcake),
            getString(R.string.sports),
            getString(R.string.mars_photos),
            getString(R.string.bus_schedule),
            getString(R.string.inventory),


            getString(R.string.bluetooth),
            "Alarm Example"
        )

        binding.listOfApps.adapter = ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listApps)

        binding.listOfApps.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> startActivity(Intent(this@ListAppsActivity, TipCalculator::class.java))
                1 -> startActivity(Intent(this@ListAppsActivity, Affirmations::class.java))
                2 -> startActivity(Intent(this@ListAppsActivity, WordsAppMainActivity::class.java))
                3 -> startActivity(Intent(this@ListAppsActivity, UnscrambleMainActivity::class.java))
                4 -> startActivity(Intent(this@ListAppsActivity, CupCakeMainActivity::class.java))
                5 -> startActivity(Intent(this@ListAppsActivity, SportsMainActivity::class.java))
                6 -> startActivity(Intent(this@ListAppsActivity, MarsPhotosMainActivity::class.java))
                7 -> startActivity(Intent(this@ListAppsActivity, BusScheduleMainActivity::class.java))
                8 -> startActivity(Intent(this@ListAppsActivity, InventoryMainActivity::class.java))

                9 -> startActivity(Intent(this@ListAppsActivity, BluetoothActivity::class.java))
                else -> startActivity(Intent(this@ListAppsActivity, AlarmActivity::class.java))
            }
        }
    }
}