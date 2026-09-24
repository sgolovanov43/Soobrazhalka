package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withSave

/**
 * Created by Sergey on 11.06.2016.
 */
class RotateImageView(context: Context, parent: GameView) : MovingImageView(context, parent),
    OnTouchListener {
    var centralPoint: PointF? = null

    init {
        setOnTouchListener(this)
    }

    fun setCentralPoint(x0: Float, y0: Float) {
        centralPoint = PointF(x0, y0)
    }

    override fun isVisiblePoint(x: Int, y: Int): Boolean {
        val radius =
            sqrt(((bitmap!!.getHeight() / 2) * (bitmap!!.getHeight() / 2) + (bitmap!!.getWidth() / 2) * (bitmap!!.getWidth() / 2)).toDouble()).toFloat()
        val obj_size = (radius * 2).toInt()
        val itemOfMask = (((y / bitmapiMul).toInt()) * obj_size + x / bitmapiMul).toInt()
        val posOfMask = itemOfMask / Integer.SIZE
        val bitOfMask = itemOfMask % Integer.SIZE
        if (posOfMask >= 0 && posOfMask < maskOfBitmap!!.size && (maskOfBitmap!![posOfMask] and (1 shl bitOfMask)) != 0) return true
        return false
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        if (bitmap != null) {
            //Rect drawRect=new Rect(0, 0, getWidth(), getHeight());
            if (centralPoint == null) centralPoint = PointF(getWidth() / 2f, getHeight() / 2f)
            if (currentAngle != 0f) {
                canvas.withSave {
                    bitmap?.let { bitmap ->
                        val radius =
                            sqrt(
                                (bitmap.getHeight() / 2f) * (bitmap.getHeight() / 2f) +
                                        (bitmap.getWidth() / 2f) * (bitmap.getWidth() / 2f)
                            )
                        val objSize = radius * 2f
                        val bitmapTmp =
                            createBitmap(objSize.toInt(), objSize.toInt())
                        val canvasTmp = Canvas(bitmapTmp)
                        canvasTmp.rotate(currentAngle, objSize / 2, objSize / 2)
                        canvasTmp.drawBitmap(
                            bitmap, null, RectF(
                                objSize / 2f - bitmap.getWidth() / 2f,
                                objSize / 2f - bitmap.getHeight() / 2f,
                                objSize / 2f + bitmap.getWidth() / 2f,
                                objSize / 2f + bitmap.getHeight() / 2f
                            ), paint
                        )
                        drawBitmap(
                            bitmapTmp, null, RectF(
                                centralPoint!!.x - objSize / 2f * bitmapiMul,
                                centralPoint!!.y - objSize / 2f * bitmapiMul,
                                centralPoint!!.x + objSize / 2f * bitmapiMul,
                                centralPoint!!.y + objSize / 2f * bitmapiMul
                            ), paint
                        )

                        val maxPos = bitmapTmp.getWidth() * bitmapTmp.getHeight()
                        val pixels1 = IntArray(maxPos)
                        maskOfBitmap = IntArray(maxPos / Integer.SIZE + 1)
                        for (i in maskOfBitmap!!.indices) maskOfBitmap!![i] = 0
                        bitmapTmp.getPixels(
                            pixels1,
                            0,
                            bitmapTmp.getWidth(),
                            0,
                            0,
                            bitmapTmp.getWidth(),
                            bitmapTmp.getHeight()
                        )
                        for (x in 0..<bitmapTmp.getWidth()) for (y in 0..<bitmapTmp.getHeight()) {
                            if ((pixels1[y * bitmapTmp.getWidth() + x] and -0x1000000) != 0) {
                                visiblePoints++
                                val itemOfMask = y * bitmapTmp.getWidth() + x
                                val posOfMask = itemOfMask / Integer.SIZE
                                val bitOfMask = itemOfMask % Integer.SIZE
                                maskOfBitmap!![posOfMask] =
                                    maskOfBitmap!![posOfMask] or (1 shl bitOfMask)
                            }
                        }
                        bitmapTmp.recycle()
                    }
                }
            } else {
                val bitmapRect = RectF(
                    centralPoint!!.x - bitmap!!.getWidth() / 2f * bitmapiMul,
                    centralPoint!!.y - bitmap!!.getHeight() / 2f * bitmapiMul,
                    centralPoint!!.x + bitmap!!.getWidth() / 2f * bitmapiMul,
                    centralPoint!!.y + bitmap!!.getHeight() / 2f * bitmapiMul
                )
                canvas.drawBitmap(bitmap!!, null, bitmapRect, paint)
            }
        }
    }

    override fun onTouch(view: View?, me: MotionEvent): Boolean {
        if (isSetOnPosition || me.getActionIndex() > 0) {
            //mGameView.playItemName(mObjectNumber);
            return super.onTouch(view, me)
        }
        val x = me.getX()
        val y = me.getY()
        when (me.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isVisiblePoint(
                        me.x.toInt(),
                        me.y.toInt()
                    ) && mGameView.isHasMovingObjectAtPoint(
                        Point(
                            (mClearObject!!.getLeft() + me.x).toInt(),
                            (mClearObject!!.getTop() + me.y).toInt()
                        )
                    )
                ) return false
                this.bringToFront()
                mLastTouchX = x
                mLastTouchY = y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                //float vectorAB=(y - getHeight()/2)
                val x1 = x
                val x0 = (width / 2).toFloat()
                val x2 = mLastTouchX
                val y1 = y
                val y0 = (height / 2).toFloat()
                val y2 = mLastTouchY
                val angle1 =
                    (atan2((y1 - y0).toDouble(), (x1 - x0).toDouble()) / Math.PI * 180).toFloat()
                val angle2 =
                    (atan2((y2 - y0).toDouble(), (x2 - x0).toDouble()) / Math.PI * 180).toFloat()
                //float currentAngle=this.getAngle();
                var angle = angle1 - angle2
                Log.v(
                    "Angle: ",
                    String.format("a=%.1f x1=%.1f x2=%.1f y1=%.1f y2=%.1f", angle, x1, x2, y1, y2)
                )
                if (abs(angle) <= 1) return true
                mLastTouchX = x
                mLastTouchY = y
                val w = width.toFloat()
                val h = height.toFloat()
                angle += this.angle
                angle = if (angle < 0) angle + 360 else (if (angle > 360) angle - 360 else angle)
                if (angle !in 5.0..355.0) {
                    touchAnimation()
                    mGameView.smallClick()
                    //this.setOnTouchListener(null);
                    isSetOnPosition = true
                    this.angle = 0f
                    mGameView.checkCompleteLevel(mObjectNumber)
                } else this.angle =  /*getRotation()+*/angle
                return true
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {}
        }
        return false
    }
}
