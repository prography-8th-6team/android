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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishListItemFragment(val list: List<ScheduleDto>, val onClick: (Int) -> Unit) : Fragment() {

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

        binding.glAgeAddView.apply {
            binding.glAgeAddView.removeAllViews()
            list.forEach { list ->
                val ageItem = ItemWishListBinding.inflate(layoutInflater)
                ageItem.tvTitle.text = list.title

                val param = GridLayout.LayoutParams(
                    GridLayout.spec(
                        GridLayout.UNDEFINED, GridLayout.FILL, 1f
                    ),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                )

                ageItem.root.layoutParams = param
                ageItem.root.setOnClickListener {
                    onClick(list.id!!)
                }

                addView(ageItem.root)
            }
        }
    }

}
