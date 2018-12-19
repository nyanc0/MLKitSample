package com.sample.mlkit.android.yuriyuri.sampleapp.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sample.mlkit.android.yuriyuri.sampleapp.R
import com.sample.mlkit.android.yuriyuri.sampleapp.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var navigationController: NavigationController

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener {
            navigationController.navigateToCameraActivityForResult()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CameraActivity.REQUEST_CD -> setImage(resultCode, data)
        }
    }

    private fun setImage(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        val byteArray = data.getByteArrayExtra(CameraActivity.KEY_INTENT)
        Glide.with(binding.image.context).load(byteArray).into(binding.image)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
