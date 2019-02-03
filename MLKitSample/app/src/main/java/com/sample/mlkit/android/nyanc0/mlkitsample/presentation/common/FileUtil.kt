package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun createFile(prefix: String): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
    val imageFileName = prefix + timeStamp
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/temp")
    if (!storageDir.exists()) {
        storageDir.mkdir()
    }
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}

