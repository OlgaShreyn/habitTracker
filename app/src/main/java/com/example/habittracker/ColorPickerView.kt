package com.example.habittracker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout


class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var canvasBackgroundColor = Color.WHITE
        set(value) {
            field = value
            backgroundBlue = Color.blue(value)
            backgroundGreen = Color.green(value)
            backgroundRed = Color.red(value)
            invalidate()
        }

    private var backgroundRed = 0
    private var backgroundGreen = 0
    private var backgroundBlue = 0
    private val paint = Paint()
    private var rectF = RectF(0f, 0f, 4f, 4f)

    init {
        canvasBackgroundColor = Color.WHITE
        paint.strokeWidth = 6f
        paint.style = Paint.Style.STROKE
    }

    constructor(
        context: Context,
        layoutParams: LinearLayout.LayoutParams,
        backgroundColor: Int
    ) : this(context) {

        canvasBackgroundColor = backgroundColor
        this.layoutParams = layoutParams
        rectF = RectF(0f, 0f, this.layoutParams.width.toFloat(), this.layoutParams.height.toFloat())
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        rectF = RectF(
            0f,
            0f,
            width.toFloat(),
            height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRGB(backgroundRed, backgroundGreen, backgroundBlue)
        canvas.drawRect(rectF, paint)
    }
}