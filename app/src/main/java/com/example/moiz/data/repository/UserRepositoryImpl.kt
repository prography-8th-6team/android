package com.example.moiz.data.repository

import com.example.moiz.data.network.NetworkResult
import com.example.moiz.data.network.dto.KakaoToken
import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.domain.datasource.UserRemoteDataSource
import com.example.moiz.domain.model.UserEntity
import com.example.moiz.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userRemoteDataSource: UserRemoteDataSource) :
    UserRepository {
    override suspend fun loginByKakaoToken(token: String): UserResponseDto {
        return userRemoteDataSource.getKakaoToken(token)
    }

}