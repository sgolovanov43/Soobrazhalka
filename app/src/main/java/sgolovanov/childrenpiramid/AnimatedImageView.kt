package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.view.View

/**
 * Created by Sergey on 15.06.2016.
 */
class AnimatedImageView  //Bitmap backgroundBitmap=null;
    (context: Context) : View(context) {
    var centralPoint: Point? = null
    var isHorizontalReverse: Boolean = false
        set(value) {
            field = value
            scaleX = if (value) -1f
                     else 1f
        }
    var bitmap: Bitmap? = null

    fun setImageResource(resID: Int) {
        if (GameView.isScaledBitmaps) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = false
            options.inSampleSize = 2
            bitmap = BitmapFactory.decodeResource(getResources(), resID, options)
        } else bitmap = BitmapFactory.decodeResource(getResources(), resID)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.isAntiAlias = true
        val drawRect = Rect(0, 0, width, height)
        bitmap?.let { bitmap -> canvas.drawBitmap(bitmap, null, drawRect, paint) }
    }

    fun recycleBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) bitmap.recycle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleBitmap(bitmap)
        bitmap = null
    }
}
