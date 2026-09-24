package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.core.graphics.withRotation

open class MovingImageView(
    context: Context,
    var mGameView: GameView
) : View(context), OnTouchListener {
    @JvmField
    var mLastTouchX: Float = 0f
    @JvmField
    var mLastTouchY: Float = 0f
    @JvmField
    var mClearObject: ShadowImageView? = null
    @JvmField
    var isSetOnPosition: Boolean = false
    @JvmField
    var mObjectNumber: Int = 0
    var invisiblePointsNum: Int = 0
    @JvmField
    var bitmap: Bitmap? = null
    @JvmField
    var visiblePoints: Int = 0
    @JvmField
    var currentAngle: Float = 0f
    var dockPointX: Float = 0f
    var dockPointY: Float = 0f
    @JvmField
    var maskOfBitmap: IntArray? = null
    @JvmField
    var bitmapiMul: Float = 1f

    init {
        // TODO Auto-generated constructor stub
        //mClearObject=clear_object;
        //mObjectNumber=num;
        setOnTouchListener(this)
        currentAngle = 0f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        bitmap?.let { bitmap ->
            val drawRect = Rect(0, 0, width, height)
            if (currentAngle != 0f) {
                canvas.withRotation(
                    currentAngle,
                    (width / 2).toFloat(),
                    (height / 2).toFloat()
                ) {
                    //canvas.rotate(currentAngle);
                    drawBitmap(bitmap, null, drawRect, paint)
                }
            } else canvas.drawBitmap(bitmap, null, drawRect, paint)
        }
    }

    val bitmapWidth: Int
        get() = (bitmap?.getWidth().orZero() * bitmapiMul).toInt() //*(GameView.isScaledBitmaps()?2:1);
    val bitmapHeight: Int
        get() = (bitmap?.getHeight().orZero() * bitmapiMul).toInt() //*(GameView.isScaledBitmaps()?2:1);

    fun setBitmapiMul(imul: Float) {
        bitmapiMul = imul
    }

    var angle: Float
        get() = currentAngle
        set(angle) {
            currentAngle = angle
            invalidate()
        }

    fun getVisiblePoint(pointNum: Int): Point {
        var visiblePoints = 0
        //int max_pos=bitmap.getWidth()*bitmap.getHeight();
        //int pixels1[]=new int[max_pos];
        //bitmap.getPixels(pixels1, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (x in 0..<bitmap?.getWidth().orZero()) for (y in 0..<bitmap?.getHeight().orZero()) {
            val itemOfMask = y * bitmap?.getWidth().orZero() + x
            val posOfMask = itemOfMask / Integer.SIZE
            val bitOfMask = itemOfMask % Integer.SIZE
            if ((maskOfBitmap!![posOfMask] and (1 shl bitOfMask)) != 0) {
                visiblePoints++
                if (visiblePoints == pointNum) return Point(
                    (x / (bitmap?.getWidth().orZero().toFloat() / width)).toInt(),
                    (y / (bitmap?.getHeight().orZero().toFloat() / height)).toInt()
                )
            }
        }
        return Point(0, 0)
    }

    open fun isVisiblePoint(x: Int, y: Int): Boolean {
        val itemOfMask = (y / bitmapiMul * bitmap?.getWidth().orZero() + x / bitmapiMul).toInt()
        val posOfMask = itemOfMask / Integer.SIZE
        val bitOfMask = itemOfMask % Integer.SIZE
        return maskOfBitmap?.let { maskOfBitmap ->
            posOfMask >= 0 && posOfMask < maskOfBitmap.size && (maskOfBitmap[posOfMask] and (1 shl bitOfMask)) != 0
        } ?: false
    }

    fun setImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        visiblePoints = 0
        val maxPos = bitmap.getWidth() * bitmap.getHeight()
        IntArray(maxPos / Integer.SIZE + 1).let { maskOfBitmap ->
            this.maskOfBitmap = maskOfBitmap
            for (i in maskOfBitmap.indices) maskOfBitmap[i] = 0
            val pixels1 = IntArray(maxPos)
            bitmap.getPixels(
                pixels1,
                0,
                bitmap.getWidth(),
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight()
            )
            for (x in 0..<bitmap.getWidth()) for (y in 0..<bitmap.getHeight()) {
                if ((pixels1[y * bitmap.getWidth() + x] and -0x1000000) != 0) {
                    visiblePoints++
                    val itemOfMask = y * bitmap.getWidth() + x
                    val posOfMask = itemOfMask / Integer.SIZE
                    val bitOfMask = itemOfMask % Integer.SIZE
                    maskOfBitmap[posOfMask] = maskOfBitmap[posOfMask] or (1 shl bitOfMask)
                }
            }
        }
    }

    open fun setImageBitmap(bmp: Bitmap, iMul: Float) {
        setImageBitmap(bmp)
        setBitmapiMul(iMul)
    }

    fun setDockPoint(x: Float, y: Float) {
        dockPointX = x
        dockPointY = y
    }

    fun touchAnimation() {
        val animation: Animation = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            (width / 2).toFloat(),
            (height / 2).toFloat()
        )
        animation.setDuration(200)
        animation.repeatMode = Animation.REVERSE
        this.startAnimation(animation)
    }

    override fun onTouch(view: View?, me: MotionEvent): Boolean {
        if (isSetOnPosition) {
            if (GameView.currentScreenType != 1) return false
            if ((me.action == MotionEvent.ACTION_DOWN) && isVisiblePoint(
                    me.x.toInt(),
                    me.y.toInt()
                ) && !mGameView.isHasMovingObjectAtPoint(
                    Point(
                        (mClearObject?.left.orZero() + me.x).toInt(),
                        (mClearObject?.top.orZero() + me.y).toInt()
                    )
                )
            ) {
                mGameView.playItemName(mObjectNumber)
                touchAnimation()
            }
            return false
        }
        val x = left + me.x
        val y = top + me.y
        when (me.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isVisiblePoint(
                        me.x.toInt(),
                        me.y.toInt()
                    ) && mGameView.isHasMovingObjectAtPoint(
                        Point(
                            (mClearObject?.left.orZero() + me.x).toInt(),
                            (mClearObject?.top.orZero() + me.y).toInt()
                        )
                    )
                ) return false
                this.bringToFront()
                mLastTouchX = x
                mLastTouchY = y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                var left = (this.left - (mLastTouchX - x)).toInt()
                var top = (this.top - (mLastTouchY - y)).toInt()
                mLastTouchX = x
                mLastTouchY = y
                val w = width.toFloat()
                val h = height.toFloat()
                if (left < 0) left = 0
                if (left + w >= GameView.Owner.viewWidth) left =
                    (GameView.Owner.viewWidth - w).toInt()
                if (top < 0) top = 0
                if (top + h >= GameView.Owner.viewHeight) top =
                    (GameView.Owner.viewHeight - h).toInt()
                mGameView.setBounds(this, left.toFloat(), top.toFloat(), left + w, top + h)
                val res = resources
                val delta = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    res.getDisplayMetrics()
                )
                mClearObject?.let { mClearObject ->
                    val checkRect = RectF(
                        mClearObject.left + (mClearObject.right - mClearObject.left) / 2 - delta,
                        mClearObject.top + (mClearObject.bottom - mClearObject.top) / 2 - delta,
                        mClearObject.left + (mClearObject.right - mClearObject.left) / 2 + delta,
                        mClearObject.top + (mClearObject.bottom - mClearObject.top) / 2 + delta
                    )
                    if (checkRect.contains(
                            (this.left + (this.right - this.left) / 2).toFloat(),
                            (this.top + (this.bottom - this.top) / 2).toFloat()
                        )
                    ) {
                        touchAnimation()
                        mGameView.smallClick()
                        //this.setOnTouchListener(null);
                        isSetOnPosition = true
                        mGameView.setBounds(
                            this,
                            mClearObject.left.toFloat(),
                            mClearObject.top.toFloat(),
                            mClearObject.right.toFloat(),
                            mClearObject.bottom.toFloat()
                        )
                        mGameView.checkCompleteLevel(mObjectNumber)
                    }
                }
                return true
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {}
        }
        return false
    }

    fun returnToDock() {
        val animation: Animation =
            TranslateAnimation(0f, dockPointX - getLeft(), 0f, dockPointY - getTop())
        animation.setDuration(200)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                this@MovingImageView.clearAnimation()
                mGameView.setBounds(
                    this@MovingImageView,
                    dockPointX,
                    dockPointY,
                    dockPointX + getWidth(),
                    dockPointY + getHeight()
                )
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        this.startAnimation(animation)
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
