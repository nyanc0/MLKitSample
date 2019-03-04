package com.sample.mlkit.android.nyanc0.mlkitsample.repository

import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.BoxGraphic
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.Graphic
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.TextGraphic
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Firebaseへ解析を行うRepository
 */
object FirebaseRepository {

    /**
     * 解析を実施.
     *
     * @param bitmap 解析する画像
     * @param detector 解析する内容
     * @return Result<MutableList<Graphic>>
     */
    @WorkerThread
    suspend fun startFirebase(bitmap: Bitmap, detector: Detector): Result<MutableList<Graphic>> {

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val firebaseVision = FirebaseVision.getInstance()

        return suspendCoroutine { cont ->
            when (detector) {
                Detector.TEXT_DETECTION -> {
                    firebaseVision.onDeviceTextRecognizer
                        .processImage(image)
                        .addOnSuccessListener { texts ->
                            val result = mutableListOf<Graphic>()
                            for (block in texts.textBlocks) {
                                for (line in block.lines) {
                                    for (element in line.elements) {
                                        element.boundingBox?.let {
                                            result.add(BoxGraphic(element.text, it))
                                        }
                                    }
                                }
                            }
                            cont.resume(Result.success(result))
                        }.addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
                Detector.CLOUD_TEXT_DETECTION -> {
                    val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
                        .setLanguageHints(listOf("jp"))
                        .build()
                    firebaseVision.getCloudTextRecognizer(options)
                        .processImage(image)
                        .addOnSuccessListener { texts ->
                            val result = mutableListOf<Graphic>()
                            for (block in texts.textBlocks) {
                                for (line in block.lines) {
                                    for (element in line.elements) {
                                        element.boundingBox?.let {
                                            result.add(BoxGraphic(element.text, it))
                                        }
                                    }
                                }
                            }
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
                Detector.FACE_DETECTION -> {
                    val highAccuracyOpts = FirebaseVisionFaceDetectorOptions.Builder()
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .enableTracking()
                        .build()
                    firebaseVision.getVisionFaceDetector(highAccuracyOpts)
                        .detectInImage(image)
                        .addOnSuccessListener { faces ->
                            val result = mutableListOf<Graphic>()
                            for (face in faces) {
                                face.boundingBox?.let {
                                    result.add(BoxGraphic(face.smilingProbability.toString(), it))
                                }
                            }
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
                Detector.BARCODE_DETECTION -> {
                    val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                            FirebaseVisionBarcode.FORMAT_EAN_8,
                            FirebaseVisionBarcode.FORMAT_EAN_13
                        ).build()
                    firebaseVision.getVisionBarcodeDetector(options)
                        .detectInImage(image)
                        .addOnSuccessListener { barcodes ->
                            val result = mutableListOf<Graphic>()
                            for (barcode in barcodes) {
                                barcode.boundingBox?.let {
                                    result.add(BoxGraphic(barcode.rawValue ?: "", it))
                                }
                            }
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }

                }
                Detector.LABELING -> {
                    val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build()
                    firebaseVision.getOnDeviceImageLabeler(options)
                        .processImage(image)
                        .addOnSuccessListener { labels ->
                            val result = mutableListOf<Graphic>()
                            result.add(TextGraphic(labels.map { "${it.text}, ${it.confidence}" }))
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
                Detector.CLOUD_LABELING -> {
                    val options = FirebaseVisionCloudImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build()
                    firebaseVision.getCloudImageLabeler(options)
                        .processImage(image)
                        .addOnSuccessListener { labels ->
                            val result = mutableListOf<Graphic>()
                            result.add(TextGraphic(labels.map { "${it.text}, ${it.confidence}" }))
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
                Detector.CLOUD_LANDMARK -> {
                    val options = FirebaseVisionCloudDetectorOptions.Builder()
                        .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                        .setMaxResults(15)
                        .build()

                    firebaseVision.getVisionCloudLandmarkDetector(options)
                        .detectInImage(image)
                        .addOnSuccessListener { labels ->
                            val result = mutableListOf<Graphic>()
                            labels.forEach {
                                if (it.boundingBox != null) {
                                    result.add(BoxGraphic("${it.landmark}, ${it.confidence}", it.boundingBox!!))
                                }
                                println("boundingBox : ${it.boundingBox}")
                                println("confidence : ${it.confidence}")
                                println("entityId : ${it.entityId}")
                                println("landmark : ${it.landmark}")
                                println("locations : ${it.locations[0].latitude}, ${it.locations[0].longitude}")
                            }
                            cont.resume(Result.success(result))
                        }
                        .addOnFailureListener { exception ->
                            cont.resume(Result.failure(exception.message, exception))
                        }
                }
            }
        }
    }
}