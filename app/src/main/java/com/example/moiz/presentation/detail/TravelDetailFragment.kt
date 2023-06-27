package com.example.moiz.presentation.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.ItemTravelMemberBinding
import com.example.moiz.databinding.TravelDetailFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        initViewPager()
    }

    private fun initViewPager() {
        var viewPagerAdapter = ViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(BillingFragment())
        viewPagerAdapter.addFragment(BillingFragment())
        viewPagerAdapter.addFragment(BillingFragment())

        binding.vpViewpagerMain.apply {
            adapter = viewPagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // viewPager
                    val view =
                        (binding.vpViewpagerMain[0] as RecyclerView).layoutManager?.findViewByPosition(
                            position
                        )
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
                viewModel.postGenerateInviteToken(args.travelId, token)
                    .observe(viewLifecycleOwner) {
                        Timber.d("share token: $it")
                    }
            }
            popupView.findViewById<View>(R.id.tv_edit).setOnClickListener {
                // 여행 수정
            }
            popupView.findViewById<View>(R.id.tv_delete).setOnClickListener {
                // 여행 삭제
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
}