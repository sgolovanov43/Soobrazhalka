package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlin.math.atan2

/**
 * Created by Sergey on 15.06.2016.
 */
class ApplicationImageView(context: Context, parent: GameView) : MovingImageView(
    context, parent
) {
    var points: MutableList<Point>? = null
    var strokeWidth: Float = 2f
    var currentPoint: Int = 0

    //static Bitmap nozhnicy=null;
    var nozhnicyView: ImageView? = null
    var rotateAngle: Int = 0
    val stepPoints: Int = 5

    init {
        setOnTouchListener(null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (nozhnicyView != null) mGameView.removeView(nozhnicyView)
        nozhnicyView = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setImageBitmap(bitmap: Bitmap, iMul: Float) {
        super.setImageBitmap(bitmap, iMul)
        points = ArrayList()
        val pixels2 = IntArray(bitmap.getHeight() * bitmap.getWidth())
        bitmap.getPixels(pixels2, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight())
        var row = bitmap.getHeight() / 2
        var col = 0
        var startSearchNum = 0
        for (x in 0..<bitmap.getWidth()) {
            if (Color.alpha(pixels2[row * bitmap.getWidth() + x]) != 0) {
                col = x
                points!!.add(Point(col, row))
                break
            }
        }
        while (true) {
            val searchNum = getSearchPoint(
                pixels2,
                col,
                row,
                bitmap.getWidth(),
                bitmap.getHeight(),
                startSearchNum
            )
            if (searchNum < 0) return
            row = getRowBySearchNum(row, searchNum)
            col = getColBySearchNum(col, searchNum)
            if (points!!.get(0).x == col && points!!.get(0).y == row) break
            points!!.add(Point(col, row))
            startSearchNum = getSearchNumByPoints(
                points!!.get(points!!.size - 2),
                points!!.get(points!!.size - 1)
            )
        }
        nozhnicyView = ImageView(context)
        nozhnicyView?.setBackgroundResource(R.drawable.animaciya_nozhnicy)
        mGameView.addView(nozhnicyView)
        (nozhnicyView?.background as? AnimationDrawable)?.let { startAnimation ->
            startAnimation.start()
        }
        setNozhnicyViewPosition()
        nozhnicyView?.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, me: MotionEvent): Boolean {
                when (me.action) {
                    MotionEvent.ACTION_DOWN -> return true
                    MotionEvent.ACTION_MOVE -> {
                        nozhnicyView?.bringToFront()
                        val delta = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            30f,
                            resources.displayMetrics
                        ).toInt()
                        val rectF: RectF = nozhnicyRect
                        val curPoint: PointF = currentScreenPoint
                        val touchPoint = PointF(rectF.left + me.getX(), rectF.top + me.getY())
                        val checkRect = RectF(
                            touchPoint.x - delta,
                            touchPoint.y - delta,
                            touchPoint.x + delta,
                            touchPoint.y + delta
                        )
                        if (checkRect.contains(curPoint.x, curPoint.y)) {
                            currentPoint += stepPoints
                            if (currentPoint >= points!!.size) {
                                //currentPoint--;
                                mGameView.removeView(nozhnicyView)
                                nozhnicyView = null
                                val layoutParams =
                                    this@ApplicationImageView.layoutParams as RelativeLayout.LayoutParams
                                val imageLeft = layoutParams.leftMargin //imageView.getLeft();
                                val imageTop = layoutParams.topMargin //imageView.getTop();
                                val layoutParams2 =
                                    this@ApplicationImageView.mClearObject?.layoutParams as RelativeLayout.LayoutParams
                                val imageLeft2 = layoutParams2.leftMargin //imageView.getLeft();
                                val imageTop2 = layoutParams2.topMargin //imageView.getTop();
                                val animationTranslate: Animation = TranslateAnimation(
                                    0f,
                                    (imageLeft2 - imageLeft).toFloat(),
                                    0f,
                                    (imageTop2 - imageTop).toFloat()
                                )
                                val animationRotate: Animation = RotateAnimation(
                                    0f,
                                    rotateAngle.toFloat(),
                                    (layoutParams.width / 2).toFloat(),
                                    (layoutParams.height / 2).toFloat()
                                )
                                val animationSet = AnimationSet(true)
                                animationSet.addAnimation(animationRotate)
                                animationSet.addAnimation(animationTranslate)
                                animationSet.setDuration(600)
                                animationTranslate.setAnimationListener(object : AnimationListener {
                                    override fun onAnimationStart(animation: Animation?) {
                                    }

                                    override fun onAnimationEnd(animation: Animation?) {
                                        isSetOnPosition = true
                                        mGameView.setBounds(
                                            this@ApplicationImageView,
                                            imageLeft2.toFloat(),
                                            imageTop2.toFloat(),
                                            (imageLeft2 + layoutParams2.width).toFloat(),
                                            (imageTop2 + layoutParams2.height).toFloat()
                                        )
                                        this@ApplicationImageView.rotation = rotateAngle.toFloat()
                                        this@ApplicationImageView.clearAnimation()
                                        mGameView.post { mGameView.checkCompleteLevel(mObjectNumber) }
                                    }

                                    override fun onAnimationRepeat(animation: Animation?) {
                                    }
                                })
                                this@ApplicationImageView.startAnimation(animationSet)
                            } else {
                                setNozhnicyViewPosition()
                            }
                            this@ApplicationImageView.invalidate()
                        }
                    }
                }
                return false
            }
        })
    }

    val nozhnicyWidth: Float
        get() = strokeWidth * 60

    val currentScreenPoint: PointF
        get() {
            val layoutParams = this.layoutParams as RelativeLayout.LayoutParams
            val imageLeft = layoutParams.leftMargin //imageView.getLeft();
            val imageTop = layoutParams.topMargin //imageView.getTop();
            return PointF(
                imageLeft + points!!.get(currentPoint).x * bitmapiMul,
                imageTop + points!!.get(currentPoint).y * bitmapiMul
            )
        }

    val nozhnicyRect: RectF
        get() {
            val nozhnicyWidth = this.nozhnicyWidth
            val screenPoint = this.currentScreenPoint
            val rectF = RectF(
                screenPoint.x - nozhnicyWidth / 2,
                screenPoint.y - nozhnicyWidth / 2,
                screenPoint.x + nozhnicyWidth / 2,
                screenPoint.y + nozhnicyWidth / 2
            )
            return rectF
        }

    fun setNozhnicyViewPosition() {
        nozhnicyView?.let { mGameView.setBounds(it, this.nozhnicyRect) }
        val point1 = points!!.get(currentPoint)
        var point2: Point? = null
        if (currentPoint < points!!.size - stepPoints - 1) point2 =
            points!!.get(currentPoint + stepPoints)
        else point2 = points!!.get(0)
        val angle = getAngleNumByPoints(point1, point2)
        nozhnicyView!!.setRotation(angle.toFloat())
    }

    fun getSearchPoint(
        pixels: IntArray,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        startSearchNum: Int
    ): Int {
        var searchNum = startSearchNum
        while (true) {
            searchNum++
            if (searchNum > 7) searchNum = 0
            if (searchNum == startSearchNum) return -1
            val row = getRowBySearchNum(y, searchNum)
            val col = getColBySearchNum(x, searchNum)
            if (col !in 0..<width || row < 0 || row >= height) {
                //searchNum++;
                continue
            }
            if (Color.alpha(pixels[row * width + col]) != 0) return searchNum
        }
    }

    fun getColBySearchNum(x: Int, searchNum: Int): Int {
        var col = x
        if (searchNum == 0 || searchNum == 1 || searchNum == 7) {
            col--
        }
        if (searchNum == 3 || searchNum == 4 || searchNum == 5) {
            col++
        }
        return col
    }

    fun getRowBySearchNum(y: Int, searchNum: Int): Int {
        var row = y
        if (searchNum == 5 || searchNum == 6 || searchNum == 7) {
            row++
        }
        if (searchNum == 1 || searchNum == 2 || searchNum == 3) {
            row--
        }
        return row
    }

    fun getSearchNumByPoints(p1: Point, p2: Point): Int {
        val deltaX = p1.x - p2.x
        val deltaY = p1.y - p2.y
        if (deltaX == 0) {
            if (deltaY == -1) return 2
            else if (deltaY == 1) return 6
        } else if (deltaX == -1) {
            if (deltaY == -1) return 1
            else if (deltaY == 1) return 7
            else if (deltaY == 0) return 0
        } else if (deltaX == 1) {
            if (deltaY == -1) return 3
            else if (deltaY == 1) return 5
            else if (deltaY == 0) return 4
        }
        return -1
    }

    fun getAngleNumByPoints(p1: Point, p2: Point): Int {
        val deltaX = p2.x - p1.x
        val deltaY = p2.y - p1.y
        val angle1 = (atan2(deltaY.toDouble(), deltaX.toDouble()) / Math.PI * 180).toFloat() + 90
        return angle1.toInt()
        /*if(deltaX==0)
        {
            if(deltaY==-1)
                return 0;
            else if(deltaY==1)
                return 180;
        }
        else if(deltaX==-1)
        {
            if(deltaY==-1)
                return 315;
            else if(deltaY==1)
                return 225;
            else if(deltaY==0)
                return 270;
        }
        else if(deltaX==1)
        {
            if(deltaY==-1)
                return 45;
            else if(deltaY==1)
                return 135;
            else if(deltaY==0)
                return 90;
        }
        return -1;*/
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points != null && points!!.size > 1 && isSetOnPosition == false) {
            val paint = Paint()
            paint.setAntiAlias(true)
            paint.setColor(Color.GRAY)
            paint.setStyle(Paint.Style.STROKE)
            run {
                paint.setStrokeWidth(strokeWidth)
                paint.setPathEffect(
                    DashPathEffect(
                        floatArrayOf(strokeWidth * 5, strokeWidth * 2),
                        0f
                    )
                )
                val path = Path()
                path.moveTo(
                    points!!.get(points!!.size - 1).x * bitmapiMul,
                    points!!.get(0).y * bitmapiMul
                )
                for (i in points!!.size - 1 downTo currentPoint) {
                    path.lineTo(points!!.get(i).x * bitmapiMul, points!!.get(i).y * bitmapiMul)
                }
                canvas.drawPath(path, paint)
            }
            if (currentPoint > 0) {
                paint.setStrokeWidth(strokeWidth / 2)
                paint.setColor(Color.BLACK)
                var startPoint = currentPoint
                if (currentPoint >= points!!.size) {
                    startPoint = points!!.size - 1
                    canvas.drawLine(
                        points!!.get(0).x * bitmapiMul,
                        points!!.get(0).y * bitmapiMul,
                        points!!.get(startPoint).x * bitmapiMul,
                        points!!.get(startPoint).y * bitmapiMul,
                        paint
                    )
                }
                for (i in startPoint downTo 1) {
                    canvas.drawLine(
                        points!!.get(i).x * bitmapiMul,
                        points!!.get(i).y * bitmapiMul,
                        points!!.get(i - 1).x * bitmapiMul,
                        points!!.get(i - 1).y * bitmapiMul,
                        paint
                    )
                }
            }
        }
    }
}
