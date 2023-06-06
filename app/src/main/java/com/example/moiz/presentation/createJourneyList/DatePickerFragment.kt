package com.example.moiz.presentation.createJourneyList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.moiz.databinding.DatePickerDialogBinding

class DatePickerDialog : DialogFragment() {
    private lateinit var binding: DatePickerDialogBinding
    private lateinit var listener: DatePickerDialogClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DatePickerDialogBinding.inflate(inflater, container, false)
        binding.btnOk.setOnClickListener {
            listener.onOkClik(
                binding.datePicker.year.toString(),
                (binding.datePicker.month + 1).toString().padStart(2, '0'),
                binding.datePicker.dayOfMonth.toString().padStart(2, '0'))
            dismiss()
        }
        binding.btnCancel.setOnClickListener { dismiss() }
        return binding.root
    }

    fun setOnOkClikListener(listener: (String, String, String) -> Unit) {
        this.listener = object : DatePickerDialogClickListener {
            override fun onOkClik(year: String, month: String, day: String) {
                listener(year, month, day)
            }
        }
    }

    interface DatePickerDialogClickListener {
        fun onOkClik(year: String, month: String, day: String)
    }
}



