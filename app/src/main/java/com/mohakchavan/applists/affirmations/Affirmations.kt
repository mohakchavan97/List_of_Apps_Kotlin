package com.mohakchavan.applists.affirmations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohakchavan.applists.R
import com.mohakchavan.applists.affirmations.adapter.AffirmationItemAdapter
import com.mohakchavan.applists.affirmations.data.DataSource
import com.mohakchavan.applists.databinding.ActivityAffirmationsBinding

class Affirmations : AppCompatActivity() {

    lateinit var binding: ActivityAffirmationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAffirmationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.affirmations)

        val list = DataSource().loadAffirmations()
        binding.recyclerView.layoutManager = LinearLayoutManager(this@Affirmations, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = AffirmationItemAdapter(this@Affirmations, list)
    }
}