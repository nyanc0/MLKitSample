package com.sample.mlkit.android.nyanc0.mlkitsample.model

import android.net.Uri
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import androidx.core.content.FileProvider
import com.sample.mlkit.android.nyanc0.mlkitsample.BuildConfig
import com.sample.mlkit.android.nyanc0.mlkitsample.MLKitSampleApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Photo(private val file: File, private val uri: Uri) : Parcelable {

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

        private fun createUri(file: File): Uri {
            return FileProvider.getUriForFile(
                MLKitSampleApplication.applicationContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        }

        fun createPhotoFile(): File {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
            val fileName = "IMG_$timeStamp.jpg"
            val filePath = getExternalStorageDirPath("/nyanc0") + "/" + fileName
            return File(filePath)
        }

        /**
         * カメラ用のPhotoを作成
         */
        fun createPhotoForCamera(): Photo {
            val file = createPhotoFile()
            val uri = createUri(file)
            return Photo(file, uri)
        }

        /**
         * ライブラリから取得した画像用
         */
        fun createPhotoForLibraryCrop(uri: Uri): Photo {
            return Photo(File(uri.path), uri)
        }

        /**
         * キャプチャ後の画像用
         */
        fun createPhotoForCropped(file: File): Photo {
            return Photo(file, createUri(file))
        }

        /**
         * 外部レポジトリの取得
         */
        private fun getExternalStorageDirPath(makeDir: String?): String? {
            var makeDir = makeDir
            var dirPath = ""
            var photoDir: File? = null
            val extStorageDir = Environment.getExternalStorageDirectory()
            if (extStorageDir.canWrite()) {
                if (makeDir == null) {
                    makeDir = ""
                }
                photoDir = File(extStorageDir.path + makeDir)
            }

            if (photoDir != null) {
                if (!photoDir.exists() && !photoDir.mkdirs()) {
                    return null
                }
                if (photoDir.canWrite()) {
                    dirPath = photoDir.path
                }
            }
            return dirPath
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