package com.jerny.moiz.presentation.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jerny.moiz.R
import com.jerny.moiz.databinding.ItemCategoryBinding
import java.text.DecimalFormat

object ViewExtensions {
    @Volatile
    private var enabled: Boolean = true
    private val enableAgain = Runnable { enabled = true }

    fun canPerform(view: View): Boolean {
        if (enabled) {
            enabled = false
            view.postDelayed(enableAgain, 500)
            return true
        }
        return false
    }
}

fun <T : View> T.onClickDebounced(click: (view: T) -> Unit) {
    setOnClickListener {
        if (ViewExtensions.canPerform(it)) {
            @Suppress("UNCHECKED_CAST")
            click(it as T)
        }
    }
}

fun Int?.toCostFormat(): String {
    return DecimalFormat("#,###").format(this?.toLong() ?: 0L)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showOrGone(isShow: Boolean) {
    visibility = if (isShow)
        View.VISIBLE
    else
        View.GONE
}

fun View.showOrHide(isShow: Boolean) {
    visibility = if (isShow)
        View.VISIBLE
    else
        View.INVISIBLE
}

fun TextView.setTextColorByRes(colorRes: Int) {
    setTextColor(ContextCompat.getColor(context, colorRes))
}

fun View.setBackGroundColorByRes(colorRes: Int) {
    background = ContextCompat.getDrawable(context, colorRes)
}

fun ImageView.setCategorySelectView(isIncludeOther: Boolean = true, click: (String) -> Unit) {
    val inflater =
        this.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val popupView = inflater.inflate(R.layout.item_category, null)
    val popupWindow =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
            isFocusable = true
        }

    val categoryBinding = ItemCategoryBinding.bind(popupView)

    val categoryClickListener: (Int) -> Unit = { resId ->
        this.setImageResource(resId)
        popupWindow.dismiss()
    }

    categoryBinding.llShopping.setOnClickListener {
        categoryClickListener(R.drawable.ic_category_shopping)
        click("shopping")
    }

    categoryBinding.llMarket.setOnClickListener {
        categoryClickListener(R.drawable.ic_category_market)
        click("market")
    }

    categoryBinding.llFood.setOnClickListener {
        categoryClickListener(R.drawable.ic_category_food)
        click("food")
    }

    categoryBinding.llHotel.setOnClickListener {
        categoryClickListener(R.drawable.ic_category_hotel)
        click("hotel")
    }

    categoryBinding.llTransportation.setOnClickListener {
        categoryClickListener(R.drawable.ic_category_transportation)
        click("transportation")
    }

    if (isIncludeOther) {
        categoryBinding.llOther.show()

        categoryBinding.llOther.setOnClickListener {
            categoryClickListener(R.drawable.ic_category_other)
            click("other")
        }
    }

    this.setOnClickListener {
        if (popupWindow.isShowing)
            popupWindow.dismiss()
        else
            popupWindow.showAsDropDown(this, -132, 20)
    }

}