package com.jerny.moiz.presentation.detail.schedule

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.FragmentJourneyBinding
import com.jerny.moiz.presentation.detail.ViewPagerAdapter
import com.jerny.moiz.presentation.util.gone
import com.jerny.moiz.presentation.util.show
import com.jerny.moiz.presentation.util.showOrGone
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
    private val viewModel: ScheduleViewModel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val fragmentList = arrayListOf<Fragment>()
    private val tabTitles = arrayListOf<String>()
    private var isFlag: Boolean = false
    private var isIdx: Int = 0

    private val wishList = arrayListOf<Fragment>()

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

        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getScheduleList("Bearer $it", id.toString(), "pending")
        }

        binding.ivHide.setOnClickListener {
            if (binding.flWishlist.visibility == View.GONE) binding.flWishlist.show()
            else binding.flWishlist.gone()
        }

        viewModel.scheduleList.observe(viewLifecycleOwner) {
            binding.viewPager.showOrGone(it.isNotEmpty())
            binding.tvEmptyList.showOrGone(it.isEmpty())
            it.chunked(8).forEach { list ->
                wishList.add(
                    WishListItemFragment(
                        list,
                                                  false,
                                                  { setInit(it) },
                                                  { deleteSchedule(it) }) {
                    findNavController().navigate(
                            R.id.action_detailFragment_to_scheduleDetailFragment,
                            bundleOf(
                            "travelId" to id,
                            "scheduleId" to it,
                            "startDate" to startDate,
                                "endDate" to endDate
                            )
                        )
                })
            }.run {
                viewPagerAdapter = ViewPagerAdapter(wishList, childFragmentManager, lifecycle)

                binding.viewPager.apply {
                    adapter = viewPagerAdapter

                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            isIdx = position
                        }
                    })
                }
            }
        }

        binding.vpViewpagerSchedule.post {
            binding.vpViewpagerSchedule.currentItem = 0
        }
    }

    private fun setInit(isFlag: Boolean) {
        wishList.clear()
        viewModel.scheduleList.value?.chunked(8)?.forEach { list ->
            wishList.add(
                WishListItemFragment(
                    list,
                                              isFlag,
                                              { setInit(it) },
                                              { deleteSchedule(it) }) {
                findNavController().navigate(
                    R.id.action_detailFragment_to_scheduleDetailFragment, bundleOf(
                        "travelId" to id,
                        "scheduleId" to it,
                        "startDate" to startDate,
                            "endDate" to endDate
                        )
                    )
            })
        }.run {
            viewPagerAdapter = ViewPagerAdapter(wishList, childFragmentManager, lifecycle)

            binding.viewPager.apply {
                adapter = viewPagerAdapter
                setCurrentItem(isIdx, false)

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                    }
                })
            }
        }
    }

    private fun deleteSchedule(scheduleId: Int) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.deleteSchedule("Bearer $it", id.toString(), scheduleId.toString())
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFragmentList() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = LocalDate.parse(startDate, formatter)
        val end = LocalDate.parse(endDate, formatter)
        var cur = start
        var idx = 0

        if (start == end) {
            fragmentList.add(0, ScheduleItemFragment(id, start.toString()))
            tabTitles.add(0, "${start.monthValue}.${start.dayOfMonth}")
        } else {
            // 시작 날짜부터 종료 날짜까지 tab layout item 으로 추가
            while (cur.compareTo(end) != 0) {
                fragmentList.add(idx, ScheduleItemFragment(id, cur.toString()))
                tabTitles.add(idx, "${cur.monthValue}.${cur.dayOfMonth}")
                cur = cur.plusDays(1)
                idx += 1
            }
            fragmentList.add(idx, ScheduleItemFragment(id, end.toString()))
            tabTitles.add(idx, "${end.monthValue}.${end.dayOfMonth}")
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