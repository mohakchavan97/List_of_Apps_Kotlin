/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mohakchavan.applists.busschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohakchavan.applists.BaseApplication
import com.mohakchavan.applists.databinding.FullScheduleFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FullScheduleFragment : Fragment() {

    private val vm: BusScheduleViewModel by activityViewModels {
        BusScheduleViewModelFactory(
            (activity?.application as BaseApplication).databaseHelper
        )
    }

    private var _binding: FullScheduleFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FullScheduleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = BusStopAdapter {
            view.findNavController().navigate(
                FullScheduleFragmentDirections.actionFullScheduleFragmentToStopScheduleFragment(
                    it.stopName
                )
            )
        }
        recyclerView.adapter = adapter
        _binding?.progress?.visibility = View.VISIBLE
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            Thread.sleep(1000)
            vm.fullSchedule().collect{
                lifecycle.coroutineScope.launch(Dispatchers.Main) {
                    _binding?.progress?.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
