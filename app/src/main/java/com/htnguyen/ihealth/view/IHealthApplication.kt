package com.htnguyen.ihealth.view

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContextWrapper
import android.os.Build
import com.htnguyen.ihealth.di.viewModelModule
import com.htnguyen.ihealth.helper.PrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class IHealthApplication : Application() {

    companion object {
        lateinit var mInstance: IHealthApplication
    }

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        PrefsHelper.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(getPackageName())
            .setUseDefaultSharedPreference(true)
            .build();

        createNotificationChannel()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@IHealthApplication)

            modules(
                listOf(viewModelModule)
            )
        }
        mInstance = this
    }

    fun setCurrentActivity(currentActivity: Activity) {
        this.currentActivity = currentActivity
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "CHANNEL_ID",
                "Contact Tracing Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }


}