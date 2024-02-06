package com.example.testcinema

import android.app.Application
import com.example.testcinema.di.AppComponent
import com.example.testcinema.di.DaggerAppComponent


class App: Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .build()
    }
}