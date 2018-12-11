package com.sample.mlkit.android.yuriyuri.sampleapp

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityCameraBinding
import dagger.android.support.DaggerAppCompatActivity

class CameraActivity : DaggerAppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
    }

    override fun onStart() {
        super.onStart()
        binding.cameraView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.cameraView.onResume()
    }

    override fun onPause() {
        binding.cameraView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.cameraView.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }
    }

}