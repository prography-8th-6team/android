package com.example.moiz.domain.datasource

import com.example.moiz.data.network.NetworkResult
import com.example.moiz.data.network.dto.UserResponseDto

interface UserRemoteDataSource {
    suspend fun getKakaoToken(token: String): UserResponseDto
}