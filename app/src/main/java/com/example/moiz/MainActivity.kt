package com.example.moiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// Activity 분리 : 스플래시, 로그인, 메인
class MainActivity : AppCompatActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
   }
}