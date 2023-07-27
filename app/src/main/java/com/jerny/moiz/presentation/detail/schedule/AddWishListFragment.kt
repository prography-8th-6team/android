package com.jerny.moiz.presentation.detail.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jerny.moiz.databinding.FragmentAddWishListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWishListFragment : Fragment() {

    private lateinit var binding: FragmentAddWishListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddWishListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}