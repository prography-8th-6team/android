package com.example.moiz.presentation.detail.journey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moiz.databinding.FragmentJourneyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JourneyFragment : Fragment() {

    private lateinit var binding: FragmentJourneyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentJourneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}