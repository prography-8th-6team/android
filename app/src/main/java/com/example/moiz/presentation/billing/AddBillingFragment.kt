package com.example.moiz.presentation.billing

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.moiz.R
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.SettlementsDto
import com.example.moiz.databinding.FragmentAddBillingBinding
import com.example.moiz.domain.usecase.PostBillingsUseCase
import com.example.moiz.presentation.detail.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddBillingFragment : Fragment() {

    private lateinit var binding: FragmentAddBillingBinding
    private val viewModel: BillingViewModel by viewModels()
    val temp = PostBillingDto(
        "",
        0,
        "",
        "USD",
        listOf(SettlementsDto(2, 12000))
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddBillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBillingMembers(
            1,
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozLCJleHBpcmVkIjoiMjAyMy0wNi0xOSAyMzoxNToxOSIsImlhdCI6MTY4NTk3NDUxOS4zMTM2MX0.XpoyqvlBN9WBpUjBoP5mtLdK3p5GPF16OdkTL8bTEik"
        )

        initViews()
    }

    private fun initViews() = with(binding) {
        tvAuto.setOnClickListener {
            tvAuto.setTextColor(resources.getColor(R.color.color_555555))
            tvInput.setTextColor(resources.getColor(R.color.color_EBEAEA))
        }

        tvInput.setOnClickListener {
            tvAuto.setTextColor(resources.getColor(R.color.color_EBEAEA))
            tvInput.setTextColor(resources.getColor(R.color.color_555555))
        }

        tvCurrency.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_billing_currency, null)

            val popupWindow =
                PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(binding.tvCurrency, -50, 20)

            popupView.findViewById<LinearLayout>(R.id.ll_usd).setOnClickListener {
                tvCurrency.text = "USD"
                temp.currency = "USD"
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_eur).setOnClickListener {
                tvCurrency.text = "EUR"
                temp.currency = "EUR"
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_krw).setOnClickListener {
                tvCurrency.text = "KRW"
                temp.currency = "KRW"
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_jpy).setOnClickListener {
                tvCurrency.text = "JPY"
                temp.currency = "JPY"
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_gbp).setOnClickListener {
                tvCurrency.text = "GBP"
                temp.currency = "GBP"
                popupWindow.dismiss()
            }
        }

        ivCategory.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_billing_category, null)

            val popupWindow =
                PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivCategory, -132, 20)

            popupView.findViewById<LinearLayout>(R.id.ll_shopping).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_shopping)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_market).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_market)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_food).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_food)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_hotel).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_hotel)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_transportation).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_transportation)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_other).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_other)
                popupWindow.dismiss()
            }

        }

        ivImg.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_billing_camera, null)

            val popupWindow =
                PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.showAsDropDown(ivImg, 0, 20)

            popupView.findViewById<TextView>(R.id.tv_camera).setOnClickListener {
                popupWindow.dismiss()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)
            }

            popupView.findViewById<TextView>(R.id.tv_gallery).setOnClickListener {
                popupWindow.dismiss()
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(intent, 2)
            }
        }

        tvPickerDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, day ->
                    run {
                        tvPickerDate.text =
                            year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                        temp.paid_date = tvPickerDate.text.toString()
                    }
                }, year, month, day)
            }?.show()
        }

        viewModel.members.observe(viewLifecycleOwner) {
            it?.let {
                val membersAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    it.map { it.name }
                )
                spMembers.adapter = membersAdapter
                spMembers.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            temp.paid_by = it[position].id!!
                        }
                    }

                it.forEach { data ->
                    val view = LayoutInflater.from(context).inflate(R.layout.item_paid_member, null)
                    val tvName = view.findViewById<TextView>(R.id.tv_member)
                    tvName.text = data.name

                    llPaidForMembers.addView(view)
                }
            }
        }

        tvAddBilling.setOnClickListener {
            temp.title = etName.text.toString()
            if (temp.title.isNotEmpty() && temp.paid_by != 0 && temp.paid_date.isNotEmpty() && temp.currency.isNotEmpty() && temp.settlements.isNotEmpty()) {
                viewModel.postBillings(
                    1,
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozLCJleHBpcmVkIjoiMjAyMy0wNi0xOSAyMzoxNToxOSIsImlhdCI6MTY4NTk3NDUxOS4zMTM2MX0.XpoyqvlBN9WBpUjBoP5mtLdK3p5GPF16OdkTL8bTEik",
                    temp
                )
            } else {
                Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}