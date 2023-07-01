package com.example.moiz.presentation.billing.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.FragmentDetailBillingBinding
import com.example.moiz.databinding.ItemBillingDetailMemberBinding
import com.example.moiz.databinding.ItemTravelMemberBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Currency

@AndroidEntryPoint
class BillingDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBillingBinding
    private val viewModel: BillingDetailViewModel by viewModels()
    private val args: BillingDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingDetail(27/*args.billingId*/, "Bearer $it")
        }

        viewModel.data.observe(viewLifecycleOwner) {
            ivBack.setOnClickListener { requireActivity().onBackPressed() }
            when (it.category) {
                "food" -> ivCategory.setImageResource(R.drawable.ic_category_food)
                "transportation" -> ivCategory.setImageResource(R.drawable.ic_category_transportation)
                "hotel" -> ivCategory.setImageResource(R.drawable.ic_category_hotel)
                "market" -> ivCategory.setImageResource(R.drawable.ic_category_market)
                "shopping" -> ivCategory.setImageResource(R.drawable.ic_category_shopping)
                else -> ivCategory.setImageResource(R.drawable.ic_category_other)
            }
            tvBillingTitle.text = it.title
            val currencySymbol = Currency.getInstance(it.total_amount_currency).symbol
            tvBillingPrice.text = "$currencySymbol ${it.total_amount}"
            tvBillingDate.text = it.paid_date

            llBillingMembers.apply {
                it.participants?.forEach { participant ->
                    val binding =
                        ItemBillingDetailMemberBinding.inflate(
                            LayoutInflater.from(context),
                            this,
                            false
                        )
                    binding.tvName.text = participant
                    if (participant == it.paid_by) {
                        binding.tvPaidBy.visibility = View.VISIBLE
                    } else {
                        binding.tvPaidBy.visibility = View.GONE
                    }
                    addView(binding.root)
                }
            }
        }
    }
}