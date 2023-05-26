package com.example.moiz.domain.repository

import com.example.moiz.data.network.NetworkResult
import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.domain.model.UserEntity

interface UserRepository{
    suspend fun loginByKakaoToken(token: String): UserResponseDto
}