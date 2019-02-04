package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.crop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityCropBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CropActivity : AppCompatActivity(), CropCallback, LoadCallback, View.OnClickListener, CoroutineScope {

    private lateinit var temPhoto: Photo
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var croppedPhoto: Photo

    private val binding: ActivityCropBinding by lazy {
        DataBindingUtil.setContentView<ActivityCropBinding>(this, R.layout.activity_crop)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        // トリミングする写真を取得
        temPhoto = intent.getParcelableExtra(KEY_INTENT)

        Log.d("MLKit debug", "tempPhoto Intent:" + temPhoto.filePath)
        Log.d("MLKit debug", "tempPhoto uri:" + temPhoto.fileUri.encodedPath)
        binding.cropImage.load(temPhoto.fileUri).execute(this)
        binding.rollBtn.setOnClickListener(this)
        binding.cropBtn.setOnClickListener(this)
    }

    /**
     * {@link LoadCallback}
     */
    override fun onSuccess() {
        // do nothing
    }

    override fun onSuccess(cropped: Bitmap?) {
        if (cropped == null) return
        saveImage(cropped)
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
        launch {
//            croppedPhoto = withContext(Dispatchers.Default) {

//                val byteArrayOutputSystem = ByteArrayOutputStream()
//                cropped!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputSystem)
//                byteArrayOutputSystem.close()
//
//                // トリミング前の写真を削除
//                deleteExistFile(temPhoto.file)
//
//                // トリミングした写真を保存
//                croppedPhoto = Photo(Photo.createTmpFile("nyanc0_cropped_"))
//                val fileOutputStream = FileOutputStream(croppedPhoto.file)
//                fileOutputStream.write(byteArrayOutputSystem.toByteArray())
//                fileOutputStream.close()
//            }
//
//            Log.d("MLKit debug", "croppedUri" + croppedUri.encodedPath)
//
//            val intent = Intent()
//            intent.putExtra(KEY_RESULT_INTENT, croppedUri)
//            setResult(Activity.RESULT_OK)
//            finish()
        }
    }

    /**
     * 画像をトリミング
     */
    private fun cropImage() {
        binding.cropImage.crop(temPhoto.fileUri).execute(this)
    }

    companion object {
        const val REQUEST_CD = 2000
        const val KEY_INTENT = "key_crop_image"
        const val KEY_RESULT_INTENT = "key_cropped_image"

        fun startForResult(activity: Activity, photo: Photo) {
            val intent = Intent(activity, CropActivity::class.java)
            intent.putExtra(KEY_INTENT, photo)
            activity.startActivityForResult(intent, REQUEST_CD)
        }
    }
}