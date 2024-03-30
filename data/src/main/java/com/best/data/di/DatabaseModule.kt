package com.best.data.di

import android.content.Context
import androidx.room.Room
import com.best.data.RepositoryROOM
import com.best.data.room.DAOInterface
import com.best.data.room.DatBase
import com.best.entity.RepositoryROOMInterface
import dagger.Module
import dagger.Provides

@Module
class DBModule(private val context: Context) {

    @Provides
    fun provideAppDatabase(context: Context): DatBase {
        return Room.databaseBuilder(
            context,
            DatBase::class.java,
            "CinemaDB"
        )//.fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideContext(): Context {
        return context
    }


    @Provides
    fun provideRepositoryROOMInterface(repositoryROOM: RepositoryROOM): RepositoryROOMInterface {
        return repositoryROOM
    }


    @Provides
    fun provideDao(dataBase: DatBase): DAOInterface //интерфейс ДАО
    {
        return dataBase.dao()
    }

    @Provides
    fun provideRepositoryROOM(dao: DAOInterface): RepositoryROOM {
        return RepositoryROOM(dao)
    }

}

