package com.example.moiz.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.service.autofill.UserData
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.moiz.data.UserDataStore
import com.example.moiz.databinding.ActivityLoginBinding
import com.example.moiz.databinding.ActivityMainBinding
import com.example.moiz.presentation.login.LoginActivity
import com.example.moiz.presentation.login.LoginViewModel
import com.kakao.sdk.auth.TokenManagerProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

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
                viewModel.postJoinCode(token, code)
            }
        }
    }

}