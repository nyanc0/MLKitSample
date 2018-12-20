package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: AppCompatActivity) {

    fun navigateToCameraActivityForResult() {
        CameraActivity.startForResult(activity)
    }

//    fun navigateToCropActivityForResult(byteArray: ByteArray) {
//        CropActivity.startForResult(activity, byteArray)
//    }

    fun navigateToCropActivityForResult(uri: Uri) {
        CropActivity.startForResult(activity, uri)
    }

}