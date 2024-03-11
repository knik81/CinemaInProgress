package com.best.nikflix

import android.app.Application
import com.best.data.di.DBModule
import com.best.nikflix.di.AppComponent
import com.best.nikflix.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .dBModule(DBModule(this))
            .build()

        //отключение Crashlytics на версии отладки
        //FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotification()

         */



    }

    //уведомление
    /*
    private fun createNotification() {
        val nameNotification = "NikFlix"
        val descriptionTextNotification = "Информирование"
        val importanceNotification = NotificationManager.IMPORTANCE_DEFAULT


        val channelNotification =
            NotificationChannel(channelId, nameNotification, importanceNotification).apply {
                description = descriptionTextNotification
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channelNotification)
    }

    companion object{
        const val channelId = "netflix_channel_id"
    }

     */
}