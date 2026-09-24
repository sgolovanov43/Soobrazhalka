package sgolovanov.childrenpiramid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Shader
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.graphics.createBitmap

class DrawImageView(context: Context, parent: GameView) : MovingImageView(context, parent),
    OnTouchListener {
    private var shadowBitmap: Bitmap? = null

    //private Bitmap puzzleBitmap;
    private var drawPaint: Paint? = null
    var shadowCanvas: Canvas? = null
    var canvasPaint: Paint? = null
    var drawPath: Path? = null
    var radius: Int = 50

    //int[] puzzlePixels;
    //int allShowPixels=0;
    //boolean isComplete;
    //int resourceSound=0;
    //PictureObject pictureObject;
    init {
        /*this.puzzleBitmap=puzzleBmp;//Bitmap.createBitmap(puzzleBmp);
		this.shadowBitmap=shadowBmp;//Bitmap.createBitmap(shadowBmp);

		puzzlePixels=new int[puzzleBitmap.getHeight()*puzzleBitmap.getWidth()];
		puzzleBitmap.getPixels(puzzlePixels, 0, puzzleBitmap.getWidth(), 0, 0, puzzleBitmap.getWidth(), puzzleBitmap.getHeight());
		allShowPixels=0;
		for(int i=0;i<puzzlePixels.length;i++)
		{
			//int k=puzzlePixels[i] >> 24;
			int alpha=Color.alpha(puzzlePixels[i]);
			if(alpha==0xff)
				allShowPixels++;
		}

		BitmapShader patternBMPshader = new BitmapShader(puzzleBitmap,
				Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		shadowCanvas=new Canvas(shadowBitmap);
		drawPath = new Path();
		drawPaint=new Paint();
		drawPaint.setColor(0xffffffff);
		drawPaint.setShader(patternBMPshader);
		drawPaint.setStrokeWidth(radius);
		drawPaint.setStyle(Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);

		canvasPaint = new Paint(Paint.DITHER_FLAG);*/
        setOnTouchListener(this)
    }

    override fun setImageBitmap(bitmap: Bitmap, radius: Float) {
        super.setImageBitmap(bitmap)
        shadowBitmap =
            createBitmap(bitmap.getWidth(), bitmap.getHeight())
        val patternBMPshader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT, Shader.TileMode.REPEAT
        )
        shadowCanvas = Canvas(shadowBitmap!!)
        drawPath = Path()
        Paint().apply {
            setAlpha(0xff)
            setShader(patternBMPshader)
            strokeWidth = radius
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        canvasPaint = Paint(Paint.DITHER_FLAG)
    }


    protected override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycleBitmaps()
    }

    override fun onDraw(canvas: Canvas) {
        val drawRect = Rect(0, 0, getWidth(), getHeight())
        if (this.isSetOnPosition == false) {
            if (shadowBitmap != null && shadowBitmap!!.isRecycled() == false) canvas.drawBitmap(
                shadowBitmap!!,
                null,
                drawRect,
                canvasPaint
            )
        } else if (bitmap?.isRecycled != true)
            canvas.drawBitmap(
            bitmap!!,
            null,
            drawRect,
            canvasPaint
        )
        //canvas.drawPath(drawPath, drawPaint);
    }

    override fun onTouch(view: View?, me: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        if (isSetOnPosition) {
            return super.onTouch(view, me)
        }
        val x = me.getX()
        val y = me.getY()
        when (me.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    this.bringToFront()
                    mLastTouchX = x
                    mLastTouchY = y
                }
                run {
                    //Canvas puzzle_canvas=new Canvas(puzzleBitmap);
                    //mainCanvas.drawLine(mLastTouchX, mLastTouchY, x, y, drawPaint);
                    drawPath!!.moveTo(mLastTouchX, mLastTouchY)
                    drawPath!!.lineTo(x, y)
                    shadowCanvas!!.drawPath(drawPath!!, drawPaint!!)
                    drawPath!!.reset()
                    mLastTouchX = x
                    mLastTouchY = y
                    if (checkComplete()) {
                        isSetOnPosition = true
                        invalidate()
                        mGameView.smallClick()
                        //this.setOnTouchListener(null);
                        isSetOnPosition = true
                        mGameView.checkCompleteLevel(mObjectNumber)
                        return false
                    }
                }
                run {}
            }

            MotionEvent.ACTION_MOVE -> {
                run {
                    drawPath!!.moveTo(mLastTouchX, mLastTouchY)
                    drawPath!!.lineTo(x, y)
                    shadowCanvas!!.drawPath(drawPath!!, drawPaint!!)
                    drawPath!!.reset()
                    mLastTouchX = x
                    mLastTouchY = y
                    if (checkComplete()) {
                        isSetOnPosition = true
                        invalidate()
                        mGameView.smallClick()
                        isSetOnPosition = true
                        mGameView.checkCompleteLevel(mObjectNumber)
                        return false
                    }
                }
                run {}
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {}
        }
        invalidate()
        return true
    }

    private fun checkComplete(): Boolean {
        //int[] pixels1=new int[puzzleBitmap.getHeight()*puzzleBitmap.getWidth()*4];
        val pixels2 = IntArray(shadowBitmap!!.getHeight() * shadowBitmap!!.getWidth())
        shadowBitmap!!.getPixels(
            pixels2,
            0,
            shadowBitmap!!.getWidth(),
            0,
            0,
            shadowBitmap!!.getWidth(),
            shadowBitmap!!.getHeight()
        )
        var equalPoints = 0
        for (i in pixels2.indices) {
            //int k=puzzlePixels[i] >> 24;
            val alpha = Color.alpha(pixels2[i])
            if (alpha > 0) equalPoints++
        }
        return equalPoints >= (visiblePoints * 0.85f).toInt()
        //return false;
    }

    fun recycleBitmaps() {
        if (shadowBitmap != null && shadowBitmap!!.isRecycled() == false) shadowBitmap!!.recycle()
        shadowBitmap = null
        /*if(puzzleBitmap!=null && puzzleBitmap.isRecycled()==false)
			puzzleBitmap.recycle();
		puzzleBitmap=null;*/
    }
}
