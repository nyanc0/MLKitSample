package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.crop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityCropBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Photo
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.deleteExistFile
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

class CropActivity : AppCompatActivity(), CropCallback, LoadCallback, View.OnClickListener, CoroutineScope {

    /** トリミング前画像 */
    private lateinit var temPhoto: Photo
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val binding: ActivityCropBinding by lazy {
        DataBindingUtil.setContentView<ActivityCropBinding>(this, R.layout.activity_crop)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        // トリミングする写真を取得
        temPhoto = intent.getParcelableExtra(KEY_INTENT)

        binding.cropImage.load(temPhoto.fileUri).execute(this)
        binding.rotationBtn.setOnClickListener(this)
        binding.cropBtn.setOnClickListener(this)
    }

    override fun onSuccess() {
        // do nothing
    }

    override fun onSuccess(cropped: Bitmap?) {
        if (cropped == null) return
        returnCroppedPhoto(cropped)
    }

    override fun onError(e: Throwable?) {
        Toast.makeText(this, resources.getString(R.string.cropped_error), Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.crop_btn -> {
                cropImage()
            }
            R.id.rotation_btn -> {
                rotateImage()
            }
        }
    }

    /**
     * 画像トリミング
     */
    private fun cropImage() {
        binding.cropImage.crop(temPhoto.fileUri).execute(this)
    }

    /**
     * 画像を回転
     */
    private fun rotateImage() {
        binding.cropImage.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
    }

    /**
     * 画像をトリミングして終了する.
     *
     * @param cropped Bitmap
     */
    private fun returnCroppedPhoto(cropped: Bitmap?) = launch(Dispatchers.Main) {
        val savedPhoto: Photo = saveImage(cropped).await()

        val intent = Intent()
        intent.putExtra(KEY_RESULT_INTENT, savedPhoto)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * トリミング画像を保存する.
     *
     * @param cropped Bitmap
     */
    private fun saveImage(cropped: Bitmap?) = async(Dispatchers.Default) {

        val byteArrayOutputSystem = ByteArrayOutputStream()
        cropped!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputSystem)
        byteArrayOutputSystem.close()

        val saveFile = Photo.createTmpFile(PHOTO_PREFIX)
        val fileOutputStream = FileOutputStream(saveFile)
        fileOutputStream.write(byteArrayOutputSystem.toByteArray())
        fileOutputStream.close()

        // トリミング前の写真を削除
        deleteExistFile(temPhoto.photoFile)

        return@async Photo.createPhoto(saveFile)
    }

    companion object {
        const val REQUEST_CD = 2000
        const val KEY_INTENT = "key_crop_image"
        const val KEY_RESULT_INTENT = "key_cropped_image"
        const val PHOTO_PREFIX = "cropped_"

        fun startForResult(activity: Activity, photo: Photo) {
            val intent = Intent(activity, CropActivity::class.java)
            intent.putExtra(KEY_INTENT, photo)
            activity.startActivityForResult(intent, REQUEST_CD)
        }
    }
}