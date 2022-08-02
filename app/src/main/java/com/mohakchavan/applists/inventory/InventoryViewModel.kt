package com.mohakchavan.applists.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mohakchavan.applists.database.DatabaseHelper
import com.mohakchavan.applists.database.entity.inventory.Item
import kotlinx.coroutines.launch

class InventoryViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    fun addItem(name: String, price: String, count: String) {
        insertItem(getNewItem(name, price, count))
    }

    fun isEntryValid(name: String, price: String, count: String): Boolean {
        return !(name.isBlank() || price.isBlank() || count.isBlank())
    }

    private fun insertItem(item: Item) {
        viewModelScope.launch {
            databaseHelper.insertItem(item)
        }
    }

    private fun getNewItem(name: String, price: String, count: String): Item {
        return Item(
            itemName = name,
            itemPrice = price.toDouble(),
            quantityInStock = count.toInt()
        )
    }
}

class InventoryViewModelFactory(private val databaseHelper: DatabaseHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(databaseHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}