package com.jerny.moiz.presentation.detail.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.FragmentScheduleItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleItemFragment(private val id: Int, private val date: String) : Fragment() {
    private lateinit var binding: FragmentScheduleItemBinding
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var adapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentScheduleItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDate.text = "${date}"
        initViews()
    }

    private fun initViews() = with(binding) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getScheduleList("Bearer $it", id.toString())
        }
        adapter = ScheduleAdapter()
        rvSchedule.layoutManager = LinearLayoutManager(context)
        rvSchedule.adapter = adapter

        viewModel.scheduleList.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data)
        }
    }
}