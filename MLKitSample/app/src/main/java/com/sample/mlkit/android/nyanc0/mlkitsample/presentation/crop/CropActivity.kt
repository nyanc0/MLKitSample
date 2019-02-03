package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.crop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityCropBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.File
import kotlin.coroutines.CoroutineContext

class CropActivity : AppCompatActivity(), CropCallback, LoadCallback, View.OnClickListener, CoroutineScope {

    lateinit var loadUri: Uri
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var file : File

    private val binding: ActivityCropBinding by lazy {
        DataBindingUtil.setContentView<ActivityCropBinding>(this, R.layout.activity_crop)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadUri = intent.getParcelableExtra(KEY_INTENT)
        binding.cropImage.load(loadUri).execute(this)
        binding.rollBtn.setOnClickListener(this)
        binding.cropImage.setOnClickListener(this)
    }

    /**
     * {@link LoadCallback}
     */
    override fun onSuccess() {
        // do nothing
    }

    override fun onSuccess(cropped: Bitmap?) {
        if (cropped == null) return

    }

    override fun onError(e: Throwable?) {
        // TODO:show Error
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.crop_btn -> {
                cropImage()
            }
        }
    }

    private fun saveImage(cropped: Bitmap?) {

    }

    /**
     * 画像をトリミング
     */
    private fun cropImage() {
        binding.cropImage.crop(loadUri).execute(this)
    }

    companion object {
        const val REQUEST_CD = 2000
        const val KEY_INTENT = "key_crop_image"

        fun startForResult(activity: Activity, uri: Uri) {
            val intent = Intent(activity, CropActivity::class.java)
            intent.putExtra(KEY_INTENT, uri)
            activity.startActivityForResult(intent, REQUEST_CD)
        }
    }
}