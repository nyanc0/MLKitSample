package com.sample.mlkit.android.yuriyuri.sampleapp.data.repository

import android.net.Uri
import android.support.annotation.CheckResult
import io.reactivex.Single
import java.io.File

interface ImageRepository {

    @CheckResult
    fun saveImage(jpeg: ByteArray?, quality: Int, externalCacheDirectory: File): Single<Uri>
}