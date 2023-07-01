package com.example.moiz.presentation.billing

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moiz.databinding.ItemPaidMemberBinding
import com.example.moiz.domain.model.InputCostEntity
import timber.log.Timber

class AddBillingAdapter(
    private val onClick: (InputCostEntity) -> Unit,
    private val itemOnChange: (InputCostEntity) -> Unit
) : ListAdapter<InputCostEntity, AddBillingAdapter.ViewHolder>(DiffCallback) {

    // false : 1/n빵 true : 직접입력
    var billingType: Boolean = false

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<InputCostEntity>() {
            override fun areItemsTheSame(
                oldItem: InputCostEntity,
                newItem: InputCostEntity
            ): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(
                oldItem: InputCostEntity,
                newItem: InputCostEntity
            ): Boolean {
                return oldItem != newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPaidMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClick, itemOnChange
        )
    }

    inner class ViewHolder(
        private val binding: ItemPaidMemberBinding,
        private val onClick: (InputCostEntity) -> Unit,
        private val itemOnChange: (InputCostEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: InputCostEntity) = with(binding) {
            cbMember.isChecked = data.isChecked
            tvMember.text = data.name
            etCost.setText(String.format("%.2f", data.cost))
            etCost.isEnabled = billingType

            cbMember.setOnClickListener {
                onClick(data.copy(isChecked = !data.isChecked))
                etCost.clearFocus()
            }

            etCost.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (etCost.text.toString().isEmpty() || etCost.text.startsWith(".")) {
                            Toast.makeText(itemView.context, "금액을 입력해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val inputMethodManager =
                                itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(itemView.windowToken, 0)
                            itemOnChange(data.copy(cost = etCost.text.toString().toDouble()))
                            onClick(data.copy(isChecked = !data.isChecked))
                            etCost.clearFocus()
                        }
                        return true
                    }
                    return false
                }
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}