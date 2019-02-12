package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityMainBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Photo
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.*
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.bottomsheet.BottomSheetFragment
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.crop.CropActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), BottomSheetFragment.OnItemSelectedListener, CoroutineScope, AdapterView.OnItemSelectedListener {

    /** カメラ/ライブラリから取得した写真 */
    private lateinit var tmpPhoto: Photo
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        binding.selectImageBtn.setOnClickListener {
            showBottomSheet()
        }

        binding.detectorSpinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Detector.createTitleList())
                    .apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
        binding.detectorSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(item: ImageSelection) {
        when (item) {
            ImageSelection.PHOTO -> {
                startCamera()
            }
            ImageSelection.LIBRARY -> {
                startLibrary()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // nothing to do
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    CropActivity.startForResult(this, tmpPhoto)
                }
            }
            REQUEST_CHOOSE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        tmpPhoto = Photo.createPhoto(it)
                        CropActivity.startForResult(this, tmpPhoto)
                    }
                }
            }
            CropActivity.REQUEST_CD -> {
                if (resultCode == Activity.RESULT_OK) {
                    val croppedPhoto: Photo = data!!.getParcelableExtra(CropActivity.KEY_RESULT_INTENT)
                    Glide.with(binding.mainImage.context).load(croppedPhoto.fileUri).into(binding.mainImage)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CAMERA -> {
                if (verifyGrantResults(grantResults)) {
                    startCamera()
                }
            }
            PERMISSION_CHOOSE_IMAGE -> {
                if (verifyGrantResults(grantResults)) {
                    startLibrary()
                }
            }
        }
    }

    /**
     * BottomSheetを表示する
     */
    private fun showBottomSheet() {
        BottomSheetFragment.newInstance().show(supportFragmentManager, BottomSheetFragment.TAG)
    }

    /**
     * 権限チェック
     */
    private fun checkCameraPermission(requestCode: Int): Boolean {
        if (!isAuthrized(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
            if (!showRationale(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
                requestPermission(Permission.P_WRITE_EXTERNAL_STORAGE, requestCode, this)
            } else {
                Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT).show()
            }
            return false
        }
        return true
    }

    /**
     * カメラ起動
     */
    private fun startCamera() {
        if (!checkCameraPermission(PERMISSION_CAMERA)) {
            return
        }

        tmpPhoto = Photo.createPhoto(TMP_PHOTO_PREFIX).apply {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, this.fileUri)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    /**
     * ライブラリ起動
     */
    private fun startLibrary() {

        if (!checkCameraPermission(PERMISSION_CHOOSE_IMAGE)) {
            return
        }

        val intent = Intent().apply {
            type = "image/jpeg"
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CHOOSE_IMAGE)
    }

    companion object {
        const val PERMISSION_CAMERA = 1000
        const val PERMISSION_CHOOSE_IMAGE = 1001
        const val REQUEST_CAMERA = 1002
        const val REQUEST_CHOOSE_IMAGE = 1003
        const val TMP_PHOTO_PREFIX = "nyanc0_"
    }
}