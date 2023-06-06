package com.example.moiz.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moiz.R
import com.example.moiz.databinding.MainFragmentBinding
import com.example.moiz.domain.model.Journey

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: JourneyAdapter
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

        initRecyclerview()
        adapter = JourneyAdapter()
        binding.btnCreate.setOnClickListener { goToCreateJourneyList() }
        binding.rvJourneyList.apply {
            adapter = adapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        }
        viewModel.journeyList.observe(viewLifecycleOwner) {
            adapter.submitList(it as MutableList<Journey>)
        }
    }

    private fun initRecyclerview() {
        viewModel.journeyList.postValue(
            listOf(
                Journey(
                    "뉴욕 리스트", "2023.05.11", "2023.05.17", arrayListOf("이형준", "이준영")),
                Journey(
                    "형준이와 준영이가 함께하는 즐거운 미국 여행",
                    "2023.04.11",
                    "2023.04.17",
                    arrayListOf("이형준", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "도쿄 리스트", "2023.02.11", "2023.02.17", arrayListOf("이형준", "오동훈", "김예은")),
                Journey(
                    "파리 리스트",
                    "2022.12.11",
                    "2022.12.24",
                    arrayListOf("이형준", "임지원", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "상하이 리스트", "2022.04.23", "2022.04.28", arrayListOf("오동훈", "조준호", "이준영", "김예은")),
                Journey(
                    "베이징 리스트", "2022.03.13", "2022.03.15", arrayListOf("오동훈", "이형준")),
                Journey(
                    "로마 리스트", "2022.02.05", "2022.02.12", arrayListOf("김예은")),
                Journey(
                    "하와이 리스트", "2021.11.23", "2021.11.29", arrayListOf("김예은", "임지원")),
                Journey(
                    "뉴욕 리스트", "2023.05.11", "2023.05.17", arrayListOf("이형준", "이준영")),
                Journey(
                    "형준이와 준영이가 함께하는 즐거운 미국 여행",
                    "2023.04.11",
                    "2023.04.17",
                    arrayListOf("이형준", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "도쿄 리스트", "2023.02.11", "2023.02.17", arrayListOf("이형준", "오동훈", "김예은")),
                Journey(
                    "파리 리스트",
                    "2022.12.11",
                    "2022.12.24",
                    arrayListOf("이형준", "임지원", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "상하이 리스트", "2022.04.23", "2022.04.28", arrayListOf("오동훈", "조준호", "이준영", "김예은")),
                Journey(
                    "베이징 리스트", "2022.03.13", "2022.03.15", arrayListOf("오동훈", "이형준")),
                Journey(
                    "로마 리스트", "2022.02.05", "2022.02.12", arrayListOf("김예은")),
                Journey(
                    "하와이 리스트", "2021.11.23", "2021.11.29", arrayListOf("김예은", "임지원")),
                Journey(
                    "뉴욕 리스트", "2023.05.11", "2023.05.17", arrayListOf("이형준", "이준영")),
                Journey(
                    "형준이와 준영이가 함께하는 즐거운 미국 여행",
                    "2023.04.11",
                    "2023.04.17",
                    arrayListOf("이형준", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "도쿄 리스트", "2023.02.11", "2023.02.17", arrayListOf("이형준", "오동훈", "김예은")),
                Journey(
                    "파리 리스트",
                    "2022.12.11",
                    "2022.12.24",
                    arrayListOf("이형준", "임지원", "이준영", "조준호", "김예은", "오동훈")),
                Journey(
                    "상하이 리스트", "2022.04.23", "2022.04.28", arrayListOf("오동훈", "조준호", "이준영", "김예은")),
                Journey(
                    "베이징 리스트", "2022.03.13", "2022.03.15", arrayListOf("오동훈", "이형준")),
                Journey(
                    "로마 리스트", "2022.02.05", "2022.02.12", arrayListOf("김예은")),
            ))
    }

    private fun goToCreateJourneyList() {
        findNavController().navigate(R.id.action_mainFragment_to_createJourneyListFragment)
    }
}