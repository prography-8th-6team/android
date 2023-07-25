package com.jerny.moiz.presentation.login

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.jerny.moiz.R
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.presentation.main.MainActivity
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_splash)

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                if (deepLink != null && deepLink.getBooleanQueryParameter("code", false)) {
                    val token = deepLink.getQueryParameter("code")
                    if (token != null) {
                        runBlocking {
                            UserDataStore.setJoinCode(this@SplashActivity, token)
                        }
                    }
                }
            }

        UserDataStore.getUserToken(this@SplashActivity).asLiveData().observe(this) {
            Handler().postDelayed(Runnable {
                val intent = Intent(
                    this@SplashActivity,
                    if (it != "") MainActivity::class.java else LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }, 3000)
        }

    }
}