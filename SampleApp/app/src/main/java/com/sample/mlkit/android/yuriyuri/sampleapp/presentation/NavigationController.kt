package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: AppCompatActivity) {

    fun navigateToCameraActivity() {
        CameraActivity.start(activity)
    }

    fun navigateToCameraActivityForResult() {
        CameraActivity.startForResult(activity)
    }

}