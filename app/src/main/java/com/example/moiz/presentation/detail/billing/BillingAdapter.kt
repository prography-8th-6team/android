package com.example.moiz.presentation.detail.billing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.data.network.dto.BillingDto
import com.example.moiz.databinding.ItemAccountBinding
import java.util.Currency

class BillingAdapter(
    private val onClick: (BillingDto) -> Unit
) : ListAdapter<BillingDto, BillingAdapter.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BillingDto>() {
            override fun areItemsTheSame(
                oldItem: BillingDto,
                newItem: BillingDto
            ): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(
                oldItem: BillingDto,
                newItem: BillingDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClick
        )
    }

    inner class ViewHolder(
        private val binding: ItemAccountBinding,
        private val onClick: (BillingDto) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: BillingDto) = with(binding) {
            ivCategory.setImageResource(
                when (data.category) {
                    "food" -> com.example.moiz.R.drawable.ic_category_food
                    "transportation" -> com.example.moiz.R.drawable.ic_category_transportation
                    "hotel" -> com.example.moiz.R.drawable.ic_category_hotel
                    "market" -> com.example.moiz.R.drawable.ic_category_market
                    "shopping" -> com.example.moiz.R.drawable.ic_category_shopping
                    "other" -> com.example.moiz.R.drawable.ic_category_other
                    else -> com.example.moiz.R.drawable.ic_category_other
                }
            )
            tvTitle.text = data.title
            tvName.text = data.paid_by
            tvCurrency.text = Currency.getInstance(data.total_amount_currency).symbol
            tvPrice.text = data.total_amount

            root.setOnClickListener {
                onClick(data)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}