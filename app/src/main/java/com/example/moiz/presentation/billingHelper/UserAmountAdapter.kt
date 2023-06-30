package com.example.moiz.presentation.billingHelper

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.R
import com.example.moiz.data.network.dto.UserAmountsDto
import com.example.moiz.databinding.ItemBillingUserAmountBinding
import kotlin.math.abs

class UserAmountAdapter : ListAdapter<UserAmountsDto, UserAmountAdapter.ViewHolder>(
    UserAmountDiffCallback) {

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    inner class ViewHolder(private val binding: ItemBillingUserAmountBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(userAmount: UserAmountsDto) {
            binding.tvName.text = userAmount.user
            userAmount.amount?.let {
                val height = abs(it) * 1.16
                binding.viewAmount.layoutParams =
                    LinearLayout.LayoutParams(38.dp, height.toInt().dp)

                // +
                if (it >= 0) {
                    binding.viewAmount.setBackgroundResource(R.drawable.bg_radius_4_top)
                    binding.viewAmount.backgroundTintList =
                        ContextCompat.getColorStateList(itemView.context, R.color.color_d8f4f1)
                } else {
                    // -
                    binding.viewAmount.setBackgroundResource(R.drawable.bg_radius_4_bottom)
                    binding.viewAmount.backgroundTintList =
                        ContextCompat.getColorStateList(itemView.context, R.color.color_f8f2c3)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBillingUserAmountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object UserAmountDiffCallback : DiffUtil.ItemCallback<UserAmountsDto>() {
        override fun areItemsTheSame(oldItem: UserAmountsDto, newItem: UserAmountsDto): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserAmountsDto, newItem: UserAmountsDto): Boolean {
            return oldItem.user == newItem.user
        }
    }
}