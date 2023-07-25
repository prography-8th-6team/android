package com.jerny.moiz.presentation.billing.detail

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
import com.bumptech.glide.Glide
import com.jerny.moiz.databinding.DialogImgCustomBinding

class ImageCustomDialog(
    private val imgUrl: String
) : DialogFragment() {
    lateinit var binding: DialogImgCustomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogImgCustomBinding.inflate(inflater, container, false)
        Glide.with(requireContext()).load(imgUrl).into(binding.ivImg)

        binding.root.setOnClickListener {
            dismiss()
        }
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
        dialog?.window?.decorView?.setPadding(32, 0, 32, 0)
        dialog?.window?.setGravity(Gravity.CENTER)
    }
}