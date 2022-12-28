package com.htnguyen.ihealth.view

import android.app.Activity
import android.app.Application
import com.htnguyen.ihealth.di.viewModelModule
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
}