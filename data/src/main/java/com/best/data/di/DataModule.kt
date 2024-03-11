package com.best.data.di

import com.best.data.RepositoryAPI
import com.best.entity.RepositoryAPIInterface
import dagger.Module
import dagger.Provides

@Module
object DataModule1 {


    @Provides
    fun provideRepositoryApiInterface(repositoryAPI: RepositoryAPI): RepositoryAPIInterface {
        return repositoryAPI
    }





}