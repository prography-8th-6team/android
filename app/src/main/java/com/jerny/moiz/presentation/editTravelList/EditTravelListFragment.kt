package com.jerny.moiz.presentation.editTravelList

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.databinding.EditTravelListFragmentBinding
import com.jerny.moiz.domain.model.Currency
import com.jerny.moiz.presentation.createTravelList.DatePickerDialog
import com.jerny.moiz.presentation.createTravelList.SpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditTravelListFragment : Fragment() {
    private lateinit var binding: EditTravelListFragmentBinding
    private var currencyList = ArrayList<Currency>()
    val viewModel by viewModels<EditTravelListViewModel>()
    private val args: EditTravelListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = EditTravelListFragmentBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        getTravelDetail()
        initSpinner()
        initDatePickerDialog()
        initRadioGroup()
        initEditButton()
        setDate()
        binding.btnEdit.setOnClickListener { putTravel(args.travelId) }
    }

    private fun setDate() {
        viewModel.startDate.observe(viewLifecycleOwner) {
            binding.dpStartDate.text = it.replace("-", ".")
        }

        viewModel.endDate.observe(viewLifecycleOwner) {
            binding.dpEndDate.text = it.replace("-", ".")
        }
    }

    private fun initEditButton() {
        viewModel.isEnabled.observe(viewLifecycleOwner) {
            binding.btnEdit.isEnabled = it
            if (it) {
                binding.btnEdit.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.color_f55c5c)

            } else {
                binding.btnEdit.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.color_ebeaea)
            }
        }
    }

    private fun getTravelDetail() {
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getTravelDetail(args.travelId, "Bearer $it")
            initValue()
        }
    }

    private fun initValue() {
        viewModel.travelDetail.observe(viewLifecycleOwner) { travelDetail ->
            travelDetail.title?.let { viewModel.setTitle(it) }
            travelDetail.start_date?.let { viewModel.setStartDate(it) }
            travelDetail.end_date?.let { viewModel.setEndDate(it) }
            travelDetail.color?.let { viewModel.setColor(it) }
            travelDetail.currency?.let { viewModel.setCurrency(it) }
            travelDetail.description?.let { viewModel.setMemo(it) }
        }
    }

    private fun initSpinner() {
        currencyList = arrayListOf(
            Currency("$", "USD"),
            Currency("€", "EUR"),
            Currency("₩", "KRW"),
            Currency("¥", "JPY"),
            Currency("£", "GBP"),
        )

        binding.spnCurrency.adapter =
            SpinnerAdapter(requireContext(), R.layout.spinner_currency_item_view, currencyList)
        viewModel.currency.observe(viewLifecycleOwner) {
            val position = when (it) {
                "USD" -> 0
                "EUR" -> 1
                "KRW" -> 2
                "JPY" -> 3
                else -> 4
            }
            binding.spnCurrency.setSelection(position)
        }

        binding.spnCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val currency = binding.spnCurrency.getItemAtPosition(p2) as Currency
                viewModel.setCurrency(currency.currencyText)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun initDatePickerDialog() {
        binding.dpStartDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog(viewModel.startDate.value)
            datePickerFragment.setOnOkClickListener { year, month, day ->
                viewModel.setStartDate("$year-$month-$day")
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        binding.dpEndDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog(viewModel.endDate.value)
            datePickerFragment.setOnOkClickListener { year, month, day ->
                viewModel.setEndDate("$year-$month-$day")
            }
            datePickerFragment.show(childFragmentManager, null)
        }
    }

    private fun initRadioGroup() {
        viewModel.color.observe(viewLifecycleOwner) {
            when (it) {
                "f9b7a4" -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f9b7a4
                        )
                    )
                    binding.rgColor.check(R.id.btn_f9b7a4)
                }

                "d8f4f1" -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_d8f4f1
                        )
                    )
                    binding.rgColor.check(R.id.btn_d8f4f1)
                }

                "f8f2c3" -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f8f2c3
                        )
                    )
                    binding.rgColor.check(R.id.btn_f8f2c3)
                }

                "a4e8c0" -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_a4e8c0
                        )
                    )
                    binding.rgColor.check(R.id.btn_a4e8c0)
                }

                "abe8ff" -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_abe8ff
                        )
                    )
                    binding.rgColor.check(R.id.btn_abe8ff)
                }

                else -> {
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f4f4f4
                        )
                    )
                    binding.rgColor.check(R.id.btn_f4f4f4)
                }
            }
        }

        binding.rgColor.setOnCheckedChangeListener { _, id ->
            setAllColorUnselected()
            when (id) {
                R.id.btn_f9b7a4 -> {
                    binding.btnF9b7a4.apply {
                        // 투명도
                        alpha = 1F
                        // backgoundTint 해제
                        backgroundTintList = null
                        // selected svg 로 변경
                        setBackgroundResource(R.drawable.bg_f9b7a4_selected)
                    }
                    // 색상코드 저장
                    viewModel.setColor("f9b7a4")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f9b7a4
                        )
                    )
                }

                R.id.btn_d8f4f1 -> {
                    binding.btnD8f4f1.apply {
                        alpha = 1F
                        backgroundTintList = null
                        setBackgroundResource(R.drawable.bg_d8f4f1_selected)
                    }
                    viewModel.setColor("d8f4f1")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_d8f4f1
                        )
                    )
                }

                R.id.btn_f8f2c3 -> {
                    binding.btnF8f2c3.apply {
                        alpha = 1F
                        backgroundTintList = null
                        setBackgroundResource(R.drawable.bg_f8f2c3_selected)
                    }
                    viewModel.setColor("f8f2c3")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f8f2c3
                        )
                    )
                }

                R.id.btn_a4e8c0 -> {
                    binding.btnA4e8c0.apply {
                        alpha = 1F
                        backgroundTintList = null
                        setBackgroundResource(R.drawable.bg_a4e8c0_selected)
                    }
                    viewModel.setColor("a4e8c0")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_a4e8c0
                        )
                    )
                }

                R.id.btn_abe8ff -> {
                    binding.btnAbe8ff.apply {
                        alpha = 1F
                        backgroundTintList = null
                        setBackgroundResource(R.drawable.bg_abe8ff_selected)
                    }
                    viewModel.setColor("abe8ff")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_abe8ff
                        )
                    )
                }

                else -> {
                    binding.btnF4f4f4.apply {
                        alpha = 1F
                        backgroundTintList = null
                        setBackgroundResource(R.drawable.bg_f4f4f4_selected)
                    }
                    viewModel.setColor("f4f4f4")
                    binding.viewLine.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.color_f4f4f4
                        )
                    )
                }
            }
        }
    }

    private fun setAllColorUnselected() {
        binding.btnF9b7a4.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_f9b7a4
                )
            )
        }

        binding.btnD8f4f1.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_d8f4f1
                )
            )
        }

        binding.btnF8f2c3.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_f8f2c3
                )
            )
        }

        binding.btnA4e8c0.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_a4e8c0
                )
            )
        }

        binding.btnAbe8ff.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_abe8ff
                )
            )
        }

        binding.btnF4f4f4.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_f4f4f4
                )
            )
        }
    }

    private fun putTravel(id: Int) {
        val travelInfo = TravelCreateDto(
            title = viewModel.title.value,
            start_date = viewModel.startDate.value,
            end_date = viewModel.endDate.value,
            color = viewModel.color.value,
            description = viewModel.memo.value,
            currency = viewModel.currency.value
        )

        lifecycleScope.launch {
            UserDataStore.getUserToken(requireContext()).collect { token ->
                viewModel.putTravel("Bearer $token", travelInfo, id)
            }
        }

        viewModel.response.observe(viewLifecycleOwner) {
            // 성공 시 여행 상세 페이지로 이동
            if (it != null) {
                findNavController().navigateUp()
            }
        }
    }
}