package com.sample.mlkit.android.nyanc0.mlkitsample

import android.app.Application
import android.content.Context

class MLKitSampleApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MLKitSampleApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}