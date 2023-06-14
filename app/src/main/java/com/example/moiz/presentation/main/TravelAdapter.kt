package com.example.moiz.presentation.main


import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.R
import com.example.moiz.data.network.dto.TravelDto
import com.example.moiz.databinding.TravelItemViewBinding
import java.text.SimpleDateFormat

class TravelAdapter : ListAdapter<TravelDto, TravelAdapter.TravelViewHolder>(TravelDiffCallback) {
    private lateinit var context: Context

    inner class TravelViewHolder(private val binding: TravelItemViewBinding) : RecyclerView.ViewHolder(
        binding.root) {
        fun bind(travel: TravelDto) {
            val memberCount = travel.members?.size!!
            val fromFormat = SimpleDateFormat("yyyy-MM-dd")
            val toFormat = SimpleDateFormat("yy-MM-dd")

            binding.tvTitle.text = travel.title
            binding.tvDate.text = "${toFormat.format(fromFormat.parse(travel.start_date))} - ${
                toFormat.format(fromFormat.parse(travel.end_date))
            }"
            binding.tvMember1.text = travel.members?.get(0)

            when {
                memberCount >= 3 -> {
                    binding.tvMember2.text = travel.members[1]
                    binding.tvMemberCount.text = "+${memberCount - 2}"
                }

                memberCount == 2 -> {
                    binding.tvMember2.text = travel.members[1]
                    binding.tvMemberCount.visibility = View.GONE
                }

                memberCount == 1 -> {
                    binding.tvMember2.visibility = View.GONE
                    binding.tvMemberCount.visibility = View.GONE
                }

                else -> {
                    binding.tvMember1.visibility = View.GONE
                    binding.tvMember2.visibility = View.GONE
                    binding.tvMemberCount.visibility = View.GONE
                }
            }

            binding.root.backgroundTintList = when (travel.color) {
                "f9b7a4" -> ContextCompat.getColorStateList(itemView.context, R.color.color_f9b7a4)
                "d8f4f1" -> ContextCompat.getColorStateList(itemView.context, R.color.color_d8f4f1)
                "f8f2c3" -> ContextCompat.getColorStateList(itemView.context, R.color.color_f8f2c3)
                "a4e8c0" -> ContextCompat.getColorStateList(itemView.context, R.color.color_a4e8c0)
                "abe8ff" -> ContextCompat.getColorStateList(itemView.context, R.color.color_abe8ff)
                else -> ContextCompat.getColorStateList(itemView.context, R.color.color_f4f4f4)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val binding = TravelItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return TravelViewHolder(binding)
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(10.dp, 10.dp, 10.dp, 10.dp)

        if ((position % 4 == 0) or (position % 4 == 2)) {
            params.height = 235.dp
        } else {
            params.height = 195.dp
        }

//      if (position <= 7) {
//         if ((position % 4 == 0) or (position % 4 == 2)) {
//            params.height = 235.dp
//         } else {
//            params.height = 195.dp
//         }
//      } else {
//         if ((position % 4 == 0) or (position % 4 == 2)) {
//            params.height = 235.dp
//         } else {
//            params.height = 195.dp
//         }
//      }

//      if (position <= 7) {
//         if ((position % 4 == 0) or (position % 4 == 2)) {
//            params.height = 235.dp
//         } else {
//            params.height = 195.dp
//         }
//      } else {
//         if ((position % 4 == 0) or (position % 4 == 3)) {
//            params.height = 235.dp
//         } else {
//            params.height = 195.dp
//         }
//      }

        holder.itemView.layoutParams = params
        holder.bind(getItem(position))
    }

    object TravelDiffCallback : DiffUtil.ItemCallback<TravelDto>() {
        override fun areItemsTheSame(oldItem: TravelDto, newItem: TravelDto): Boolean {
            return oldItem == newItem
        }

        // id 로 비교
        override fun areContentsTheSame(oldItem: TravelDto, newItem: TravelDto): Boolean {
            return oldItem.id == newItem.id
        }
    }
}