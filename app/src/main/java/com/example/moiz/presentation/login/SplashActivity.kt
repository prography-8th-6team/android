package com.example.moiz.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.moiz.R
import com.example.moiz.data.UserDataStore
import com.example.moiz.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_splash)

        UserDataStore.getUserToken(this@SplashActivity).asLiveData().observe(this) {
            Handler().postDelayed(Runnable {
                val intent = Intent(
                    this@SplashActivity,
                    if (it != "") MainActivity::class.java else LoginActivity::class.java
                )
                startActivity(intent)
            }, 3000)
        }
    }
}