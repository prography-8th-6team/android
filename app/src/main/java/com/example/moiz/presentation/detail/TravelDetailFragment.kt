package com.example.moiz.presentation.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.ItemTravelMemberBinding
import com.example.moiz.databinding.TravelDetailFragmentBinding
import com.example.moiz.presentation.billingHelper.BillingHelperFragment
import com.example.moiz.presentation.detail.billing.BillingFragment
import com.example.moiz.presentation.detail.schedule.ScheduleFragment
import com.example.moiz.presentation.util.CustomDialog
import com.example.moiz.presentation.util.showOrHide
import com.example.moiz.presentation.util.toCostFormat
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Currency

@AndroidEntryPoint class TravelDetailFragment : Fragment() {

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
                args.travelId, "Bearer $it")
        }
        initViews()
        initViewPager()
    }

    private fun initViewPager() {
        val fragmentList = arrayListOf(
            BillingFragment(args.travelId),
            BillingHelperFragment(args.travelId),
            ScheduleFragment())
        val viewPagerAdapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.vpViewpagerMain.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view =
                        (binding.vpViewpagerMain[0] as RecyclerView).layoutManager?.findViewByPosition(
                            position)
                    view?.post {
                        val wMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                        val hMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)
                        if (binding.vpViewpagerMain.layoutParams.height != view.measuredHeight) {
                            binding.vpViewpagerMain.layoutParams =
                                (binding.vpViewpagerMain.layoutParams).also { lp ->
                                    lp.height = view.measuredHeight
                                }
                        }
                    }

                    binding.grBottomNavigation.showOrHide(position == 0)
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

        ivAdditional.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_travel_detail_popup, null)

            val popupWindow = PopupWindow(
                popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivAdditional, -80, 20)

            popupView.findViewById<View>(R.id.tv_share).setOnClickListener {
                val dialog = CustomDialog("링크를 복사해 리스트를 공유하세요", "취소", "링크 복사") {
                    viewModel.postGenerateInviteToken(args.travelId, token)
                        .observe(viewLifecycleOwner) {
                            val clipboard =
                                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                            val temp = "moiz://deeplink?code=${it.toekn}"
                            val clip = ClipData.newPlainText(
                                "label", temp)
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
                R.id.goto_add_billing, bundleOf("travelId" to args.travelId))
        }
    }

    private fun goToEditTravel(travelId: Int) {
        findNavController().navigate(
            R.id.action_detailFragment_to_editTravelListFragment, bundleOf("travelId" to travelId))
    }
}