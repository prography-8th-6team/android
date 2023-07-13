package com.example.moiz.presentation.billingHelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.BillingHelperFragmentBinding
import com.example.moiz.databinding.ItemBillingBalanceBinding
import com.example.moiz.presentation.util.toCostFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class BillingHelperFragment(private val travelId: Int) : Fragment() {
    private lateinit var binding: BillingHelperFragmentBinding
    private val viewModel: BillingHelperViewModel by viewModels()
    private lateinit var adapter: BalancePercentAdapter
    private lateinit var emptyAdapter: BalancePercentEmptyAdapter

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
        getBillingMembers()
        initView()

        binding.btnComplete.setOnClickListener {
            postCompleteSettlement()
        }

        viewModel.completeResponse.observe(viewLifecycleOwner) {
            if (it == "정산할 금액이 없습니다.") {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getBillingsHelper() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingsHelper(travelId, "Bearer $it")
        }
    }

    private fun getBillingMembers() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingMembers(travelId, "Bearer $it")
        }
    }

    private fun postCompleteSettlement() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.postCompleteSettlement("Bearer $it", travelId)
        }
    }

    private fun initView() {
        viewModel.balancePercent.observe(viewLifecycleOwner) {
            emptyAdapter = BalancePercentEmptyAdapter()
            binding.rvBalancePercentEmpty.adapter = emptyAdapter
            binding.rvBalancePercentEmpty.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            adapter = BalancePercentAdapter()
            binding.rvBalancePercent.adapter = adapter
            binding.rvBalancePercent.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            if (it?.isEmpty() == true) {
                setBillingPercentGone()
                setBillingPercentEmptyVisible()
                setBtnPayDisabled()

                viewModel.members.observe(viewLifecycleOwner) { members ->
                    emptyAdapter.submitList(members)
                }
            } else {
                setBillingPercentVisible()
                setBillingPercentEmptyGone()
                setBtnPayEnabled()
                adapter.submitList(it)
            }
        }

        viewModel.balances.observe(viewLifecycleOwner) {
            binding.llBalance.apply {
                it?.forEach { balance ->
                    val binding =
                        ItemBillingBalanceBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.tvUser.text = balance.user?.nickname

                    binding.tvAmount.text = balance.amount.toCostFormat()
                    binding.tvPaidBy.text = "${balance.paid_by?.nickname}"
                    addView(binding.root)
                }
            }
        }
    }

    private fun setBtnPayEnabled() {
        binding.btnComplete.isEnabled = true
        binding.btnComplete.backgroundTintList = context?.getColorStateList(R.color.color_f55c5c)
    }

    private fun setBtnPayDisabled() {
        binding.btnComplete.isEnabled = false
        binding.btnComplete.backgroundTintList = context?.getColorStateList(R.color.color_ebeaea)
    }

    private fun setBillingPercentVisible() {
        binding.rvBalancePercent.visibility = View.VISIBLE
        binding.llIcons.visibility = View.VISIBLE
        binding.llBalance.visibility = View.VISIBLE
    }

    private fun setBillingPercentGone() {
        binding.rvBalancePercent.visibility = View.GONE
        binding.llIcons.visibility = View.GONE
        binding.llBalance.visibility = View.GONE
    }

    private fun setBillingPercentEmptyVisible() {
        binding.tvBalanceEmpty.visibility = View.VISIBLE
        binding.rvBalancePercentEmpty.visibility = View.VISIBLE
    }

    private fun setBillingPercentEmptyGone() {
        binding.rvBalancePercentEmpty.visibility = View.GONE
        binding.tvBalanceEmpty.visibility = View.GONE
    }
}