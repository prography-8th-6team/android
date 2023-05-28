package com.example.moiz.domain.repository

import com.example.moiz.data.network.dto.UserResponseDto

interface UserRepository{
    suspend fun loginByKakaoToken(token: String): UserResponseDto
}