package com.jerny.moiz.presentation.detail.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.FragmentScheduleItemBinding
import com.jerny.moiz.presentation.util.showOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleItemFragment(private val travel_pk: Int, private val date: String) : Fragment() {
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

        adapter = ScheduleAdapter(requireContext(), object : ScheduleAdapter.OnClickListener {
            override fun delete(id: Int) {
                deleteSchedule(id)
                // 삭제 후 새로고침
                getScheduleList()
            }

            override fun onClick(schedule: ScheduleDto) {
                findNavController().navigate(
                    R.id.action_detailFragment_to_scheduleDetailFragment,
                    bundleOf(
                        "travelId" to travel_pk,
                        "scheduleId" to schedule.id,
                        "startDate" to schedule.start_at,
                        "endDate" to schedule.end_at
                    )
                )
            }
        })

        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.adapter = adapter
        getScheduleList()

        viewModel.scheduleList.observe(viewLifecycleOwner) {
            binding.rvSchedule.showOrGone(it.isNotEmpty())
            binding.tvEmptyList.showOrGone(it.isEmpty())
            adapter.submitList(it)
        }

        val swiperHelperCallback = SwipeHelperCallback(adapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }

        ItemTouchHelper(swiperHelperCallback).attachToRecyclerView(binding.rvSchedule)
        // 다른 곳 터치하면 기존 뷰 닫기
        binding.rvSchedule.setOnTouchListener { view, motionEvent ->
            swiperHelperCallback.removePreviousClamp(binding.rvSchedule)
            false
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun getScheduleList() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getScheduleList("Bearer $it", travel_pk.toString(), "confirmed", date = date)
        }
    }

    private fun deleteSchedule(id: Int) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.deleteSchedule("Bearer $it", travel_pk.toString(), id.toString())
        }
    }
}