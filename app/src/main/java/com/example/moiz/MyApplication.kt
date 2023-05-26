package com.example.moiz

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "6dd5e434c8c6e5ad91ff752d7101a2bb")
    }
}