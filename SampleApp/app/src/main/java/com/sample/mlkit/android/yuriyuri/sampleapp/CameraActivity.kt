package com.sample.mlkit.android.yuriyuri.sampleapp

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityCameraBinding
import dagger.android.support.DaggerAppCompatActivity

class CameraActivity : DaggerAppCompatActivity(), View.OnClickListener {

    // https://github.com/google/cameraview/blob/master/demo/src/main/java/com/google/android/cameraview/demo/MainActivity.java

    private val binding: ActivityCameraBinding by lazy {
        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.take_picture -> binding.camera.takePicture()
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }
    }

}