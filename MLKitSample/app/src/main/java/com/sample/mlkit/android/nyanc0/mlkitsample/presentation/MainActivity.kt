package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityMainBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.Permission
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.isAuthrized
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.requestPermission
import com.sample.mlkit.android.nyanc0.mlkitsample.permission.showRationale
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.bottomsheet.BottomSheetFragment
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.createFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), BottomSheetFragment.OnItemSelectedListener, CoroutineScope {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private lateinit var path: String
    private lateinit var uri: Uri
    lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val permissions = arrayOf(
        Permission.P_WRITE_EXTERNAL_STORAGE,
        Permission.P_READ_EXTERNAL_STORAGE
    )

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.selectImageBtn.setOnClickListener {
            showBottomSheet()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_CAMERA -> {

            }
        }
    }

    private fun showBottomSheet() {
        BottomSheetFragment.newInstance().show(supportFragmentManager, BottomSheetFragment.TAG)
    }

    /**
     * カメラ起動
     */
    private fun startCamera() {
        if (!isAuthrized(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
            if (!showRationale(Permission.P_WRITE_EXTERNAL_STORAGE, this)) {
                requestPermission(Permission.P_WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION, this)
            } else {
                Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT).show()
            }
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, createSaveFileUri())
        startActivityForResult(intent, RESULT_CAMERA)
    }

    private fun startLibrary() {

    }

    /**
     * 保存先ファイル作成
     */
    private fun createSaveFileUri(): Uri {
        val file = createFile("nyanc0_")
        path = file.absolutePath
        Log.d("debug", "filePath:$path")
        uri = FileProvider.getUriForFile(this, application.packageName + ".fileprovider", file)
        return uri
    }

    companion object {
        val REQUEST_PERMISSION = 1000
        val RESULT_CAMERA = 1001
    }
}