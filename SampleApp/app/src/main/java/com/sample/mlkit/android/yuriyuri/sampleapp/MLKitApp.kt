package com.sample.mlkit.android.yuriyuri.sampleapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

open class MLKitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}