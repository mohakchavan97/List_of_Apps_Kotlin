package com.mohakchavan.applists.inventory

import androidx.lifecycle.*
import com.mohakchavan.applists.database.DatabaseHelper
import com.mohakchavan.applists.database.entity.inventory.Item
import kotlinx.coroutines.launch

class InventoryViewModel(private val databaseHelper: DatabaseHelper) : ViewModel() {

    val allItems: LiveData<List<Item>> = databaseHelper.getAllItems().asLiveData()

    fun addItem(name: String, price: String, count: String) {
        insertItem(getNewItem(name, price, count))
    }

    fun isEntryValid(name: String, price: String, count: String): Boolean {
        return !(name.isBlank() || price.isBlank() || count.isBlank())
    }

    fun retrieveItem(id: Int): LiveData<Item> {
        return databaseHelper.getItem(id).asLiveData()
    }

    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            updateItem(item.copy(quantityInStock = item.quantityInStock.dec()))
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            databaseHelper.deleteItem(item)
        }
    }

    fun updateItem(id: Int, name: String, price: String, count: String){
        updateItem(generateUpdatedItem(id, name, price, count))
    }

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            databaseHelper.updateItem(item)
        }
    }

    private fun generateUpdatedItem(id: Int, name: String, price: String, count: String): Item {
        return Item(
            id = id,
            itemName = name,
            itemPrice = price.toDouble(),
            quantityInStock = count.toInt()
        )
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