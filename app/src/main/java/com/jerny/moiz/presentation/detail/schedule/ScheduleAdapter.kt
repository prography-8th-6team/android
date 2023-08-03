package com.jerny.moiz.presentation.detail.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.ItemScheduleBinding

class ScheduleAdapter() : ListAdapter<ScheduleDto, ScheduleAdapter.ViewHolder>(
    DiffCallback) {
    inner class ViewHolder(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(schedule: ScheduleDto) {
            binding.tvName.text = schedule.title
            binding.tvTime.text = schedule.start_at
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ScheduleDto>() {
            override fun areItemsTheSame(oldItem: ScheduleDto, newItem: ScheduleDto): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: ScheduleDto, newItem: ScheduleDto): Boolean {
                return oldItem == newItem
            }
        }
    }

}