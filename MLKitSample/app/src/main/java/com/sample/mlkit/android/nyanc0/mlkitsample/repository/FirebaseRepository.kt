package com.sample.mlkit.android.nyanc0.mlkitsample.repository

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.BoxGraphic
import com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common.Graphic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository(override val coroutineContext: CoroutineContext) : CoroutineScope {

    fun async1(bitmap: Bitmap) = async(Dispatchers.Default) {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        return@async suspendCoroutine<Result<MutableList<Graphic>>> { cont ->
            FirebaseVision.getInstance()
                .onDeviceTextRecognizer
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
                    cont.resumeWith(kotlin.Result.failure(exception))
                }
        }
    }
}