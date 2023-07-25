package com.jerny.moiz.presentation.createTravelList

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
import androidx.navigation.fragment.findNavController
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.data.network.dto.TravelCreateDto
import com.jerny.moiz.databinding.CreateTravelListFragmentBinding
import com.jerny.moiz.domain.model.Currency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class CreateTravelListFragment : Fragment() {
    private lateinit var binding: CreateTravelListFragmentBinding
    private var currencyList = ArrayList<Currency>()
    val viewModel by viewModels<CreateTravelListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = CreateTravelListFragmentBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        initSpinner()
        setDatePickerDialog()
        setRadioGroup()

        binding.btnCreate.setOnClickListener { postTravel() }
        viewModel.isEnabled.observe(viewLifecycleOwner) {
            binding.btnCreate.isEnabled = it
            if (it) {
                binding.btnCreate.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.color_f55c5c)

            } else {
                binding.btnCreate.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.color_ebeaea)
            }
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
        binding.spnCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val currency = binding.spnCurrency.getItemAtPosition(p2) as Currency
                viewModel.setCurrency(currency.currencyText)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setDatePickerDialog() {
        binding.dpStartDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog()
            datePickerFragment.setOnOkClickListener { year, month, day ->
                viewModel.setStartDate("$year.$month.$day")
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        binding.dpEndDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog(viewModel.startDate.value?.replace(".", "-"))
            datePickerFragment.setOnOkClickListener { year, month, day ->
                viewModel.setEndDate("$year.$month.$day")
            }
            datePickerFragment.show(childFragmentManager, null)
        }
    }

    private fun setRadioGroup() {
        binding.rgColor.setOnCheckedChangeListener { _, id ->
            setAllColorUnselected()
            binding.viewLine.visibility = View.VISIBLE

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
                            requireContext(), R.color.color_f9b7a4))
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
                            requireContext(), R.color.color_d8f4f1))
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
                            requireContext(), R.color.color_f8f2c3))
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
                            requireContext(), R.color.color_a4e8c0))
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
                            requireContext(), R.color.color_abe8ff))
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
                            requireContext(), R.color.color_f4f4f4))
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
                    requireContext(), R.color.color_f9b7a4))
        }

        binding.btnD8f4f1.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_d8f4f1))
        }

        binding.btnF8f2c3.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_f8f2c3))
        }

        binding.btnA4e8c0.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_a4e8c0))
        }

        binding.btnAbe8ff.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_abe8ff))
        }

        binding.btnF4f4f4.apply {
            alpha = 0.5F
            setBackgroundResource(R.drawable.bg_circle)
            backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(), R.color.color_f4f4f4))
        }
    }

    private fun postTravel() {
        val travelInfo = TravelCreateDto(
            title = viewModel.title.value,
            start_date = viewModel.startDate.value?.replace(".", "-"),
            end_date = viewModel.endDate.value?.replace(".", "-"),
            color = viewModel.color.value,
            description = viewModel.memo.value,
            currency = viewModel.currency.value)

        UserDataStore.getUserToken(requireContext())
            .asLiveData()
            .observe(viewLifecycleOwner) { token ->
                viewModel.postTravel(travelInfo, "Bearer $token")
            }

        viewModel.response.observe(viewLifecycleOwner) {
            // 성공 시 홈화면으로 이동
            if (it.results != null) {
                findNavController().navigateUp()
            }
        }
    }
}