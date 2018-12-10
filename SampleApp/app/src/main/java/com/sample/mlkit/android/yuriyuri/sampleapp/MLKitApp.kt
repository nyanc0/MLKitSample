package com.sample.mlkit.android.yuriyuri.sampleapp

import android.support.v7.app.AppCompatDelegate
import com.sample.mlkit.android.yuriyuri.sampleapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class MLKitApp : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
                .application(this)
                .build()
    }
}