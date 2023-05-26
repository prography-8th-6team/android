package com.example.moiz.data

import com.example.moiz.data.datasource.UserRemoteDataSourceImpl
import com.example.moiz.data.network.UserService
import com.example.moiz.data.repository.UserRepositoryImpl
import com.example.moiz.domain.datasource.UserRemoteDataSource
import com.example.moiz.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserRepository(userRemoteDataSource: UserRemoteDataSource): UserRepository =
        UserRepositoryImpl(userRemoteDataSource)

    @Provides
    @Singleton
    fun providesUserRemoteDataSource(userService: UserService): UserRemoteDataSource =
        UserRemoteDataSourceImpl(userService)
}