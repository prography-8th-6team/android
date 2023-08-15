package com.jerny.moiz.presentation.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.ItemTravelMemberBinding
import com.jerny.moiz.databinding.TravelDetailFragmentBinding
import com.jerny.moiz.presentation.billingHelper.BillingHelperFragment
import com.jerny.moiz.presentation.detail.billing.BillingFragment
import com.jerny.moiz.presentation.detail.schedule.ScheduleFragment
import com.jerny.moiz.presentation.util.CustomDialog
import com.jerny.moiz.presentation.util.showOrHide
import com.jerny.moiz.presentation.util.toCostFormat
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint
class TravelDetailFragment : Fragment() {

    private lateinit var binding: TravelDetailFragmentBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: TravelDetailFragmentArgs by navArgs()
    private var token: String = ""

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
            token = "Bearer $it"
            viewModel.getTravelDetail(
                args.travelId, "Bearer $it"
            )
        }
        initViews()

        viewModel.list.observe(viewLifecycleOwner) {
            if (!it.start_date.isNullOrEmpty() && !it.end_date.isNullOrEmpty()) {
                initViewPager(it.start_date, it.end_date)
            }
        }
    }

    private fun initViewPager(startDate: String, endDate: String) {
        val fragmentList = arrayListOf(
            BillingFragment(args.travelId),
            BillingHelperFragment(args.travelId),
            ScheduleFragment(args.travelId, startDate, endDate)
        )

        val viewPagerAdapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.vpViewpagerMain.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.grBottomNavigation.showOrHide(position == 0)
                    binding.ivAddSchedule.showOrHide(position == 2)
                }
            })
        }

        TabLayoutMediator(binding.tlNavigationView, binding.vpViewpagerMain) { tab, position ->
            when (position) {
                0 -> tab.text = "가계부"
                1 -> tab.text = "계산도우미"
                2 -> tab.text = "일정"
            }
        }.attach()
    }

    private fun initViews() = with(binding) {
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivAddSchedule.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_add_schedule, null)

            val popupWindow =
                PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivAddSchedule, 0, -600)

            popupView.findViewById<TextView>(R.id.tv_wishList).setOnClickListener {
                popupWindow.dismiss()
                findNavController().navigate(R.id.action_detailFragment_to_addWishListFragment, bundleOf("travelId" to args.travelId))
            }

            popupView.findViewById<TextView>(R.id.tv_schedule).setOnClickListener {
                popupWindow.dismiss()
                findNavController().navigate(
                    R.id.goto_add_schedule, bundleOf("travelId" to args.travelId)
                )
            }
        }

        ivAdditional.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_travel_detail_popup, null)

            val popupWindow = PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivAdditional, -80, 20)

            popupView.findViewById<View>(R.id.tv_share).setOnClickListener {
                /*
                viewModel.postGenerateInviteToken(args.travelId, token)
                    .observe(viewLifecycleOwner) {
                        val inviteLink =
                            "https://jernymoiz.page.link/invite?code=${it.toekn}"

                        FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse(inviteLink))
                            .setDomainUriPrefix("https://jernymoiz.page.link")
                            .setAndroidParameters(
                                AndroidParameters.Builder()
                                    .setMinimumVersion(1)
                                    .build()
                            )
                            .buildShortDynamicLink()
                            .addOnSuccessListener { shortDynamicLink ->
                                val sendIntent = Intent()
                                sendIntent.action = Intent.ACTION_SEND
                                sendIntent.putExtra(
                                    Intent.EXTRA_TEXT,
                                    shortDynamicLink.shortLink.toString()
                                )
                                sendIntent.type = "text/plain"
                                startActivity(sendIntent)
                            }
                    }
                 */
                val dialog = CustomDialog("링크를 복사해 리스트를 공유하세요", "취소", "링크 복사") {
                    viewModel.postGenerateInviteToken(args.travelId, token)
                        .observe(viewLifecycleOwner) {
                            val clipboard =
                                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

                            val temp =
                                "https://jernymoiz.page.link/invite?code=${it.toekn}"
                            val clip = ClipData.newPlainText(
                                "label", temp
                            )
                            clipboard.setPrimaryClip(clip)
                            popupWindow.dismiss()
                            Toast.makeText(requireContext(), "공유 코드가 복사되었습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                dialog.isCancelable = false
                dialog.show(requireActivity().supportFragmentManager, "share")

            }

            popupView.findViewById<View>(R.id.tv_edit).setOnClickListener {
                viewModel.list.value?.id?.let { goToEditTravel(it) }
                popupWindow.dismiss()
            }

            popupView.findViewById<View>(R.id.tv_delete).setOnClickListener {
                val dialog = CustomDialog("리스트를 삭제하시겠습니까?", "취소", "네") {
                    UserDataStore.getUserToken(requireContext())
                        .asLiveData()
                        .observe(viewLifecycleOwner) {
                            viewModel.deleteTravel("Bearer $it", args.travelId)
                        }
                    popupWindow.dismiss()
                    findNavController().navigateUp()
                }
                dialog.isCancelable = false
                dialog.show(requireActivity().supportFragmentManager, "delete")
            }
        }

        nsvTravelDetail.run {
            nsvTravelDetail.run {
                header = tlNavigationView
                temp = llToolbar
            }
        }

        viewModel.list.observe(viewLifecycleOwner) { data ->
            tvTitle.text = data.title
            tvData.text = data.start_date + "~" + data.end_date
            tvMemo.text = data.description

            val currencySymbol = Currency.getInstance(data.currency).symbol
            tvMyCost.text = currencySymbol + " " + data.my_total_billing?.toInt().toCostFormat()
            tvTotalCost.text = currencySymbol + " " + data.total_amount?.toInt().toCostFormat()

            llTravelMember.apply {
                llTravelMember.removeAllViews()
                data.members?.forEach {
                    val binding =
                        ItemTravelMemberBinding.inflate(LayoutInflater.from(context), this, false)
                    binding.tvMemberName.text = it
                    addView(binding.root)
                }
            }
        }

        ivAddAccount.setOnClickListener {
            findNavController().navigate(
                R.id.goto_add_billing, bundleOf("travelId" to args.travelId)
            )
        }
    }

    private fun goToEditTravel(travelId: Int) {
        findNavController().navigate(
            R.id.action_detailFragment_to_editTravelListFragment, bundleOf("travelId" to travelId)
        )
    }
}