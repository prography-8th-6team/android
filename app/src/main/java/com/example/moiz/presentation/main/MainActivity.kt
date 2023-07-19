package com.example.moiz.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        UserDataStore.getJoinCode(this@MainActivity).asLiveData().observe(this) { code ->
            UserDataStore.getUserToken(this@MainActivity).asLiveData().observe(this) { token ->
                if(code != "" && token != "") {
                    viewModel.postJoinCode(token, code)
                }
            }
        }
    }

}