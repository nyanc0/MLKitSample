package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.graphics.Bitmap
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.Graphic
import com.sample.mlkit.android.nyanc0.mlkitsample.repository.Result

class MainViewModel : ViewModel() {

    private val mutableGraphic: MutableLiveData<Result<Graphic>> = MutableLiveData()
    val result: LiveData<Result<Graphic>> = mutableGraphic

    private val mutableBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    val bitmap: LiveData<Bitmap> = mutableBitmap

    @UiThread
    fun detect(detector: Detector) {


    }
}