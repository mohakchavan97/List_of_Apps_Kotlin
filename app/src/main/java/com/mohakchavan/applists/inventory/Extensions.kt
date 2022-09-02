package com.mohakchavan.applists.inventory

import com.mohakchavan.applists.database.entity.inventory.Item
import java.text.NumberFormat

fun Item.getFormattedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(itemPrice)
}