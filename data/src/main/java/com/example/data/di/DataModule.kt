package com.example.data.di

import com.example.data.RepositoryAPI
import com.example.entity.RepositoryAPIInterface
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideRepositoryApiInterface(repositoryAPI: RepositoryAPI): RepositoryAPIInterface {
        return repositoryAPI
    }


}