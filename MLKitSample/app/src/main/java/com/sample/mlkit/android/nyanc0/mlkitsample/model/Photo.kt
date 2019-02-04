package com.sample.mlkit.android.nyanc0.mlkitsample.model

import android.net.Uri
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.FileProvider
import com.sample.mlkit.android.nyanc0.mlkitsample.MLKitSampleApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Photo(val file: File) : Parcelable {

    val photofile: File = file

    val filePath: String
        get() = file.absolutePath

    val fileUri: Uri
        get() = FileProvider.getUriForFile(
            MLKitSampleApplication.applicationContext(),
            MLKitSampleApplication.applicationContext().packageName + ".fileprovider",
            file
        )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(file)
    }

    companion object {
        private val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())

        fun createTmpFile(prefix: String): File {
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/temp")
            if (!storageDir.exists()) {
                storageDir.mkdir()
            }
            return File.createTempFile(prefix + timeStamp, ".jpg", storageDir)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(`in`: Parcel): Photo {
                return Photo(`in`.readValue(File::class.java.classLoader) as File)
            }

            override fun newArray(size: Int): Array<Photo?> {
                return arrayOfNulls(size)
            }
        }
    }
}