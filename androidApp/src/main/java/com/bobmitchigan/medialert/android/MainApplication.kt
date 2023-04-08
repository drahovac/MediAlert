package com.bobmitchigan.medialert.android

import android.app.Application
import com.bobmitchigan.medialert.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}