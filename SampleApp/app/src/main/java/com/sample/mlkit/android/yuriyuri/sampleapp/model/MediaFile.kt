package com.sample.mlkit.android.yuriyuri.sampleapp.model

data class MediaFile(
        var mediaId: Long,
        val title: String,
        val fileName: String,
        val path: String,
        val timeStamp: Long,
        var mineType: String,
        var size: Int
)