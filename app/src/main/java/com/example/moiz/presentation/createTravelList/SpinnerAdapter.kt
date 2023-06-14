package com.example.moiz.presentation.createTravelList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.moiz.databinding.SpinnerCurrencyItemViewBinding
import com.example.moiz.domain.model.Currency

class SpinnerAdapter(
    context: Context,
    private val resID: Int,
    private val values: MutableList<Currency>,
) : ArrayAdapter<Currency>(context, 0, values) {
    override fun getCount(): Int = values.size
    override fun getItem(position: Int) = values[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCurrencyItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        val item = values[position]

        try {
            binding.layout.setPadding(8, 2, 8, 2)
            binding.tvSymbol.visibility = View.GONE
            binding.tvCurrencyText.text = item.currencyText
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = SpinnerCurrencyItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        val item = values[position]
        try {
            binding.tvSymbol.visibility = View.VISIBLE
            binding.tvSymbol.text = item.symbol
            binding.tvCurrencyText.text = item.currencyText
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root

    }
}