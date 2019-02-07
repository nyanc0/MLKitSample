package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common

import android.graphics.Rect

sealed class Graphic

data class BoxGraphic(val text: String, val boundingBox: Rect) : Graphic()
data class TextGraphoc(val texts: List<String>) : Graphic()