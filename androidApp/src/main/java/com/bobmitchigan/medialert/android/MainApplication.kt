package com.bobmitchigan.medialert.android

import android.app.Application
import com.bobmitchigan.medialert.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApplication)
        }
    }
}