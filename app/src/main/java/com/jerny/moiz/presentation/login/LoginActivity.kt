package com.jerny.moiz.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jerny.moiz.data.UserDataStore
import com.jerny.moiz.databinding.ActivityLoginBinding
import com.jerny.moiz.presentation.UiState
import com.jerny.moiz.presentation.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private var mBinding: ActivityLoginBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.userInfo.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {}

                    is UiState.Success -> {
                        runBlocking {
                            UserDataStore.setUserToken(
                                this@LoginActivity,
                                state.data.token.toString()
                            )
                        }
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)

                        finish()

                        runBlocking {
                            UserDataStore.setUserId(
                                this@LoginActivity,
                                state.data.user_id.toString()
                            )
                        }
                    }

                    is UiState.Error -> {}
                }
            }
        }
    }

    private fun initView() {
        binding.btnKakao.setOnClickListener {
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Timber.e(error.message)
                } else if (token != null) {
                    viewModel.getKakaoToken(token.accessToken)
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Timber.e(error.message)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        viewModel.getKakaoToken(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}