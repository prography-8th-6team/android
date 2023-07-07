package com.example.moiz.presentation.billing.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
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
            viewModel.getBillingDetail(args.billingId, "Bearer $it")
        }

        ivBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            tvEdit.setOnClickListener {
                findNavController().navigate(
                    R.id.action_detailBillingFragment_to_editBillingFragment,
                    bundleOf("billingId" to data.id)
                )
            }

            when (data.category) {
                "food" -> ivCategory.setImageResource(R.drawable.ic_category_food)
                "transportation" -> ivCategory.setImageResource(R.drawable.ic_category_transportation)
                "hotel" -> ivCategory.setImageResource(R.drawable.ic_category_hotel)
                "market" -> ivCategory.setImageResource(R.drawable.ic_category_market)
                "shopping" -> ivCategory.setImageResource(R.drawable.ic_category_shopping)
                else -> ivCategory.setImageResource(R.drawable.ic_category_other)
            }
            tvBillingTitle.text = data.title
            val currencySymbol = Currency.getInstance(data.total_amount_currency).symbol
            tvBillingPrice.text = "$currencySymbol ${data.total_amount}"
            tvBillingDate.text = data.paid_date

            when (data.images?.size) {
                0 -> {
                    ivEmptyImg.visibility = View.VISIBLE
                }

                1 -> {
                    ivBillingImg.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(data.images[0].image)
                        .into(ivBillingImg)
                    ivBillingImg.setOnClickListener { _ ->
                        clickImage(data.images[0].image!!)
                    }
                }

                2 -> {
                    llBillingImg.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(data.images[0].image!!)
                        .into(ivBillingImg1)
                    Glide.with(requireContext()).load(data.images[1].image!!)
                        .into(ivBillingImg2)
                    ivBillingImg1.setOnClickListener { _ ->
                        clickImage(data.images[0].image!!)
                    }
                    ivBillingImg2.setOnClickListener { _ ->
                        clickImage(data.images[1].image!!)
                    }
                }
            }

            llBillingMembers.apply {
                llBillingMembers.removeAllViews()
                data.participants?.forEach { participant ->
                    val binding =
                        ItemBillingDetailMemberBinding.inflate(
                            LayoutInflater.from(context),
                            this,
                            false
                        )
                    binding.tvName.text = participant.user?.nickname
                    binding.tvAmount.text = "$currencySymbol ${participant.total_amount}"
                    if (participant.user?.nickname == data.paid_by) {
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