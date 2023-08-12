package com.jerny.moiz.presentation.detail.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.FragmentScheduleItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleItemFragment(private val travel_pk: Int, private val date: String) : Fragment() {
    private lateinit var binding: FragmentScheduleItemBinding
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var adapter: ScheduleAdapter
    private val list = MutableLiveData<ArrayList<ScheduleDto>>(arrayListOf())

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
//        setList()
        initViews()

        adapter = ScheduleAdapter(requireContext(), object : ScheduleAdapter.OnClickListener {
            override fun delete(id: Int) {
                deleteSchedule(id)
            }

        })
        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.adapter = adapter

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

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() = with(binding) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getScheduleList("Bearer $it", travel_pk.toString(), "confirmed", date = date)
        }

        viewModel.scheduleList.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data)
        }
    }

    private fun deleteSchedule(id: Int) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.deleteSchedule("Bearer $it", travel_pk.toString(), id.toString())
        }
    }

//    private fun setList() {
//        list.value = arrayListOf(
//            ScheduleDto(
//                travel = 34,
//                images = listOf(),
//                id = 1,
//                title = "타임스퀘어",
//                description = "브로드 웨이 옆",
//                type = "confirmed",
//                category = "sights",
//                date = "2023-08-10",
//                start_at = "13:00",
//                end_at = "15:30",
//                created = "2023-08-05",
//                updated = null),
//
//            ScheduleDto(
//                travel = 34,
//                images = listOf(),
//                id = 2,
//                title = "센트럴파크로 이동",
//                description = "Metrocard 구입",
//                type = 2,
//                category = "transportation",
//                date = "2023-08-10",
//                start_at = "15:30",
//                end_at = "16:00",
//                created = "2023-08-05",
//                updated = null),
//
//            ScheduleDto(
//                travel = 34,
//                images = listOf(),
//                id = 3,
//                title = "센트럴파크",
//                description = "자전거타기",
//                type = 2,
//                category = "sights",
//                date = "2023-08-10",
//                start_at = "16:00",
//                end_at = null,
//                created = "2023-08-05",
//                updated = null),
//
//            ScheduleDto(
//                travel = 34,
//                images = listOf(),
//                id = 4,
//                title = "Essa Bagle!",
//                description = "연어+크림치즈 무조건",
//                type = 2,
//                category = "food",
//                date = "2023-08-10",
//                start_at = "16:30",
//                end_at = "17:00",
//                created = "2023-08-05",
//                updated = null),
//        )
//    }
}