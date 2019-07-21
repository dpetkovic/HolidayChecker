package com.dekisolutions.holidaychecker

import android.app.Application
import com.dekisolutions.holidaychecker.di.dataModule
import com.dekisolutions.holidaychecker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class HolidayCheckerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@HolidayCheckerApplication)
            modules(listOf(viewModelModule, dataModule))
        }
    }
}