package com.mohakchavan.applists.cupcake.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
private const val TAG = "OrderViewModel"

class OrderViewModel : ViewModel() {

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private var _flavour = MutableLiveData<String>()
    val flavour: LiveData<String> = _flavour

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private var _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickUpOptions()

    init {
        Log.e(TAG, "init: ")
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavour.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun hasNoFlavourSet(): Boolean {
        return _flavour.value.isNullOrEmpty()
    }

    fun resetOrder() {
        Log.e(TAG, "resetOrder: ")
        _quantity.value = 0
        _flavour.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice() {
        Log.e(TAG, "updatePrice: ")
        var calPrice = (_quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if (dateOptions[0] == _date.value) {
            calPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calPrice
    }

    private fun getPickUpOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(cal.time))
            cal.add(Calendar.DATE, 1)
        }
        return options
    }
}