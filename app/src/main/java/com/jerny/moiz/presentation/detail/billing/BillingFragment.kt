package com.jerny.moiz.presentation.detail.billing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.BillingDto
import com.jerny.moiz.databinding.FragmentBillingBinding
import com.jerny.moiz.presentation.util.showOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BillingFragment(private val travelId: Int) : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private lateinit var adapter: BillingAdapter
    private val viewModel: BillingViewModel by viewModels()

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
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getTravelDetail(
                travelId, "Bearer $it"
            )
        }

        adapter = BillingAdapter(::itemOnClick)
        rvAccounts.layoutManager = LinearLayoutManager(context)
        rvAccounts.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) { data ->
            rvAccounts.showOrGone(!data.billings.isNullOrEmpty())
            tvEmpty.showOrGone(data.billings.isNullOrEmpty())
            if (!data.billings.isNullOrEmpty()) {
                adapter.submitListEx(data.billings.sortedBy { it.paid_date })
            }
        }
    }

    private fun itemOnClick(data: BillingDto) {
        findNavController().navigate(
            R.id.action_billingFragment_to_detailBillingFragment, bundleOf("billingId" to data.id)
        )
    }

}