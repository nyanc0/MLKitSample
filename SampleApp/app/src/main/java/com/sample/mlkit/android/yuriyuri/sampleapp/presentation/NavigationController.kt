package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.app.Activity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio.BioActivity

class NavigationController {

//    fun navigateToCameraActivityForResult() {
//        CameraActivity.startForResult(activity)
//    }

//    fun navigateToCropActivityForResult(byteArray: ByteArray) {
//        CropActivity.startForResult(activity, byteArray)
//    }

//    fun navigateToCropActivityForResult(uri: Uri) {
//        CropActivity.startForResult(activity, uri)
//    }


    fun navigateToBioActivity(activity: Activity) {
        BioActivity.start(activity)
    }

}