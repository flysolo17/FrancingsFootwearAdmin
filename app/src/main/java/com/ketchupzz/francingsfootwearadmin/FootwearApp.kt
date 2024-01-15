package com.ketchupzz.francingsfootwearadmin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FootwearApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}