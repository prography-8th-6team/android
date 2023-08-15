package com.jerny.moiz.presentation.util

import android.app.TimePickerDialog
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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.jerny.moiz.databinding.CustomDialogBinding
import com.jerny.moiz.databinding.DialogScheduleBinding
import com.jerny.moiz.presentation.createTravelList.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar

class ScheduleDialog(
    private val okClickListener: (String, String, String) -> Unit
) : DialogFragment() {
    lateinit var binding: DialogScheduleBinding

    private var date = ""
    private var startDate = ""
    private var endDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DialogScheduleBinding.inflate(inflater, container, false)

        binding.dpDate.setOnClickListener {
            val datePickerFragment = DatePickerDialog()
            datePickerFragment.setOnOkClickListener { year, month, day ->
                date = "$year.$month.$day"
                binding.dpDate.text = date
            }
            datePickerFragment.show(childFragmentManager, null)
        }

        binding.dpStartDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val tempDate = SimpleDateFormat("HH:mm").format(cal.time)
                startDate = tempDate
                binding.dpStartDate.text = startDate
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.dpEndDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                val tempDate = SimpleDateFormat("HH:mm").format(cal.time)
                endDate = tempDate
                binding.dpEndDate.text = endDate
            }

            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.tvOk.setOnClickListener {
            if (date == "" || startDate == "" || endDate == "") Toast.makeText(
                context,
                "날짜와 시간을 모두 선택해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            else {
                okClickListener.invoke(date, startDate, endDate)
                dismiss()
            }
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
        dialog?.window?.setGravity(Gravity.CENTER)
    }
}