/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mohakchavan.applists.inventory

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mohakchavan.applists.BaseApplication
import com.mohakchavan.applists.R
import com.mohakchavan.applists.database.entity.inventory.Item
import com.mohakchavan.applists.databinding.FragmentAddItemBinding

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as BaseApplication).databaseHelper
        )
    }
    lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveItem(id).observe(viewLifecycleOwner) {
                it?.let { item ->
                    this@AddItemFragment.item = item
                    bind(item)
                }
            }
        } else {
            _binding?.saveAction?.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            _binding?.itemName?.text.toString(),
            _binding?.itemPrice?.text.toString(),
            _binding?.itemCount?.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addItem(
                _binding?.itemName?.text.toString(),
                _binding?.itemPrice?.text.toString(),
                _binding?.itemCount?.text.toString()
            )
            findNavController().navigate(R.id.action_addItemFragment_to_itemListFragment)
        }
    }

    private fun bind(item: Item) {
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText("%.2f".format(item.itemPrice), TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)

            saveAction.setOnClickListener {
                updateItem()
            }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            viewModel.updateItem(
                item.id,
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
            )
            findNavController().navigate(AddItemFragmentDirections.actionAddItemFragmentToItemListFragment())
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}
