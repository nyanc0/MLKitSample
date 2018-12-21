package com.sample.mlkit.android.yuriyuri.sampleapp.data.repository

import android.net.Uri
import android.support.annotation.CheckResult

interface ImageRepository {

    @CheckResult
    fun saveImage(quality: Int): Uri

}