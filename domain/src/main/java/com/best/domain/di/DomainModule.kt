package com.best.domain.di


import com.best.domain.ApiDataUseCase
import com.best.domain.RoomUseCase
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.RoomUseCaseInterface
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideAllFilmsUseCaseInterface(allFilmsUseCase: ApiDataUseCase): ApiDataUseCaseInterface {
        return allFilmsUseCase
    }

    @Provides
    fun provideRoomUseCaseInterface(roomUseCase: RoomUseCase): RoomUseCaseInterface {
        return roomUseCase
    }

}