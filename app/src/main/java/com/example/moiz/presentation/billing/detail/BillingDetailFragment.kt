package com.example.moiz.presentation.billing.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.FragmentDetailBillingBinding
import com.example.moiz.databinding.ItemBillingDetailMemberBinding
import com.example.moiz.databinding.ItemTravelMemberBinding
import com.example.moiz.presentation.CustomDialog
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
            ivBack.setOnClickListener { findNavController().popBackStack() }
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

            when (it.images?.size) {
                0 -> {
                    ivEmptyImg.visibility = View.VISIBLE
                }

                1 -> {
                    ivBillingImg.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(it.images[0])
                        .into(ivBillingImg)
                    ivBillingImg.setOnClickListener { _ ->
                        clickImage(it.images[0])
                    }
                }

                2 -> {
                    llBillingImg.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(it.images[0])
                        .into(ivBillingImg1)
                    Glide.with(requireContext()).load(it.images[1])
                        .into(ivBillingImg2)
                    ivBillingImg1.setOnClickListener { _ ->
                        clickImage(it.images[0])
                    }
                    ivBillingImg2.setOnClickListener { _ ->
                        clickImage(it.images[1])
                    }
                }
            }

            llBillingMembers.apply {
                it.participants?.forEach { participant ->
                    val binding =
                        ItemBillingDetailMemberBinding.inflate(
                            LayoutInflater.from(context),
                            this,
                            false
                        )
                    binding.tvName.text = participant.user?.nickname
                    binding.tvAmount.text = "$currencySymbol ${participant.total_amount}"
                    if (participant.user?.nickname == it.paid_by) {
                        binding.tvPaidBy.visibility = View.VISIBLE
                    } else {
                        binding.tvPaidBy.visibility = View.GONE
                    }
                    addView(binding.root)
                }
            }
        }
    }

    private fun clickImage(imgUrl: String) {
        val dialog = ImageCustomDialog(imgUrl)
        dialog.isCancelable = true
        dialog.show(requireActivity().supportFragmentManager, "img_detail")
    }
}