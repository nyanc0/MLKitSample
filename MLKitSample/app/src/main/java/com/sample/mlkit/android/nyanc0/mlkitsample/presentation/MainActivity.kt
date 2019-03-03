package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityMainBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.*
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.bottomsheet.BottomSheetFragment
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.createFile
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.createUri
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.crop.CropActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), BottomSheetFragment.OnItemSelectedListener, CoroutineScope,
    AdapterView.OnItemSelectedListener {

    /** 選択中のDetector */
    private var selectedDetector: Detector = Detector.TEXT_DETECTION
    /** カメラで撮影したURI */
    private lateinit var cameraUri: Uri

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()

        binding.selectImageBtn.setOnClickListener {
            showBottomSheet()
        }
        binding.detectorSpinner.onItemSelectedListener = this
        binding.detectorSpinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Detector.createTitleList())
                    .apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }

        binding.detectBtn.setOnClickListener {
            viewModel.detect(selectedDetector)
        }

        // Bitmap
        viewModel.bitmap.observe(this, Observer { bitmap ->
            bitmap?.let {
                binding.mainImage.setImageBitmap(bitmap)
                binding.overlay.targetWidth = bitmap.width
                binding.overlay.targetHeight = bitmap.height
            }
        })

        // 解析結果
        viewModel.graphics.observe(this, Observer { graphics ->
            binding.overlay.clear()
            graphics?.let {
                for (graphic in it) {
                    overlay.add(graphic)
                }
            }
        })
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
        selectedDetector = Detector.getDetector(binding.detectorSpinner.selectedItem as String)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // nothing to do
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    CropActivity.startForResult(this, cameraUri)
                }
            }
            REQUEST_CHOOSE_IMAGE -> {
                data?.data?.let {
                    CropActivity.startForResult(this, it)
                }
            }
            CropActivity.REQUEST_CD -> {
                if (resultCode == Activity.RESULT_OK) {
                    val croppedUri: Uri = data!!.getParcelableExtra(CropActivity.KEY_RESULT_INTENT)
                    launch {
                        binding.overlay.clear()
                        viewModel.setBitmap(croppedUri, binding.mainImage.width, binding.mainImage.height)
                    }
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
        cameraUri = createUri(createFile())
        val intent = Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            addCategory(Intent.CATEGORY_DEFAULT)
            this.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        }

        startActivityForResult(intent, REQUEST_CAMERA)
    }

    /**
     * ライブラリ起動
     */
    private fun startLibrary() {

        if (!checkCameraPermission(PERMISSION_CHOOSE_IMAGE)) {
            return
        }

        val intent = Intent().apply {
            type = "image/*"
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
    }
}