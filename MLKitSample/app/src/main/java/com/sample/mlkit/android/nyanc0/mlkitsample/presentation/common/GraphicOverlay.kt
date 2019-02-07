package com.sample.mlkit.android.nyanc0.mlkitsample.presentation.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class GraphicOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val lock = Any()
    private val graphics = mutableListOf<Graphic>()
    var previewWidth = 0
    var previewHeight = 0
    private val rect = RectF()

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2 * resources.displayMetrics.density
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 12 * resources.displayMetrics.density
    }

    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        synchronized(lock) {
            graphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic: Graphic) {
        synchronized(lock) {
            graphics.remove(graphic)
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
    }
}