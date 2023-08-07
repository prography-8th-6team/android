package com.jerny.moiz.data

import com.jerny.moiz.data.network.service.ScheduleService
import com.jerny.moiz.data.network.service.TravelService
import com.jerny.moiz.data.network.service.UserService
import com.jerny.moiz.data.repository.ScheduleRepositoryImpl
import com.jerny.moiz.data.repository.TravelRepositoryImpl
import com.jerny.moiz.data.repository.UserRepositoryImpl
import com.jerny.moiz.domain.repository.ScheduleRepository
import com.jerny.moiz.domain.repository.TravelRepository
import com.jerny.moiz.domain.repository.UserRepository
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

    @Provides
    @Singleton
    fun provideScheduleRepository(scheduleService: ScheduleService): ScheduleRepository =
        ScheduleRepositoryImpl(scheduleService)

}