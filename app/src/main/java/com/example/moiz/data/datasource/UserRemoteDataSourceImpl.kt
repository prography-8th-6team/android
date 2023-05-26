package com.example.moiz.data.datasource

import com.example.moiz.data.network.dto.UserResponseDto
import com.example.moiz.data.network.UserService
import com.example.moiz.domain.datasource.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val userService: UserService) :
    UserRemoteDataSource {

    override suspend fun getKakaoToken(token: String): UserResponseDto =
        userService.sendKakaoToken(token)

}
