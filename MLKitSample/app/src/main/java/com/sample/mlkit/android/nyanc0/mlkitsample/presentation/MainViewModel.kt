package com.sample.mlkit.android.nyanc0.mlkitsample.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.sample.mlkit.android.nyanc0.mlkitsample.MLKitSampleApplication
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.Graphic
import com.sample.mlkit.android.nyanc0.mlkitsample.repository.FirebaseRepository
import com.sample.mlkit.android.nyanc0.mlkitsample.repository.Result
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.math.max

/**
 * MainActivityのViewModel
 */
class MainViewModel : ViewModel(), CoroutineScope {

    /** 解析結果(private) */
    private val mutableGraphics: MutableLiveData<MutableList<Graphic>?> = MutableLiveData()
    /** 解析結果.ViewはこちらをObserveする */
    val graphics: LiveData<MutableList<Graphic>?> = mutableGraphics

    /** 解析結果(private) */
    private val mutableBitmap: MutableLiveData<Bitmap> = MutableLiveData()
    /** 解析結果.ViewはこちらをObserveする */
    val bitmap: LiveData<Bitmap> = mutableBitmap

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    /**
     * 解析開始.
     *
     * @param detector 解析内容
     */
    fun detect(detector: Detector) {
        bitmap.value?.let {
            launch {
                val result = withContext(Dispatchers.Default) {
                    FirebaseRepository.startFirebase(it, detector)
                }
                when (result) {
                    is Result.Success -> {
                        mutableGraphics.postValue(result.data)
                    }
                    is Result.Failure -> {
                        mutableGraphics.postValue(null)
                    }
                }
            }
        }
    }

    /**
     * Bitmapをリサイズしてセットする.<br>
     * Viewはbitmapをobserveして結果を受け取る.
     *
     * @see MainViewModel.bitmap
     * @param uri 表示する画像のURI
     * @param width 表示先ImageViewの横幅
     * @param height 表示先ImageViewの縦幅
     */
    fun setBitmap(uri: Uri, width: Int, height: Int) {
        launch {
            val result: Bitmap = resizeBitmap(uri, width, height)
            mutableBitmap.postValue(result)
        }
    }

    /**
     * Bitmapをアスペクト比を維持したままリサイズする.
     *
     * @param width 表示先ImageViewの横幅
     * @param height 表示先ImageViewの縦幅
     * @return Bitmap
     */
    private suspend fun resizeBitmap(uri: Uri, width: Int, height: Int): Bitmap =
        withContext(Dispatchers.Default) {

            val imageBitmap: Bitmap = Glide.with(MLKitSampleApplication.applicationContext())
                .asBitmap()
                .load(uri)
                .submit(width, height)
                .get()

            val scaleFactor = max(
                imageBitmap.width.toFloat() / width.toFloat(),
                imageBitmap.height.toFloat() / height.toFloat()
            )

            val targetWidth = (imageBitmap.width / scaleFactor).toInt()
            val targetHeight = (imageBitmap.height / scaleFactor).toInt()

            Bitmap.createScaledBitmap(
                imageBitmap,
                targetWidth,
                targetHeight,
                true
            )
        }
}