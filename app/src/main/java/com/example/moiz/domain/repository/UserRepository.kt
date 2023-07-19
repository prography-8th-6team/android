package com.example.moiz.domain.repository

import com.example.moiz.data.network.dto.UserProfileDto
import com.example.moiz.data.network.dto.UserInfoResponseDto

interface UserRepository {
    suspend fun loginByKakaoToken(token: String): UserInfoResponseDto

    suspend fun getUserProfile(token: String, id: Int): UserProfileDto
}