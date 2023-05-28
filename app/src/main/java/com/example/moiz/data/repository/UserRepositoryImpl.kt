package com.example.moiz.data.repository

import com.example.moiz.data.network.UserService
import com.example.moiz.data.network.dto.KakaoToken
import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.domain.model.TokenEntity
import com.example.moiz.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) :
    UserRepository {
    override suspend fun loginByKakaoToken(token: String): UserResponseDto {
        val temp = userService.sendKakaoToken(token)
        return if (temp.isSuccessful) {
            temp.body()!!
        } else {
            UserResponseDto("fail", KakaoToken(""))
        }
    }

}