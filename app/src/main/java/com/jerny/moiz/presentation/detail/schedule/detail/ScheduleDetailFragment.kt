package com.jerny.moiz.presentation.detail.schedule.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.FragmentJourneyBinding
import com.jerny.moiz.databinding.FragmentScheduleDetailBinding
import com.jerny.moiz.databinding.TravelDetailFragmentBinding
import com.jerny.moiz.presentation.billing.detail.ImageCustomDialog
import com.jerny.moiz.presentation.detail.DetailViewModel
import com.jerny.moiz.presentation.detail.TravelDetailFragmentArgs
import com.jerny.moiz.presentation.detail.ViewPagerAdapter
import com.jerny.moiz.presentation.util.CustomDialog
import com.jerny.moiz.presentation.util.ScheduleDialog
import com.jerny.moiz.presentation.util.gone
import com.jerny.moiz.presentation.util.show
import com.jerny.moiz.presentation.util.showOrGone
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScheduleDetailFragment : Fragment() {

    private lateinit var binding: FragmentScheduleDetailBinding
    private val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: ScheduleDetailFragmentArgs by navArgs()
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentScheduleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            token = it
            viewModel.getScheduleDetail("Bearer $token", args.travelId.toString(), args.scheduleId)
        }

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tvEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_scheduleDetailFragment_to_editScheduleFragment,
                bundleOf("travelId" to args.travelId, "scheduleId" to args.scheduleId)
            )
        }

        viewModel.scheduleData.observe(viewLifecycleOwner) { data ->
            data?.let {
                setTop(data.type!!)
                tvBillingTitle.text = data.title
                etMemo.text = data.description

                tvSchedule.setOnClickListener {
                    val dialog = ScheduleDialog() { date, start, end ->
                        setTop("confirmed")
                        viewModel.updateParam("confirmed", date, start, end)
                        scheduleGroup.show()
                        dpDate.text = date
                        dpStartDate.text = start
                        dpEndDate.text = end

                        viewModel.putSchedule("Bearer $token", args.travelId, data.id!!)
                    }
                    dialog.isCancelable = false
                    dialog.show(requireActivity().supportFragmentManager, "schedule")
                }

                tvWishList.setOnClickListener {
                    val dialog =
                        CustomDialog("일정에서 위시리스트로 이동시키면 날짜와 시간이 모두 초기화됩니다.", "취소", "위시리스트로 이동") {
                            setTop("pending")
                            viewModel.updateParam("pending")
                            scheduleGroup.gone()

                            viewModel.putSchedule("Bearer $token", args.travelId, data.id!!)
                        }
                    dialog.isCancelable = false
                    dialog.show(requireActivity().supportFragmentManager, "wishList")
                }

                scheduleGroup.showOrGone(data.type == "confirmed")
                if (data.type == "confirmed") {
                    dpDate.text = data.date
                    dpStartDate.text = data.start_at
                    dpEndDate.text = data.end_at
                }

                when (data.category) {
                    "food" -> ivCategory.setImageResource(R.drawable.ic_category_food)
                    "transportation" -> ivCategory.setImageResource(R.drawable.ic_category_transportation)
                    "hotel" -> ivCategory.setImageResource(R.drawable.ic_category_hotel)
                    "market" -> ivCategory.setImageResource(R.drawable.ic_category_market)
                    "shopping" -> ivCategory.setImageResource(R.drawable.ic_category_shopping)
                    else -> ivCategory.setImageResource(R.drawable.ic_category_other)
                }

                when (data.images.size) {
                    0 -> {
                        ivEmptyImg.visibility = View.VISIBLE
                    }

                    1 -> {
                        ivBillingImg.visibility = View.VISIBLE
                        Glide.with(requireContext()).load(data.images[0])
                            .into(ivBillingImg)
                        ivBillingImg.setOnClickListener { _ ->
                            clickImage(data.images[0]!!)
                        }
                    }

                    2 -> {
                        llBillingImg.visibility = View.VISIBLE
                        Glide.with(requireContext()).load(data.images[0])
                            .into(ivBillingImg1)
                        Glide.with(requireContext()).load(data.images[1])
                            .into(ivBillingImg2)
                        ivBillingImg1.setOnClickListener { _ ->
                            clickImage(data.images[0]!!)
                        }
                        ivBillingImg2.setOnClickListener { _ ->
                            clickImage(data.images[1]!!)
                        }
                    }
                }
            }
        }
    }

    private fun clickImage(imgUrl: String) {
        val dialog = ImageCustomDialog(imgUrl)
        dialog.isCancelable = true
        dialog.show(requireActivity().supportFragmentManager, "img_detail")
    }

    private fun setTop(type: String) {
        val isConfirmed = (type == "confirmed")
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        val greyColor = ContextCompat.getColor(requireContext(), R.color.color_EBEAEA)

        binding.tvSchedule.backgroundTintList =
            ColorStateList.valueOf(if (isConfirmed) whiteColor else greyColor)
        binding.tvWishList.backgroundTintList =
            ColorStateList.valueOf(if (isConfirmed) greyColor else whiteColor)
    }

}