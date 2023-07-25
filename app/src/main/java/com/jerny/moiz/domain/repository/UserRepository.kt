package com.jerny.moiz.domain.repository

import com.jerny.moiz.data.network.dto.UserProfileDto
import com.jerny.moiz.data.network.dto.UserInfoResponseDto

interface UserRepository {
    suspend fun loginByKakaoToken(token: String): UserInfoResponseDto

    suspend fun getUserProfile(token: String, id: Int): UserProfileDto
}