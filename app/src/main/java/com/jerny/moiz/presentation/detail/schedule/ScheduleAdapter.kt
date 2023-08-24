package com.jerny.moiz.presentation.detail.schedule

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jerny.moiz.R
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.ItemScheduleBinding
import com.jerny.moiz.domain.model.Category
import java.text.SimpleDateFormat

class ScheduleAdapter(private val context: Context, private val onClick: OnClickListener) :
    ListAdapter<ScheduleDto, ScheduleAdapter.ViewHolder>(DiffCallback) {
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    val fromFormat = SimpleDateFormat("HH:mm:ss")
    val toFormat = SimpleDateFormat("HH:mm")

    inner class ViewHolder(private val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(schedule: ScheduleDto) {
            binding.tvName.text = schedule.title
            binding.tvOrder.text = "${adapterPosition + 1}"

            val totalParams =
                FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            val scheduleParams =
                LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

            // 끝 시간이 없는 경우
            if (schedule.end_at.isNullOrEmpty()) {
                binding.tvTime.text = "${toFormat.format(fromFormat.parse(schedule.start_at))}"
                totalParams.setMargins(37.dp, 0, 0, 0)
                scheduleParams.setMargins(38.dp, 0, 0, 0)
            } else {
                binding.tvTime.text = "${toFormat.format(fromFormat.parse(schedule.start_at))}-${
                    toFormat.format(fromFormat.parse(schedule.end_at))
                }"
                totalParams.setMargins(19.dp, 0, 0, 0)
                scheduleParams.setMargins(17.dp, 0, 0, 0)
            }
            binding.llTotal.layoutParams = totalParams
            binding.llSchedule.layoutParams = scheduleParams

            // 카테고리에 맞게 색상 설정
            binding.tvOrder.backgroundTintList = when (schedule.category) {
                Category.Shopping.value -> ColorStateList.valueOf(context.getColor(R.color.color_f9b7a4))
                Category.Sights.value -> ColorStateList.valueOf(context.getColor(R.color.color_d8f4f1))
                Category.Food.value -> ColorStateList.valueOf(context.getColor(R.color.color_f8f2c3))
                Category.Hotel.value -> ColorStateList.valueOf(context.getColor(R.color.color_a4e8c0))
                else -> ColorStateList.valueOf(context.getColor(R.color.color_abe8ff))
            }

            binding.tvDescription.text = schedule.description

            binding.llSchedule.setOnClickListener {
                onClick.onClick(schedule)
            }

            binding.tvRemove.setOnClickListener {
                val location = IntArray(2)
                binding.tvOrder.getLocationOnScreen(location)
                // swipe 했을 때만 삭제되게끔
                if (location[0] < 0) {
                    schedule.id?.let { it1 -> onClick.delete(it1) }
                    Toast.makeText(
                        binding.root.context, "일정이 삭제되었어요.", Toast.LENGTH_SHORT
                    ).show()
                }
            }
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

    object DiffCallback : DiffUtil.ItemCallback<ScheduleDto>() {
        override fun areItemsTheSame(
            oldItem: ScheduleDto,
            newItem: ScheduleDto,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScheduleDto,
            newItem: ScheduleDto,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface OnClickListener {
        fun delete(id: Int)

        fun onClick(schedule: ScheduleDto)
    }
}