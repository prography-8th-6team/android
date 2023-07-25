package com.jerny.moiz.data.repository

import com.jerny.moiz.data.network.dto.UserInfoDto
import com.jerny.moiz.data.network.dto.UserProfileDto
import com.jerny.moiz.data.network.dto.UserInfoResponseDto
import com.jerny.moiz.data.network.service.UserService
import com.jerny.moiz.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) : UserRepository {
    override suspend fun loginByKakaoToken(token: String): UserInfoResponseDto {
        val temp = userService.sendKakaoToken(token)
        return if (temp.isSuccessful) {
            temp.body()!!
        } else {
            UserInfoResponseDto("fail", UserInfoDto("", "", 0, ""))
        }
    }

    override suspend fun getUserProfile(token: String, id: Int): UserProfileDto {
        return if (userService.getUserProfile(token, id).isSuccessful) {
            userService.getUserProfile(token, id).body()?.results!!
        } else {
            UserProfileDto(nickname = "", fcm_token = "", created = "")
        }
    }
}