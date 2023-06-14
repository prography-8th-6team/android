package com.example.moiz.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.MainFragmentBinding
import com.example.moiz.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: TravelAdapter
    val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener { goToCreateTravelList() }
        adapter = TravelAdapter()
        binding.rvTravelList.adapter = adapter
        binding.rvTravelList.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        getTravelList()
        viewModel.list.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    private fun getTravelList() {
        lifecycleScope.launch {
            UserDataStore.getUserToken(requireContext()).collect {
                viewModel.getTravelList("Bearer $it")
            }
        }

        viewModel.response.observe(viewLifecycleOwner) {
            if (it.message == "Forbidden") {
                val intent = Intent(
                    requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                it.results?.let { it1 -> viewModel.setTravelList(it1) }
            }
        }
    }

    private fun goToCreateTravelList() {
        findNavController().navigate(R.id.action_mainFragment_to_createTravelListFragment)
    }
}