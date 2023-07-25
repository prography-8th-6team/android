package com.jerny.moiz.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
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