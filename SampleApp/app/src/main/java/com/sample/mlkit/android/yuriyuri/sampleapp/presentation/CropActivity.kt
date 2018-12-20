package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityCropBinding
import com.sample.mlkit.android.yuriyuri.sampleapp.util.createPreCroppedFileName
import com.sample.mlkit.android.yuriyuri.sampleapp.util.createTimeStamp
import com.sample.mlkit.android.yuriyuri.sampleapp.util.deletePreFile
import dagger.android.support.DaggerAppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CropActivity : DaggerAppCompatActivity(), LoadCallback, CropCallback, View.OnClickListener {

    private val binding: ActivityCropBinding by lazy {
        DataBindingUtil.setContentView<ActivityCropBinding>(this, R.layout.activity_crop)
    }

    private lateinit var loadUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUri = intent.getParcelableExtra(KEY_INTENT)
        binding.cropImage.load(loadUri).execute(this)
        binding.cropBtn.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.crop_btn -> cropImage()
        }
    }

    /**
     * 画像トリミング
     */
    private fun cropImage() {
        binding.cropImage.crop(loadUri).execute(this)
    }

    /**
     * CropImageView#LoadCallback
     */
    override fun onSuccess() {
    }

    /**
     * CropImageView#CropCallback
     */
    override fun onSuccess(cropped: Bitmap?) {
        if (cropped == null) return
        val outputStream = ByteArrayOutputStream()
        cropped.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        val timeStamp = createTimeStamp(Date().time)
        val saveFileName = createPreCroppedFileName(timeStamp)
        val saveFile = File(externalCacheDir, saveFileName)

        val fileOutputStream = FileOutputStream(saveFile)
        fileOutputStream.write(outputStream.toByteArray())
        fileOutputStream.close()

        deletePreFile(File(loadUri.path))

        val intent = Intent()
        intent.putExtra(KEY_INTENT, Uri.fromFile(saveFile))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onError(e: Throwable?) {
        Log.e("CAMERA_ERROR", e!!.message)
    }

    companion object {

        const val REQUEST_CD = 300
        const val KEY_INTENT = "key_crop_image"

        fun startForResult(activity: Activity, uri: Uri) {
            val intent = Intent(activity, CropActivity::class.java)
            intent.putExtra(KEY_INTENT, uri)
            activity.startActivityForResult(intent, REQUEST_CD)
        }
    }
}