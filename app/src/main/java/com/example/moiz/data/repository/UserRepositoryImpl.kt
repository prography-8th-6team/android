package com.example.moiz.data.repository

import com.example.moiz.data.network.dto.KakaoToken
import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.data.network.service.UserService
import com.example.moiz.domain.repository.UserRepository
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