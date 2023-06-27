package com.example.moiz.presentation.billing

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.data.network.dto.PostBillingDto
import com.example.moiz.data.network.dto.SettlementsDto
import com.example.moiz.databinding.FragmentAddBillingBinding
import com.example.moiz.presentation.util.FileResult
import com.example.moiz.presentation.util.PermissionUtil
import com.example.moiz.presentation.util.getFileInfo
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class AddBillingFragment : Fragment() {

    private lateinit var binding: FragmentAddBillingBinding
    private val viewModel: BillingViewModel by viewModels()

    //private lateinit var adapter: BillingInputAdapter
    var tempImgFile = arrayListOf<FileResult>()
    var camUri: Uri? = null
    private val args: AddBillingFragmentArgs by navArgs()

    val temp = PostBillingDto(
        "",
        0,
        "",
        "USD",
        listOf(SettlementsDto(7, 12000))
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
        UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
            viewModel.getBillingMembers(
                args.travelId, "Bearer $it"
            )
        }

        initViews()
    }

    private fun initViews() = with(binding) {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy.MM.dd")
        tvPickerDate.text = format.format(calendar.time)

        etPrice.setOnEditorActionListener { _, _, _ ->
            //adapter.inputPrice = etPrice.text.toString().toDouble()
            val mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                etPrice.windowToken,
                0
            )
            //adapter.notifyDataSetChanged()
            true
        }

        etName.setOnEditorActionListener { _, _, _ ->
            val mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                etName.windowToken,
                0
            )
            true
        }

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
                            //temp.paid_by = it[position].id!!
                        }
                    }

                //adapter = BillingInputAdapter()
                //adapter.submitListEx(it)
                rvPaidForMembers.layoutManager = LinearLayoutManager(context)
                //rvPaidForMembers.adapter = adapter
            }
        }

        tvAddBilling.setOnClickListener {
            temp.title = etName.text.toString()
            if (temp.title.isNotEmpty() && temp.paid_by != 0 && temp.paid_date.isNotEmpty() && temp.currency.isNotEmpty() && temp.settlements.isNotEmpty()) {
                UserDataStore.getUserToken(requireContext()).asLiveData()
                    .observe(viewLifecycleOwner) {
                        viewModel.postBillings(
                            args.travelId,
                            "Bearer $it",
                            temp
                        )
                    }
            } else {
                Toast.makeText(context, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
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