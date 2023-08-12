package com.jerny.moiz.presentation.detail.schedule.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.FragmentJourneyBinding
import com.jerny.moiz.databinding.FragmentScheduleDetailBinding
import com.jerny.moiz.databinding.TravelDetailFragmentBinding
import com.jerny.moiz.presentation.detail.DetailViewModel
import com.jerny.moiz.presentation.detail.TravelDetailFragmentArgs
import com.jerny.moiz.presentation.detail.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleDetailFragment : Fragment() {

    private lateinit var binding: FragmentScheduleDetailBinding
    private val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: TravelDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            
        }

        viewModel.scheduleData.observe(viewLifecycleOwner) {
            Timber.d("Schedule Data: $it")
        }
    }

}