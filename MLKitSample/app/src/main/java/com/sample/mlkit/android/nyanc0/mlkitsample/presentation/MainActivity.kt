package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.mlkit.android.nyanc0.mlkitsample.R
import com.sample.mlkit.android.nyanc0.mlkitsample.databinding.ActivityMainBinding
import com.sample.mlkit.android.nyanc0.mlkitsample.model.ImageSelection

class MainActivity : AppCompatActivity(), BottomSheetFragment.OnItemSelectedListener {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.selectImageBtn.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        BottomSheetFragment.newInstance().show(supportFragmentManager, BottomSheetFragment.TAG)
    }

    override fun onItemSelected(item: ImageSelection) {

    }
}