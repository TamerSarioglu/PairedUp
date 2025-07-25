package com.tamersarioglu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PairedUpApp: Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Keep application startup simple and fast
        // Locale configuration will be handled per-activity
    }
}