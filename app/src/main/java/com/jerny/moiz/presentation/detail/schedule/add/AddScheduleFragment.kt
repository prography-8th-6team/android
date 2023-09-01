package com.jerny.moiz.presentation.detail.schedule.add

import android.app.Activity
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.FragmentAddScheduleBinding
import com.jerny.moiz.presentation.travel.create.DatePickerDialog
import com.jerny.moiz.presentation.util.FileResult
import com.jerny.moiz.presentation.util.PermissionUtil
import com.jerny.moiz.presentation.util.getFileInfo
import com.jerny.moiz.presentation.util.hideKeyboard
import com.jerny.moiz.presentation.util.setCategorySelectView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class AddScheduleFragment : Fragment() {

    private lateinit var binding: FragmentAddScheduleBinding

    private val viewModel: AddScheduleViewModel by viewModels()
    private val args: AddScheduleFragmentArgs by navArgs()

    private val currentDateTime = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val timeFormat = SimpleDateFormat("HH:mm")

    private var date = dateFormat.format(currentDateTime)
    private var dateTime = timeFormat.format(currentDateTime)

    var tempImgFile = arrayListOf<FileResult>()
    var camUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddScheduleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        dpDate.text = args.startDate.replace("-", ".")
        dpStartDate.text = dateTime
        dpEndDate.text = dateTime

        viewModel.updateParam(2, "confirmed")
        viewModel.updateParam(4, args.startDate)
        viewModel.updateParam(5, dateTime)
        viewModel.updateParam(6, dateTime)

        imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        etName.doOnTextChanged { text, start, before, count ->
            viewModel.updateParam(0, text.toString())
        }

        etMemo.doOnTextChanged { text, start, before, count ->
            viewModel.updateParam(1, text.toString())
        }

        lifecycleScope.launch {
            viewModel.totalValidation.collect {
                tvAddSchedule.isEnabled = it
                tvAddSchedule.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.color_F55C5C else R.color.color_F1F0F0
                    )
                )
            }
        }

        root.setOnClickListener {
            it.hideKeyboard()

            etName.clearFocus()
            etMemo.clearFocus()
        }

        dpDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog(args.startDate, args.endDate)
            datePickerFragment.setOnOkClickListener { year, month, day ->
                dpDate.text = "$year.$month.$day"
                viewModel.updateParam(4, "$year-$month-$day")
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        dpStartDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val startDate = SimpleDateFormat("HH:mm").format(cal.time)
                dpStartDate.text = startDate
                viewModel.updateParam(5, startDate)
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        dpEndDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val endDate = SimpleDateFormat("HH:mm").format(cal.time)
                dpEndDate.text = endDate
                viewModel.updateParam(6, endDate)
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        ivCategory.setCategorySelectView(false) {
            viewModel.updateParam(3, it)
        }

        tvAddSchedule.setOnClickListener {
            UserDataStore.getUserToken(requireContext()).asLiveData().observe(viewLifecycleOwner) {
                viewModel.postSchedule("Bearer $it", args.travelId, tempImgFile)
                findNavController().popBackStack()
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
            popupWindow.showAsDropDown(binding.ivImg, 0, 20)

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

    private fun setImgCnt() = with(binding) {
        if (tempImgFile.size == 0) {
            ivImg.setImageResource(R.drawable.ic_unselect_img)
            tvImgCnt.visibility = View.GONE
        } else {
            ivImg.setImageResource(R.drawable.ic_select_img)
            tvImgCnt.visibility = View.VISIBLE
            tvImgCnt.text = tempImgFile.size.toString()
        }
    }
}