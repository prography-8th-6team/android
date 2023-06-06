package com.example.moiz.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.data.network.dto.BillingDto
import com.example.moiz.databinding.ItemTravelMemberBinding
import com.example.moiz.databinding.TravelDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint
class TravelDetailFragment : Fragment() {

    private lateinit var binding: TravelDetailFragmentBinding
    private lateinit var adapter: BillingAdapter
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = TravelDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTravelDetail(
            1,
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozLCJleHBpcmVkIjoiMjAyMy0wNi0xOSAyMzoxNToxOSIsImlhdCI6MTY4NTk3NDUxOS4zMTM2MX0.XpoyqvlBN9WBpUjBoP5mtLdK3p5GPF16OdkTL8bTEik"
        )
        initViews()
    }

    private fun initViews() = with(binding) {
        viewModel.list.observe(viewLifecycleOwner) { data ->
            tvTitle.text = data.title
            tvData.text = data.start_date + "~" + data.end_date
            tvMemo.text = data.description
            val currencySymbol = Currency.getInstance(data.currency).symbol
            tvMyCost.text = currencySymbol + " " + data.my_total_billing.toString()
            tvTotalCost.text = currencySymbol + " " + data.total_amount.toString()

            adapter = BillingAdapter(::itemOnClick)
            data.billings.let { adapter.submitList(data.billings) }
            rvAccounts.layoutManager = LinearLayoutManager(context)
            rvAccounts.adapter = adapter

            llTravelMember.apply {
                data.members?.forEach {
                    val binding =
                        ItemTravelMemberBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.tvMemberName.text = it
                    addView(binding.root)
                }
            }
        }
    }

    private fun itemOnClick(data: BillingDto) {

    }

}