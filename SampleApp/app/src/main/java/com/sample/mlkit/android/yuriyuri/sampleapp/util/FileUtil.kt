package com.sample.mlkit.android.yuriyuri.sampleapp.util

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.sample.mlkit.android.yuriyuri.sampleapp.model.MediaFile
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * 一時保存先のファイルを作成
 */
fun createTmpFile(filePath: String, bitmap: Bitmap): File {
    val tempFile = File(filePath)
    val fileOutputStream = FileOutputStream(tempFile)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    fileOutputStream.flush()
    fileOutputStream.close()
    return tempFile
}

/**
 * 画像のパスを取得する.
 */
fun getExternalStorageDirPath(dir: String?): String? {
    var makeDir = dir
    var dirPath = ""
    var photoDir: File? = null

    // 外部保存先のパス
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


fun createMediaFile(): MediaFile {

    val timeStamp = Date().time
    val title = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(timeStamp)
    val fileName = "IMG_$title.jpg"
    val path = getExternalStorageDirPath("/MLKitSample") + "/" + fileName

    return MediaFile(
            0,
            title,
            fileName,
            path,
            timeStamp,
            "",
            0
    )
}

fun createTimeStamp(time: Long): String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(time)

fun createPreCroppedFileName(title: String): String = "IMG_" + title + "_pre_cropped" + ".jpg"

fun createSaveFile(fileName: String, file: File) = File(file, fileName)

fun createSaveFileName(title: String): String = "IMG_" + title + "_cropped" + ".jpg"

fun isFileExist(file: File) = file.exists()

fun deletePreFile(file: File) = {
    if (isFileExist(file)) {
        file.delete()
    }
}


fun createPhotoUri() {

}

/**
 * メディアに情報を保存し、URIを返す.
 */
fun createMediaUri(contentResolver: ContentResolver, mediaFile: MediaFile) {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, mediaFile.title)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, mediaFile.fileName)
    values.put(MediaStore.Images.Media.DATA, mediaFile.path)

    if (mediaFile.timeStamp > 0) {
        values.put(MediaStore.Images.Media.DATE_TAKEN, mediaFile.timeStamp)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, mediaFile.timeStamp / 1000)
    }
    values.put(MediaStore.Images.Media.MIME_TYPE, mediaFile.mineType)
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}

/**
 * サムネイルを作成
 */
fun createThumbnail(contentResolver: ContentResolver, imageId: Long) {
    MediaStore.Images.Thumbnails.getThumbnail(contentResolver, imageId,
            MediaStore.Images.Thumbnails.MINI_KIND, null)
}