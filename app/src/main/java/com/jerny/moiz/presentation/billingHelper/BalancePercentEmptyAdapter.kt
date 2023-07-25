package com.jerny.moiz.presentation.billingHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jerny.moiz.data.network.dto.BillingMembersDto
import com.jerny.moiz.databinding.ItemBillingPercentEmptyBinding

class BalancePercentEmptyAdapter : ListAdapter<BillingMembersDto, BalancePercentEmptyAdapter.ViewHolder>(
    DiffCallback) {

    inner class ViewHolder(private val binding: ItemBillingPercentEmptyBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(member: BillingMembersDto) {
            binding.tvName.text = member.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBillingPercentEmptyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<BillingMembersDto>() {
        override fun areItemsTheSame(
            oldItem: BillingMembersDto,
            newItem: BillingMembersDto,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BillingMembersDto,
            newItem: BillingMembersDto,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
}