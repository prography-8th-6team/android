package com.jerny.moiz.presentation.detail.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jerny.moiz.databinding.FragmentScheduleItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleItemFragment(private val date: String) : Fragment() {
    private lateinit var binding: FragmentScheduleItemBinding

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
    }
}