package com.sample.mlkit.android.yuriyuri.sampleapp

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
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
    private val REQUEST_CAMERA_PERMISSION = 1
    private lateinit var backgroundHandler: Handler

    private val binding: ActivityCameraBinding by lazy {
        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
    }

    private val cameraCallback = object : CameraView.Callback() {

        override fun onCameraOpened(cameraView: CameraView?) {
        }

        override fun onCameraClosed(cameraView: CameraView?) {
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
            getBackgroundHandler().post {
                
            }
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
            else -> permissionUtil.requestPermission(Permission.P_CAMERA, REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.camera.stop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (!permissionUtil.verifyGrantResults(grantResults)) {
                    Toast.makeText(this, R.string.message_no_camera_permission, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.take_picture -> binding.camera.takePicture()
        }
    }

    private fun getBackgroundHandler(): Handler {
        val thread = HandlerThread("background")
        thread.start()
        backgroundHandler = Handler(thread.looper)
        return backgroundHandler
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }
    }

}