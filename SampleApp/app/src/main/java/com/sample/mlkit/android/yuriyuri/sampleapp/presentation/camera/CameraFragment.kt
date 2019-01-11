package com.sample.mlkit.android.yuriyuri.sampleapp.presentation.camera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.data.Result
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.ImageCameraRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.ImageRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.FragmentCameraBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.permission.Permission
import com.sample.mlkit.android.yuriyuri.sampleapp.permission.PermissionUtil
import com.sample.mlkit.android.yuriyuri.sampleapp.util.AppSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var permissionUtil: PermissionUtil
    private val schedulerProvider = AppSchedulerProvider()
    private val repository: ImageRepository = ImageCameraRepository(schedulerProvider)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(jpeg: ByteArray?) {
            super.onPictureTaken(jpeg)
            repository.saveImage(jpeg, 50, context!!.externalCacheDir)
                    .map { Result.success(it) }
                    .observeOn(schedulerProvider.ui())
                    .subscribe {
                        t: Result<Uri>? ->  
                    }.addTo(compositeDisposable)
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
        }
    }

    fun newInstance(): CameraFragment {
        return CameraFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        permissionUtil = PermissionUtil(this.activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.camera.setLifecycleOwner(this)
        binding.camera.addCameraListener(cameraListener)
        binding.takePicture.setOnClickListener {
            binding.camera.capturePicture()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
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

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
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
                    Toast.makeText(context, getText(R.string.message_no_camera_permission), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val REQUEST_CD = 200
        const val KEY_INTENT = "key_image"
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}