package com.sample.mlkit.android.yuriyuri.sampleapp

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.google.android.cameraview.CameraView
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityCameraBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.permission.Permission
import com.sample.mlkit.android.yuriyuri.sampleapp.permission.PermissionUtil
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class CameraActivity : DaggerAppCompatActivity(), View.OnClickListener {

    // https://github.com/google/cameraview/blob/master/demo/src/main/java/com/google/android/cameraview/demo/MainActivity.java

    @Inject
    lateinit var permissionUtil: PermissionUtil

    private val binding: ActivityCameraBinding by lazy {
        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
    }

    private val cameraCallback = object : CameraView.Callback() {

        override fun onCameraOpened(cameraView: CameraView?) {
        }

        override fun onCameraClosed(cameraView: CameraView?) {
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.camera.addCallback(cameraCallback)
        binding.takePicture.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        when {
            permissionUtil.isAuthorized(Permission.P_CAMERA) -> binding.camera.start()
            permissionUtil.showRationale(Permission.P_CAMERA) -> {
                // TODO:show toast or dialog
            }
            else -> permissionUtil.requestPermission(Permission.P_CAMERA, 200)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.camera.stop()
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