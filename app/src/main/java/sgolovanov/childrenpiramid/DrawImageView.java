package sgolovanov.childrenpiramid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

public class DrawImageView extends MovingImageView  implements View.OnTouchListener {
	private Bitmap shadowBitmap;
	//private Bitmap puzzleBitmap;
	private Paint drawPaint;
	Canvas shadowCanvas;
	Paint canvasPaint;
	Path drawPath;
	int radius=50;
	//int[] puzzlePixels;
	//int allShowPixels=0;
	//boolean isComplete;
	//int resourceSound=0;
	//PictureObject pictureObject;
	public DrawImageView(Context context, GameView parent) {
		super(context,parent);
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
		setOnTouchListener(this);
	}

	public void setImageBitmap(Bitmap bmp,float radius)
	{
		super.setImageBitmap(bmp);
		shadowBitmap=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		BitmapShader patternBMPshader = new BitmapShader(bmp,
				Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		shadowCanvas=new Canvas(shadowBitmap);
		drawPath = new Path();
		drawPaint=new Paint();
		drawPaint.setAlpha(0xff);
		drawPaint.setShader(patternBMPshader);
		drawPaint.setStrokeWidth(radius);
		drawPaint.setStyle(Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);

		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}


	@Override
	protected  void  onDetachedFromWindow() {
		super.onDetachedFromWindow();
		recycleBitmaps();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Rect drawRect=new Rect(0, 0, getWidth(), getHeight());
		if(this.isSetOnPosition==false) {
			if(shadowBitmap!=null && shadowBitmap.isRecycled()==false)
				canvas.drawBitmap(shadowBitmap, null, drawRect, canvasPaint);
		}
		else
			if(bitmap!=null && bitmap.isRecycled()==false)
				canvas.drawBitmap(bitmap, null, drawRect, canvasPaint);
	    //canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent me) {
		// TODO Auto-generated method stub
		if(isSetOnPosition) {
			return super.onTouch(view,me);
		}
		float x=me.getX();
		float y=me.getY();
		switch (me.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				this.bringToFront();
				mLastTouchX=x;
				mLastTouchY=y;
				//return true;
			}
			case MotionEvent.ACTION_MOVE: {
				//Canvas puzzle_canvas=new Canvas(puzzleBitmap);
		        //mainCanvas.drawLine(mLastTouchX, mLastTouchY, x, y, drawPaint);
				drawPath.moveTo(mLastTouchX, mLastTouchY);
				drawPath.lineTo(x, y);
				shadowCanvas.drawPath(drawPath, drawPaint);
				drawPath.reset();
				mLastTouchX=x;
				mLastTouchY=y;
				if(checkComplete())
				{
					isSetOnPosition=true;
					invalidate();
					mGameView.SmallClick();
					//this.setOnTouchListener(null);
					isSetOnPosition=true;
					mGameView.CheckCompleteLevel(mObjectNumber);
					return false;
				}
				//this.setImageBitmap(mainBitmap);
				//return true;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				break;
			}
		}
		invalidate();
		return true;
	}
	
	private boolean checkComplete()
	{
        //int[] pixels1=new int[puzzleBitmap.getHeight()*puzzleBitmap.getWidth()*4];
        int[] pixels2=new int[shadowBitmap.getHeight()*shadowBitmap.getWidth()];
		shadowBitmap.getPixels(pixels2, 0, shadowBitmap.getWidth(), 0, 0, shadowBitmap.getWidth(), shadowBitmap.getHeight());
        int equalPoints=0;
        for(int i=0;i<pixels2.length;i++)
        {
			//int k=puzzlePixels[i] >> 24;
			int alpha=Color.alpha(pixels2[i]);
        	if(alpha>0)
        		equalPoints++;
        }
        return equalPoints>=(int)(visiblePoints*0.85f);
		//return false;
	}

	public void recycleBitmaps()
	{
		if(shadowBitmap!=null && shadowBitmap.isRecycled()==false)
			shadowBitmap.recycle();
		shadowBitmap=null;
		/*if(puzzleBitmap!=null && puzzleBitmap.isRecycled()==false)
			puzzleBitmap.recycle();
		puzzleBitmap=null;*/
	}
}
