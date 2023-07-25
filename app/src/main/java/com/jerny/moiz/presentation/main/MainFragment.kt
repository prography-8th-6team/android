package com.jerny.moiz.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jerny.moiz.BuildConfig
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.MainFragmentBinding
import com.jerny.moiz.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: TravelAdapter
    val viewModel by viewModels<MainViewModel>()
    val privacyUrl = "https://sites.google.com/view/jernylist/%ED%99%88"

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
        adapter = TravelAdapter { goToDetail(it) }

        with(binding) {
            rvTravelList.adapter = adapter
            rvTravelList.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
            btnDrawer.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
            imgClose.setOnClickListener { drawerLayout.closeDrawer(GravityCompat.START) }
            tvVersion.text = "현재버전 ${BuildConfig.VERSION_NAME}"
            tvPrivacy.setOnClickListener { goToPrivacyPage() }
        }

        getTravelList()
        getUserProfile()
        viewModel.list.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.nickName.observe(viewLifecycleOwner) {
            binding.tvTitle.text = "${it}님의 여행 리스트"
        }
    }

    private fun goToPrivacyPage() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
        startActivity(intent)
    }

    private fun getTravelList() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getTravelList("Bearer $it")

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

    private fun getUserProfile() {
        UserDataStore.getUserToken(requireContext())
            .asLiveData()
            .observe(viewLifecycleOwner) { token ->
                UserDataStore.getUserId(requireContext())
                    .asLiveData()
                    .observe(viewLifecycleOwner) { id ->
                        if (token != "" && id != "") {
                            viewModel.getUserProfile("Bearer $token", id.toInt())
                        }
                    }
            }
    }

    private fun goToCreateTravelList() {
        findNavController().navigate(R.id.action_mainFragment_to_createTravelListFragment)
    }

    private fun goToDetail(travelId: Int) {
        findNavController().navigate(
            R.id.action_mainFragment_to_detailFragment, bundleOf("travelId" to travelId))
    }
}