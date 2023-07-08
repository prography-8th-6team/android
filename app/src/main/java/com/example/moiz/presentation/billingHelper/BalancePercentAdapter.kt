package com.example.moiz.presentation.billingHelper

import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.R
import com.example.moiz.data.network.dto.BalancePercentDto
import com.example.moiz.databinding.ItemBillingPercentBinding
import kotlin.math.abs

class BalancePercentAdapter : ListAdapter<BalancePercentDto, BalancePercentAdapter.ViewHolder>(
    DiffCallback) {

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    inner class ViewHolder(private val binding: ItemBillingPercentBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(balancePercent: BalancePercentDto) {
            binding.tvName.text = balancePercent.nickname
            balancePercent.amount?.let {
                val height = (abs(it) / 2) * 1.16
                val params = FrameLayout.LayoutParams(38.dp, height.toInt().dp)

                // +
                if (it >= 0) {
                    binding.viewAmount.apply {
                        setBackgroundResource(R.drawable.bg_radius_4_top)
                        backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.color_d8f4f1)
                        layoutParams = params
                    }
                } else {
                    // -
                    params.gravity = Gravity.BOTTOM
                    binding.viewAmount.apply {
                        setBackgroundResource(R.drawable.bg_radius_4_bottom)
                        backgroundTintList =
                            ContextCompat.getColorStateList(itemView.context, R.color.color_f8f2c3)
                        layoutParams = params
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBillingPercentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<BalancePercentDto>() {
        override fun areItemsTheSame(
            oldItem: BalancePercentDto,
            newItem: BalancePercentDto,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BalancePercentDto,
            newItem: BalancePercentDto,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}