package com.example.moiz.presentation.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment(layoutID: Int) : Fragment(layoutID) {
    abstract fun init()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    inline fun <reified T : AppCompatActivity> getActivityOrNull(): T? =
        if (requireActivity() is T)
            requireActivity() as T
        else
            null
}