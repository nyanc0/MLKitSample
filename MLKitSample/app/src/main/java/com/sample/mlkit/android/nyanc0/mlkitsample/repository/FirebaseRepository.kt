package com.sample.mlkit.android.nyanc0.mlkitsample.repository

import android.graphics.BitmapFactory
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Detector
import com.sample.mlkit.android.nyanc0.mlkitsample.model.Photo
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.BoxGraphic
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.Graphic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.FileInputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository(override val coroutineContext: CoroutineContext) : CoroutineScope {

    fun detect(photo: Photo, detector: Detector) = async(Dispatchers.Default) {

        val inputStream = FileInputStream(photo.photoFile)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val firebaseVision = FirebaseVision.getInstance()

        return@async suspendCoroutine<Result<MutableList<Graphic>>> { cont ->

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

                }
                Detector.LABELING -> {

                }
                Detector.CLOUD_LABELING -> {

                }
                Detector.CLOUD_LANDMARK -> {

                }
            }
        }
    }

}