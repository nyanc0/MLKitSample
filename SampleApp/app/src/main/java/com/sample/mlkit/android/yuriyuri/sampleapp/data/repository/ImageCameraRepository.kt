package com.sample.mlkit.android.yuriyuri.sampleapp.data.repository

import android.net.Uri
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.util.createPreCroppedFileName
import com.sample.mlkit.android.yuriyuri.sampleapp.util.createSaveFile
import com.sample.mlkit.android.yuriyuri.sampleapp.util.createTimeStamp
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class ImageCameraRepository @Inject constructor(
        private val appSchedulerProvider: SchedulerProvider
) : ImageRepository {

    override fun saveImage(jpeg: ByteArray?, quality: Int, externalCacheDirectory: File): Single<Uri> {
        return Single.create<Uri> {
            val timeStamp = createTimeStamp(Date().time)
            val saveFileName = createPreCroppedFileName(timeStamp)
            val saveFile = createSaveFile(saveFileName, externalCacheDirectory)
            val fileOutputStream = FileOutputStream(saveFile)
            try {
                fileOutputStream.write(jpeg)
                fileOutputStream.close()
                val uri = Uri.fromFile(saveFile)
                it.onSuccess(uri)
            } catch (e: Exception) {
                it.onError(e)
            } finally {
                fileOutputStream.close()
            }
        }.subscribeOn(appSchedulerProvider.io())
    }
}