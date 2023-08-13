package com.jerny.moiz.presentation.detail.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.FragmentJourneyBinding
import com.jerny.moiz.presentation.detail.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleFragment(
    private val id: Int,
    private val startDate: String,
    private val endDate: String,
) : Fragment() {
    private lateinit var binding: FragmentJourneyBinding

    private val fragmentList = arrayListOf<Fragment>()
    private val tabTitles = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragmentList()
        binding.vpViewpagerSchedule.post {
            binding.vpViewpagerSchedule.currentItem = 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFragmentList() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)
        var cur = start
        var idx = 0

        // 시작 날짜부터 종료 날짜까지 tab layout item 으로 추가
        while (cur.compareTo(end) != 0) {
            fragmentList.add(idx, ScheduleItemFragment(id, cur.toString()))
            tabTitles.add(idx, "${cur.monthValue}.${cur.dayOfMonth}")
            cur = cur.plusDays(1)
            idx += 1
        }

        val viewPagerAdapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.vpViewpagerSchedule.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter

//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    binding.vpViewpagerSchedule.setCurrentItem(position, false)
//                }
//            })
        }

        TabLayoutMediator(binding.tlScheduleDays, binding.vpViewpagerSchedule) { tab, position ->
            tab.text = tabTitles[position]
            binding.vpViewpagerSchedule.setCurrentItem(tab.position, true)
        }.attach()
    }
}