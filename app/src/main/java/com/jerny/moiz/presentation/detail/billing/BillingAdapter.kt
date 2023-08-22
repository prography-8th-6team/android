package com.jerny.moiz.presentation.detail.billing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jerny.moiz.data.network.dto.BillingDto
import com.jerny.moiz.databinding.ItemAccountBinding
import com.jerny.moiz.databinding.ItemHeaderBinding
import java.util.Currency

class BillingAdapter(
    private val onClick: (BillingDto) -> Unit
) : ListAdapter<BillingAdapter.ClassItem, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ClassItem>() {
            override fun areItemsTheSame(
                oldItem: ClassItem,
                newItem: ClassItem
            ): Boolean {
                return if (oldItem is ClassItem.Item && newItem is ClassItem.Item) {
                    (oldItem.item.id == newItem.item.id)
                } else if (oldItem is ClassItem.Date && newItem is ClassItem.Date)
                    (oldItem == newItem)
                else
                    true
            }

            override fun areContentsTheSame(
                oldItem: ClassItem,
                newItem: ClassItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    sealed class ClassItem {
        data class Date(val dateTime: String? = null) : ClassItem()
        data class Item(val item: BillingDto) : ClassItem()
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is ClassItem.Date -> {
                0
            }

            else -> {
                1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                DateTimeHolder(
                    ItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ViewHolder(
                    ItemAccountBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), onClick
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is ViewHolder -> holder.onBind((item as ClassItem.Item))
            is DateTimeHolder -> holder.onBind((item as ClassItem.Date))
        }
    }

    fun submitListEx(list: List<BillingDto>) {
        val submitItem = java.util.ArrayList<ClassItem>()

        var lastDate: String? = null

        list.forEachIndexed { _, data ->
            if (lastDate != data.paid_date) {
                val date = data.paid_date?.split("-")
                val dateTime = "${date?.get(1)}월 ${date?.get(2)}일"
                submitItem.add(ClassItem.Date(dateTime))
                lastDate = data.paid_date
            }
            submitItem.add(ClassItem.Item(data))
        }
        submitList(submitItem)
    }

    inner class DateTimeHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ClassItem.Date) = with(binding) {
            tvStart.text = data.dateTime
        }
    }

    inner class ViewHolder(
        private val binding: ItemAccountBinding,
        private val onClick: (BillingDto) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ClassItem.Item) = with(binding) {
            ivCategory.setImageResource(
                when (data.item.category) {
                    "food" -> com.jerny.moiz.R.drawable.ic_category_food
                    "transportation" -> com.jerny.moiz.R.drawable.ic_category_transportation
                    "hotel" -> com.jerny.moiz.R.drawable.ic_category_hotel
                    "market" -> com.jerny.moiz.R.drawable.ic_category_market
                    "shopping" -> com.jerny.moiz.R.drawable.ic_category_shopping
                    "other" -> com.jerny.moiz.R.drawable.ic_category_other
                    else -> com.jerny.moiz.R.drawable.ic_category_other
                }
            )
            tvTitle.text = data.item.title
            tvName.text = data.item.paid_by
            tvCurrency.text = Currency.getInstance(data.item.total_amount_currency).symbol
            tvPrice.text = data.item.total_amount

            root.setOnClickListener {
                onClick(data.item)
            }
        }
    }

}