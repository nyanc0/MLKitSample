package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import com.isseiaoki.simplecropview.callback.CropCallback
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityCropBinding
import dagger.android.support.DaggerAppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File

class CropActivity : DaggerAppCompatActivity() {

    private val binding: ActivityCropBinding by lazy {
        DataBindingUtil.setContentView<ActivityCropBinding>(this, R.layout.activity_crop)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val byteArray = intent.getByteArrayExtra(CameraActivity.KEY_INTENT)
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val sdcard = Environment.getExternalStorageDirectory()
            if (sdcard != null) {
                val mediaDir = File(sdcard, "DCIM/Camera")
                if (!mediaDir.exists()) {
                    mediaDir.mkdirs()
                }
            }
            val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "Title", null)
            val uri = Uri.parse(path)

            binding.cropImage.crop(uri).execute(
                    object : CropCallback {
                        override fun onSuccess(cropped: Bitmap?) {
                            
                        }

                        override fun onError(e: Throwable?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    }
            )
        }
    }
}