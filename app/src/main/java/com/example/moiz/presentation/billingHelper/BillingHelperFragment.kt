package com.example.moiz.presentation.billingHelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.BillingHelperFragmentBinding
import com.example.moiz.databinding.ItemBillingBalanceBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint class BillingHelperFragment : Fragment() {
    private lateinit var binding: BillingHelperFragmentBinding
    private val viewModel: BillingHelperViewModel by viewModels()
    private lateinit var adapter: BalancePercentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BillingHelperFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBillingsHelper()
        initView()
    }

    private fun getBillingsHelper() {
        // travel Id 수정
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingsHelper(30, "Bearer $it")
        }
    }

    private fun initView() {
        adapter = BalancePercentAdapter()
        binding.rvBalancePercent.adapter = adapter
        binding.rvBalancePercent.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        viewModel.balancePercent.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        viewModel.balances.observe(viewLifecycleOwner) {
            binding.llBalance.apply {
                it?.forEach { balance ->
                    val binding =
                        ItemBillingBalanceBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.tvUser.text = balance.user?.nickname

                    // TODO
                    // 화폐 단위 표기
                    val decimalformat = DecimalFormat("#,###")
                    binding.tvAmount.text = "${decimalformat.format(balance.amount)}"
                    binding.tvPaidBy.text = "${balance.paid_by?.nickname}"
                    addView(binding.root)
                }
            }
        }
    }
}