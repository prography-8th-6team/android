package com.jerny.moiz.presentation.detail.schedule

import android.app.Activity
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
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jerny.moiz.R
import com.jerny.moiz.databinding.CreateTravelListFragmentBinding
import com.jerny.moiz.databinding.FragmentAddScheduleBinding
import com.jerny.moiz.presentation.createTravelList.DatePickerDialog
import com.jerny.moiz.presentation.util.FileResult
import com.jerny.moiz.presentation.util.PermissionUtil
import com.jerny.moiz.presentation.util.getFileInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScheduleFragment : Fragment() {

    private lateinit var binding: FragmentAddScheduleBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.dpStartDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog()
            datePickerFragment.setOnOkClickListener { year, month, day ->
                binding.dpStartDate.text = "$year.$month.$day"
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        binding.dpEndDate.setOnClickListener {
            val datePickerFragment =
                DatePickerDialog(binding.dpStartDate.text.toString().replace(".", "-"))
            datePickerFragment.setOnOkClickListener { year, month, day ->
                binding.dpEndDate.text = "$year.$month.$day"
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        binding.root.setOnClickListener {
            val mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                binding.root.windowToken,
                0
            )
            binding.etName.clearFocus()
            binding.etMemo.clearFocus()
        }

        binding.ivCategory.setOnClickListener {
            val inflater =
                view?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.item_billing_category, null)

            val popupWindow =
                PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    isOutsideTouchable = true
                    isFocusable = true
                }

            val categoryClickListener: (Int) -> Unit = { resId ->
                binding.ivCategory.setImageResource(resId)
                popupWindow.dismiss()
            }

            popupView.findViewById<LinearLayout>(R.id.ll_shopping).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_shopping)
            }

            popupView.findViewById<LinearLayout>(R.id.ll_market).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_market)
            }

            popupView.findViewById<LinearLayout>(R.id.ll_food).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_food)
            }

            popupView.findViewById<LinearLayout>(R.id.ll_hotel).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_hotel)
            }

            popupView.findViewById<LinearLayout>(R.id.ll_transportation).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_transportation)
            }

            popupView.findViewById<LinearLayout>(R.id.ll_other).setOnClickListener {
                categoryClickListener(R.drawable.ic_category_other)
            }

            popupWindow.showAsDropDown(binding.ivCategory, -132, 20)
        }

        binding.etName.setOnEditorActionListener { _, _, _ ->
            val mInputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(
                binding.etName.windowToken,
                0
            )
            binding.etName.clearFocus()
            true
        }

        binding.ivImg.setOnClickListener {
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