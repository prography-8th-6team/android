package com.jerny.moiz.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation.findNavController
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    private val viewModel by viewModels<MainViewModel>()
    private var isJoin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        UserDataStore.getJoinCode(this@MainActivity).asLiveData().observe(this) { code ->
            UserDataStore.getUserToken(this@MainActivity).asLiveData().observe(this) { token ->
                if (code != "" && token != "") {
                    if (!isJoin) {
                        viewModel.postJoinCode("Bearer $token", code)
                        isJoin = true
                        runBlocking {
                            UserDataStore.setJoinCode(this@MainActivity, "")
                        }
                    }
                }
            }
        }
    }

    private var backKeyPressedTime: Long = 0
    override fun onBackPressed() {
        val navController = findNavController(this, R.id.nav_host_fragment)
        if (navController.currentDestination?.id == R.id.mainFragment) {
            if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        }

        super.onBackPressed()
    }

}