package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun deleteExistFile(file: File) {
    if (isFileExist(file)) {
        file.delete()
    }
}

fun isFileExist(file: File): Boolean {
    return file.exists()
}