package com.example.moiz.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.network.dto.BillingDto
import com.example.moiz.databinding.FragmentBillingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private lateinit var adapter: BillingAdapter
    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        adapter = BillingAdapter(::itemOnClick)
        viewModel.list.observe(viewLifecycleOwner) { data ->
            //data.billings.let { adapter.submitList(data.billings) }
        }

        val list = mutableListOf<BillingDto>()
        repeat(10) {
            list.add(
                BillingDto(
                    id = it,
                    travel = 20,
                    title = "10000",
                    category = "KRW",
                    paid_by = "2021-09-01",
                    paid_date = "가계부 메모",
                    total_amount = "12012",
                    total_amount_currency = "KRW",
                    captured_amount = "10000",
                    participants = listOf("참여자1", "참여자2")
                )
            )
        }

        adapter.submitList(list)
        rvAccounts.layoutManager = LinearLayoutManager(context)
        rvAccounts.adapter = adapter
    }

    private fun itemOnClick(data: BillingDto) {
        // 가계부 상세 이동
    }

}