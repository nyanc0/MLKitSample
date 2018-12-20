package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var navigationController: NavigationController

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CameraActivity.REQUEST_CD -> startCrop(resultCode, data)
            CropActivity.REQUEST_CD -> setImage(resultCode, data)
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab -> navigationController.navigateToCameraActivityForResult()
        }
    }

    /**
     * トリミング画面起動
     */
    private fun startCrop(resultCd: Int, data: Intent?) {
        if (resultCd != Activity.RESULT_OK || data == null) return

        val uri = data.getParcelableExtra<Uri>(CameraActivity.KEY_INTENT)
        navigationController.navigateToCropActivityForResult(uri)
    }

    private fun setImage(resultCd: Int, data: Intent?) {
        if (resultCd != Activity.RESULT_OK || data == null) return

        val uri = data.getParcelableExtra<Uri>(CropActivity.KEY_INTENT)
        Glide.with(this).load(uri).into(binding.image)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
