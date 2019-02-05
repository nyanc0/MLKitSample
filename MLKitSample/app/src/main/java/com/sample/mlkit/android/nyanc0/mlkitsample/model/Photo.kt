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

data class Photo(private val file: File,private val uri: Uri) : Parcelable {

    val photoFile: File = file

    val fileUri: Uri = uri

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(file)
        dest.writeValue(uri)
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

        private fun createUri(file: File): Uri {
            return FileProvider.getUriForFile(
                MLKitSampleApplication.applicationContext(),
                MLKitSampleApplication.applicationContext().packageName + ".fileprovider",
                file
            )
        }

        fun createPhoto(prefix: String) : Photo {
            val file = createTmpFile(prefix)
            val uri = createUri(file)
            return Photo(file, uri)
        }

        fun createPhoto(uri: Uri) : Photo {
            return Photo(File(uri.path), uri)
        }

        fun createPhoto(file: File) : Photo {
            return Photo(file, createUri(file))
        }

        @JvmField
        val CREATOR: Parcelable.Creator<Photo> = object : Parcelable.Creator<Photo> {
            override fun createFromParcel(`in`: Parcel): Photo {
                return Photo(
                    `in`.readValue(File::class.java.classLoader) as File
                    , `in`.readValue(Uri::class.java.classLoader) as Uri
                )
            }

            override fun newArray(size: Int): Array<Photo?> {
                return arrayOfNulls(size)
            }
        }
    }
}