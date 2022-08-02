package com.mohakchavan.applists.cupcake

import com.mohakchavan.applists.cupcake.model.OrderViewModel
import androidx.arch.core.internal.DefaultTaskExecutor.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ViewModelTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun quan_12_cup(){
        val viewModel = OrderViewModel()
        viewModel.quantity.observeForever{}
        viewModel.setQuantity(12)
        assertEquals(12,viewModel.quantity.value)
    }

}