package com.example.moiz.data

import com.example.moiz.data.network.service.TravelService
import com.example.moiz.data.network.service.UserService
import com.example.moiz.data.repository.TravelRepositoryImpl
import com.example.moiz.data.repository.UserRepositoryImpl
import com.example.moiz.domain.repository.TravelRepository
import com.example.moiz.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository =
        UserRepositoryImpl(userService)

    @Provides
    @Singleton
    fun provideTravelRepository(travelService: TravelService): TravelRepository =
        TravelRepositoryImpl(travelService)

}