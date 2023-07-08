package com.example.moiz.presentation.billing.add

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.FragmentAddBillingBinding
import com.example.moiz.domain.model.Currency
import com.example.moiz.domain.model.InputCostEntity
import com.example.moiz.presentation.billing.BillingViewModel
import com.example.moiz.presentation.createTravelList.SpinnerAdapter
import com.example.moiz.presentation.util.FileResult
import com.example.moiz.presentation.util.PermissionUtil
import com.example.moiz.presentation.util.getFileInfo
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar


@AndroidEntryPoint
class AddBillingFragment : Fragment() {

    private lateinit var binding: FragmentAddBillingBinding
    private val viewModel: BillingViewModel by viewModels()
    private var currencyList = ArrayList<Currency>()

    var tempImgFile = arrayListOf<FileResult>()
    var camUri: Uri? = null
    private val args: AddBillingFragmentArgs by navArgs()
    private lateinit var adapter: AddBillingAdapter

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
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingMembers(
                args.travelId, "Bearer $it"
            )
        }

        initViews()
    }

    private fun itemOnClick(data: InputCostEntity) {
        viewModel.updateCost(data)
        binding.etPrice.setText(viewModel.totalAmount.toString())
    }

    private fun itemOnChange(data: InputCostEntity) {
        viewModel.changeCost(data)
        binding.etPrice.setText(viewModel.totalAmount.toString())
    }

    private fun initViews() = with(binding) {
        adapter = AddBillingAdapter(::itemOnClick, ::itemOnChange)
        rvPaidForMembers.layoutManager = LinearLayoutManager(context)
        rvPaidForMembers.adapter = adapter

        viewModel.temp.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        viewModel.isValidated.observe(viewLifecycleOwner) {
            tvAddBilling.isEnabled = it
            tvAddBilling.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.color_F55C5C else R.color.color_F1F0F0
                )
            )
        }

        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd")
        tvPickerDate.text = format.format(calendar.time)
        viewModel.updateParam(3, format.format(calendar.time))

        etPrice.setOnEditorActionListener { _, _, _ ->
            if (etPrice.text.isEmpty() || etPrice.text.toString().startsWith("0")) {
                Toast.makeText(context, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val mInputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mInputMethodManager.hideSoftInputFromWindow(
                    etPrice.windowToken,
                    0
                )
                viewModel.totalAmount = etPrice.text.toString().toDouble()
                viewModel.updateTotalAmount()
                etPrice.clearFocus()
            }
            true
        }

        etName.setOnEditorActionListener { _, _, _ ->
            val mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                etName.windowToken,
                0
            )
            viewModel.updateParam(0, etName.text.toString())
            etName.clearFocus()
            true
        }

        tvAuto.setOnClickListener {
            adapter.billingType = false
            etPrice.setText("")
            etPrice.isEnabled = true
            viewModel.clearCost()
            tvAuto.setTextColor(resources.getColor(R.color.color_555555))
            tvInput.setTextColor(resources.getColor(R.color.color_EBEAEA))
        }

        tvInput.setOnClickListener {
            adapter.billingType = true
            etPrice.setText("")
            etPrice.isEnabled = false
            viewModel.clearCost()
            tvAuto.setTextColor(resources.getColor(R.color.color_EBEAEA))
            tvInput.setTextColor(resources.getColor(R.color.color_555555))
        }

        ivTooltip.setOnClickListener {
            val balloon = Balloon.Builder(requireContext())
                .setWidthRatio(0.6f)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText("직접 입력 시, 오차는 1%입니다.")
                .setTextColorResource(R.color.color_555555)
                .setBackgroundColorResource(R.color.white)
                .setTextSize(14f)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(12)
                .setCornerRadius(8f)
                .setBalloonAnimation(BalloonAnimation.ELASTIC)
                .build()
            balloon.showAsDropDown(ivTooltip)
            balloon.dismissWithDelay(1500L)
        }

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
                viewModel.updateParam(2, currency.currencyText)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
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
                viewModel.updateParam(1, "shopping")
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_market).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_market)
                viewModel.updateParam(1, "market")
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_food).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_food)
                viewModel.updateParam(1, "food")
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_hotel).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_hotel)
                viewModel.updateParam(1, "hotel")
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_transportation).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_transportation)
                viewModel.updateParam(1, "transportation")
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_other).setOnClickListener {
                ivCategory.setImageResource(R.drawable.ic_category_other)
                viewModel.updateParam(1, "other")
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
                if (PermissionUtil.hasCameraPermission(requireContext())) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    val values = ContentValues(1)
                    values.put(MediaStore.Images.Media.TITLE, "New Picture")
                    camUri = requireContext().contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, camUri)

                    cameraLauncher.launch(intent)
                } else {
                    PermissionUtil.requestCameraPermission(requireActivity())
                }
            }

            popupView.findViewById<TextView>(R.id.tv_gallery).setOnClickListener {
                popupWindow.dismiss()
                var intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

                launcher.launch(Intent.createChooser(intent, "파일을 선택해주세요."))
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
                        val tempDate =
                            year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                        tvPickerDate.text = tempDate
                        viewModel.updateParam(3, tempDate)
                    }
                }, year, month, day)
            }?.show()
        }

        ivBack.setOnClickListener { findNavController().popBackStack() }

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
                            viewModel.updateParam(4, it[position].id!!)
                        }
                    }
            }
        }

        tvAddBilling.setOnClickListener {
            var tempAmount = 0.0
            viewModel.paramList.value?.settlements?.forEach {
                tempAmount += it?.amount!!
            }

            if (viewModel.totalAmount * 0.99 <= tempAmount && tempAmount <= viewModel.totalAmount * 1.01) {
                UserDataStore.getUserToken(requireContext()).asLiveData()
                    .observe(viewLifecycleOwner) {
                        viewModel.postBillings(
                            args.travelId,
                            "Bearer $it",
                            tempImgFile
                        )
                    }
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "비용과 체크된 금액의 합이 다릅니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val imageUri = camUri
                val fileInfo = getFileInfo(imageUri!!, requireContext())

                if (tempImgFile.size < 2) {
                    tempImgFile.add(fileInfo!!)
                } else {
                    Toast.makeText(context, "사진은 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private var launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val data: Intent? = result.data

                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        val fileInfo = getFileInfo(imageUri!!, requireContext())

                        if (tempImgFile.size < 2) {
                            tempImgFile.add(fileInfo!!)
                            setImgCnt()
                        } else {
                            Toast.makeText(context, "사진은 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else if (data?.data != null) {
                    val imageUri = data.data
                    val fileInfo = getFileInfo(imageUri!!, requireContext())
                    if (tempImgFile.size < 2) {
                        tempImgFile.add(fileInfo!!)
                        setImgCnt()
                    } else {
                        Toast.makeText(context, "사진은 최대 2장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setImgCnt() {
        if (tempImgFile.size == 0) {
            binding.ivImg.setImageResource(R.drawable.ic_unselect_img)
            binding.tvImgCnt.visibility = View.GONE
        } else {
            binding.ivImg.setImageResource(R.drawable.ic_select_img)
            binding.tvImgCnt.visibility = View.VISIBLE
            binding.tvImgCnt.text = tempImgFile.size.toString()
        }
    }

}