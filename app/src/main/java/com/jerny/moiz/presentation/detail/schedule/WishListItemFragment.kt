package com.jerny.moiz.presentation.detail.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.jerny.moiz.data.network.dto.ScheduleDto
import com.jerny.moiz.databinding.FragmentWishListItemBinding
import com.jerny.moiz.databinding.ItemWishListBinding
import com.jerny.moiz.presentation.util.showOrGone
import com.jerny.moiz.presentation.util.showOrHide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishListItemFragment(
    val list: List<ScheduleDto>,
    val flag: Boolean,
    val isFlag: (Boolean) -> Unit,
    val onDeleteItem: (Int) -> Unit,
    val onClick: (Int) -> Unit
) : Fragment() {

    private lateinit var binding: FragmentWishListItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWishListItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.glWishList.apply {
            binding.glWishList.removeAllViews()
            list.forEach { list ->
                val ageItem = ItemWishListBinding.inflate(layoutInflater)
                ageItem.tvTitle.text = list.title
                ageItem.ivRemove.showOrHide(flag)

                ageItem.root.setOnClickListener {
                    if (!flag)
                        onClick(list.id!!)
                }

                ageItem.ivRemove.setOnClickListener {
                    onDeleteItem(list.id!!)
                    binding.glWishList.removeView(ageItem.root)
                }

                ageItem.root.setOnLongClickListener {
                    isFlag(!flag)
                    true
                }

                addView(ageItem.root)
            }
        }
    }

}
