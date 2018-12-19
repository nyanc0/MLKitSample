package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.otaliastudios.cameraview.CameraListener
import com.sample.mlkit.android.yuriyuri.sampleapp.R
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

    private val binding: ActivityCameraBinding by lazy {
        DataBindingUtil.setContentView<ActivityCameraBinding>(this, R.layout.activity_camera)
    }

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
                val intent = Intent()
                intent.putExtra(KEY_INTENT, jpeg)
                setResult(Activity.RESULT_OK, intent)
                finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.camera.setLifecycleOwner(this)
        binding.camera.addCameraListener(cameraListener)
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

    override fun onDestroy() {
        super.onDestroy()
        binding.camera.destroy()
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
            R.id.take_picture -> binding.camera.capturePicture()
        }
    }

    companion object {

        const val REQUEST_CD = 200
        const val KEY_INTENT = "key_image"

        fun start(context: Context) {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }

        fun startForResult(activity: Activity) {
            val intent = Intent(activity, CameraActivity::class.java)
            activity.startActivityForResult(intent, REQUEST_CD)
        }
    }
}