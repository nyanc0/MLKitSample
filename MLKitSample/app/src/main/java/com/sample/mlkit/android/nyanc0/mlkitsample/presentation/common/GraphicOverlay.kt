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
    var targetWidth = 0
    var targetHeight = 0
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val offsetX = (canvas.width - targetWidth) * 0.25f
        val offsetY = (canvas.height - targetHeight) * 0.25f

        synchronized(lock) {
            for (graphic in graphics) {
                when (graphic) {
                    is BoxGraphic -> {
                        rect.set(graphic.boundingBox)
                        rect.offset(offsetX, offsetY)
                        canvas.drawRect(rect, rectPaint)

                        if (graphic.text.isNotEmpty()) {
                            canvas.drawText(graphic.text, rect.left, rect.bottom, textPaint)
                        }
                    }
                    is TextGraphic -> {
                        val offset = textPaint.textSize * 1.5f
                        val left = textPaint.textSize * 0.5f
                        var bottom = offset
                        for (text in graphic.texts) {
                            if (text.isNotEmpty()) {
                                canvas.drawText(text, left, bottom, textPaint)
                                bottom += offset
                            }
                        }
                    }
                }
            }
        }
    }
}