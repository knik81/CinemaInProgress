package com.example.domain.di


import com.example.domain.ApiDataUseCase
import com.example.entity.ApiDataUseCaseInterface
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideAllFilmsUseCaseInterface(allFilmsUseCase: ApiDataUseCase): ApiDataUseCaseInterface {
        return allFilmsUseCase
    }



}