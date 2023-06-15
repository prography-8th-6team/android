package com.example.moiz.presentation.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.data.network.dto.BillingDto
import com.example.moiz.databinding.ItemTravelMemberBinding
import com.example.moiz.databinding.TravelDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint class TravelDetailFragment : Fragment() {

    private lateinit var binding: TravelDetailFragmentBinding
    private lateinit var adapter: BillingAdapter
    private val viewModel: DetailViewModel by viewModels()
    private val args: TravelDetailFragmentArgs by navArgs()

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

        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getTravelDetail(
                args.travelId, "Bearer $it")
        }

        initViews()
    }

    private fun initViews() = with(binding) {
        ivAdditional.setOnClickListener {
            val inflater =
                view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_travel_detail_popup, null)

            val popupWindow = PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivAdditional, -80, 20)

            popupView.findViewById<View>(R.id.tv_share).setOnClickListener {
                // 여행 공유
            }
            popupView.findViewById<View>(R.id.tv_edit).setOnClickListener {
                viewModel.list.value?.id?.let { goToEditTravel(it) }
                popupWindow.dismiss()
            }
            popupView.findViewById<View>(R.id.tv_delete).setOnClickListener {
                // 여행 삭제
            }
        }

        nsvTravelDetail.run {
            header = llDetailTab
            temp = llToolbar
        }

        ivAddAccount.setOnClickListener {
            findNavController().navigate(R.id.goto_add_billing)
        }

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
        // 가계부 상세 이동
    }

    private fun goToEditTravel(travelId: Int) {
        findNavController().navigate(
            R.id.action_detailFragment_to_editTravelListFragment, bundleOf("travelId" to travelId))
    }

}