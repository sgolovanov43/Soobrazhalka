package sgolovanov.childrenpiramid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import java.nio.ByteBuffer

/**
 * Created by Sergey on 13.06.2016.
 */
class ShadowImageView(context: Context) : View(context) {
    @JvmField
    var bitmap: Bitmap? = null
    var maskOfBitmap: IntArray? = null
    @JvmField
    var visiblePoints: Int = 0
    var shadowColor: Int = Color.BLACK
    fun setImageBitmap(bmp: Bitmap?) {
        bitmap = bmp
        visiblePoints = 0
        val max_pos = bitmap!!.getWidth() * bitmap!!.getHeight()
        maskOfBitmap = IntArray(max_pos / Integer.SIZE + 1)
        for (i in maskOfBitmap!!.indices) maskOfBitmap!![i] = 0
        val pixels1: IntArray? = IntArray(max_pos)
        bitmap!!.getPixels(
            pixels1!!,
            0,
            bitmap!!.getWidth(),
            0,
            0,
            bitmap!!.getWidth(),
            bitmap!!.getHeight()
        )
        for (x in 0..<bitmap!!.getWidth()) for (y in 0..<bitmap!!.getHeight()) {
            if ((pixels1[y * bitmap!!.getWidth() + x] and -0x1000000) != 0) {
                visiblePoints++
                val itemOfMask = y * bitmap!!.getWidth() + x
                val posOfMask = itemOfMask / Integer.SIZE
                val bitOfMask = itemOfMask % Integer.SIZE
                maskOfBitmap!![posOfMask] = maskOfBitmap!![posOfMask] or (1 shl bitOfMask)
            }
        }
        invalidate()
    }

    fun setShadowBitmap(bmp: Bitmap, color: Int) {
        shadowColor = color
        val max_pos = bmp.getWidth() * bmp.getHeight()
        val pixels1: IntArray? = IntArray(max_pos)
        val pixels2: ByteArray? = ByteArray(max_pos)
        bmp.getPixels(pixels1!!, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight())
        visiblePoints = 0
        maskOfBitmap = IntArray(max_pos / Integer.SIZE + 1)
        for (i in maskOfBitmap!!.indices) maskOfBitmap!![i] = 0
        val buffer = ByteBuffer.allocate(max_pos)
        for (y in 0..<bmp.getHeight()) {
            val row_pos = y * bmp.getWidth()
            val prev_row_pos = (y - 1) * bmp.getWidth()
            val prev_prev_row_pos = (y - 2) * bmp.getWidth()
            val prev_prev_prev_row_pos = (y - 3) * bmp.getWidth()
            val next_row_pos = (y + 1) * bmp.getWidth()
            val next_next_row_pos = (y + 2) * bmp.getWidth()
            val next_next_next_row_pos = (y + 3) * bmp.getWidth()
            for (x in 0..<bmp.getWidth()) {
                val pixel = pixels1[row_pos + x]
                if (pixel != Color.TRANSPARENT) {
                    visiblePoints++
                    val itemOfMask = row_pos + x
                    val posOfMask = itemOfMask / Integer.SIZE
                    val bitOfMask = itemOfMask % Integer.SIZE
                    maskOfBitmap!![posOfMask] = maskOfBitmap!![posOfMask] or (1 shl bitOfMask)
                    var isPrevRowTransparent = true
                    var isPrevPrevRowTransparent = true
                    var isPrevPrevPrevRowTransparent = true
                    if (prev_row_pos >= 0) isPrevRowTransparent =
                        pixels1[prev_row_pos + x] == Color.TRANSPARENT
                    if (prev_prev_row_pos >= 0) isPrevPrevRowTransparent =
                        pixels1[prev_prev_row_pos + x] == Color.TRANSPARENT
                    if (prev_prev_prev_row_pos >= 0) isPrevPrevPrevRowTransparent =
                        pixels1[prev_prev_prev_row_pos + x] == Color.TRANSPARENT
                    var isPrevPosTransparent = true
                    var isPrevPrevPosTransparent = true
                    var isPrevPrevPrevPosTransparent = true
                    if (x > 0) isPrevPosTransparent =
                        pixels1[row_pos + x - 1] == Color.TRANSPARENT
                    if (x > 1) isPrevPrevPosTransparent =
                        pixels1[row_pos + x - 2] == Color.TRANSPARENT
                    if (x > 2) isPrevPrevPrevPosTransparent =
                        pixels1[row_pos + x - 3] == Color.TRANSPARENT

                    var isNextRowTransparent = true
                    var isNextNextRowTransparent = true
                    var isNextNextNextRowTransparent = true
                    if (y < bmp.getHeight() - 1) isNextRowTransparent =
                        pixels1[next_row_pos + x] == Color.TRANSPARENT
                    if (y < bmp.getHeight() - 2) isNextNextRowTransparent =
                        pixels1[next_next_row_pos + x] == Color.TRANSPARENT
                    if (y < bmp.getHeight() - 3) isNextNextNextRowTransparent =
                        pixels1[next_next_next_row_pos + x] == Color.TRANSPARENT
                    var isNextPosTransparent = true
                    var isNextNextPosTransparent = true
                    var isNextNextNextPosTransparent = true
                    if (x < bmp.getWidth() - 1) isNextPosTransparent =
                        pixels1[row_pos + x + 1] == Color.TRANSPARENT
                    if (x < bmp.getWidth() - 2) isNextNextPosTransparent =
                        pixels1[row_pos + x + 2] == Color.TRANSPARENT
                    if (x < bmp.getWidth() - 3) isNextNextNextPosTransparent =
                        pixels1[row_pos + x + 3] == Color.TRANSPARENT

                    if (isPrevPosTransparent || isPrevPrevPosTransparent || isPrevPrevPrevRowTransparent || isPrevRowTransparent || isPrevPrevRowTransparent || isPrevPrevPrevPosTransparent ||
                        isNextPosTransparent || isNextNextPosTransparent || isNextNextNextRowTransparent || isNextRowTransparent || isNextNextRowTransparent || isNextNextNextPosTransparent
                    ) buffer.put(row_pos + x, Color.alpha(pixel).toByte()) //|0x00ffffff;
                    else buffer.put(row_pos + x, (Color.alpha(pixel) / 2).toByte())
                } else buffer.put(row_pos + x, 0.toByte())
            }
        }
        bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ALPHA_8)
        bitmap!!.copyPixelsFromBuffer(buffer)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.setAntiAlias(true)
        if (bitmap != null) {
            val drawRect = Rect(0, 0, getWidth(), getHeight())
            paint.setColor(shadowColor)
            canvas.drawBitmap(bitmap!!, null, drawRect, paint)
        }
    }

    fun getVisiblePoint(pointNum: Int): Point {
        var visiblePoints = 0
        //int max_pos=bitmap.getWidth()*bitmap.getHeight();
        //int pixels1[]=new int[max_pos];
        //bitmap.getPixels(pixels1, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (x in 0..<bitmap!!.getWidth()) for (y in 0..<bitmap!!.getHeight()) {
            val itemOfMask = y * bitmap!!.getWidth() + x
            val posOfMask = itemOfMask / Integer.SIZE
            val bitOfMask = itemOfMask % Integer.SIZE
            if ((maskOfBitmap!![posOfMask] and (1 shl bitOfMask)) != 0) {
                visiblePoints++
                if (visiblePoints == pointNum) return Point(
                    (x / (bitmap!!.getWidth().toFloat() / getWidth())).toInt(),
                    (y / (bitmap!!.getHeight().toFloat() / getHeight())).toInt()
                )
            }
        }
        return Point(0, 0)
    }

    fun isVisiblePoint(x: Int, y: Int): Boolean {
        val itemOfMask = y * bitmap!!.getWidth() + x
        val posOfMask = itemOfMask / Integer.SIZE
        val bitOfMask = itemOfMask % Integer.SIZE
        if (posOfMask < maskOfBitmap!!.size && (maskOfBitmap!![posOfMask] and (1 shl bitOfMask)) != 0) return true
        return false
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
