package com.example.moiz.presentation.main


import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.domain.model.Journey
import com.example.moiz.databinding.JourneyItemViewBinding

class JourneyAdapter : ListAdapter<Journey, JourneyAdapter.JourneyViewHolder>(JourneyDiffCallback) {
   inner class JourneyViewHolder(private val binding: JourneyItemViewBinding) : RecyclerView.ViewHolder(
      binding.root) {
      fun bind(journey: Journey) {
         val memberCount = journey.memberList.size
         binding.tvTitle.text = journey.title
         binding.tvDate.text = "${journey.startDate} ~ ${journey.endDate}"
         binding.tvMember1.text = journey.memberList[0]
         binding.index.text = "${adapterPosition}"
         when {
            memberCount >= 3 -> {
               binding.tvMember2.text = journey.memberList[1]
               binding.tvMemberCount.text = "+${memberCount - 2}"
            }

            memberCount == 2 -> {
               binding.tvMember2.text = journey.memberList[1]
               binding.tvMemberCount.visibility = View.GONE
            }

            memberCount == 1 -> {
               binding.tvMember2.visibility = View.GONE
               binding.tvMemberCount.visibility = View.GONE
            }
         }

         binding.root.setOnClickListener {
            Log.i("adapterPosition: ", adapterPosition.toString())
            Log.i("oldPosition: ", oldPosition.toString())
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JourneyViewHolder {
      val binding =
         JourneyItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return JourneyViewHolder(binding)
   }

   val Int.dp: Int
      get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

   override fun onBindViewHolder(holder: JourneyViewHolder, position: Int) {
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

   object JourneyDiffCallback : DiffUtil.ItemCallback<Journey>() {
      override fun areItemsTheSame(oldItem: Journey, newItem: Journey): Boolean {
         return oldItem == newItem
      }

      // title 로 비교해도 괜찮은지?
      override fun areContentsTheSame(oldItem: Journey, newItem: Journey): Boolean {
         return oldItem.title == newItem.title
      }
   }
}