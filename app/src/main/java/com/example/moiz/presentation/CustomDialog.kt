package com.example.moiz.presentation

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.moiz.databinding.CustomDialogBinding

class CustomDialog(
    val content: String,
    val cancelText: String? = "취소",
    val okText: String,
    val okClickListener: () -> Unit,
) : DialogFragment() {
    lateinit var binding: CustomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = CustomDialogBinding.inflate(inflater, container, false)
        binding.tvContent.text = content
        binding.tvCancel.text = cancelText
        binding.tvOk.text = okText

        binding.tvOk.setOnClickListener {
            okClickListener.invoke()
            dismiss()
        }

        binding.tvCancel.setOnClickListener { dismiss() }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val windowManager =
            requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        dialog?.window?.setLayout((width * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setGravity(Gravity.BOTTOM)
    }
}