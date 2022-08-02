package com.mohakchavan.applists.tipcalculator

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.mohakchavan.applists.R
import com.mohakchavan.applists.databinding.TipCalculatorBinding
import java.text.NumberFormat
import kotlin.math.ceil

class TipCalculator : AppCompatActivity() {

    private lateinit var binding: TipCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TipCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.tipCalculator)

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }
        binding.costOfServiceEditText.setOnKeyListener { view, keycode, _ ->
            handleKeyEvent(view, keycode)
        }
    }

    private fun handleKeyEvent(view: View, keycode: Int): Boolean {
        if (keycode == KeyEvent.KEYCODE_ENTER) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

    private fun calculateTip() {
        val cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()
        val selTipPerc = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
        var tip = if (cost != null) cost * selTipPerc else 0.0
        if (binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }
        binding.tipResult.text = getString(R.string.tip_amount, NumberFormat.getCurrencyInstance().format(tip))
    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}