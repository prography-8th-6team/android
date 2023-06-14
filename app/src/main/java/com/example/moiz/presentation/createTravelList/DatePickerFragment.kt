package com.example.moiz.presentation.createTravelList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.moiz.databinding.DatePickerDialogBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class DatePickerDialog(private val startDate: String? = null) : DialogFragment() {
    private lateinit var binding: DatePickerDialogBinding
    private lateinit var listener: DatePickerDialogClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DatePickerDialogBinding.inflate(inflater, container, false)
        startDate?.let {
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat("yyyy.MM.dd").parse(it)
            binding.datePicker.minDate = cal.timeInMillis
        }

        binding.btnOk.setOnClickListener {
            listener.onOkClick(
                binding.datePicker.year.toString(),
                (binding.datePicker.month + 1).toString().padStart(2, '0'),
                binding.datePicker.dayOfMonth.toString().padStart(2, '0'))
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
        return binding.root
    }

    fun setOnOkClickListener(listener: (String, String, String) -> Unit) {
        this.listener = object : DatePickerDialogClickListener {
            override fun onOkClick(year: String, month: String, day: String) {
                listener(year, month, day)
            }
        }
    }

    interface DatePickerDialogClickListener {
        fun onOkClick(year: String, month: String, day: String)
    }
}



