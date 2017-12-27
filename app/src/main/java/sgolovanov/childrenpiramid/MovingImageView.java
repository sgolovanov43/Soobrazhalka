package sgolovanov.childrenpiramid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.security.acl.Owner;

public class MovingImageView extends View implements View.OnTouchListener
{
	float mLastTouchX=0;
	float mLastTouchY=0;
	GameView mGameView;
	public ShadowImageView mClearObject;
	public boolean isSetOnPosition;
	public int mObjectNumber;
	int invisiblePointsNum=0;
	Bitmap bitmap;
	int visiblePoints=0;
	float currentAngle=0;
	float dockPointX=0;
	float dockPointY=0;
	int[] maskOfBitmap=null;
	float bitmapiMul=1;
	public MovingImageView(Context context,GameView parent/*,ImageView clear_object,int num*/) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		mGameView=parent;
		//mClearObject=clear_object;
		isSetOnPosition=false;
		//mObjectNumber=num;
		setOnTouchListener(this);
		currentAngle=0;
	}

	@Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
		paint.setAntiAlias(true);
        /*if(backgroundBitmap!=null)
            canvas.drawBitmap(backgroundBitmap,null,new Rect(0,0,getWidth(),getHeight()),paint);*/
        if(bitmap!=null) {
            Rect drawRect=new Rect(0, 0, getWidth(), getHeight());
			if(currentAngle!=0) {
				canvas.save();
				canvas.rotate(currentAngle, getWidth() / 2, getHeight() / 2);
				//canvas.rotate(currentAngle);
				canvas.drawBitmap(bitmap, null, drawRect,paint);
				canvas.restore();
			}
			else
				canvas.drawBitmap(bitmap, null, drawRect,paint);
		}
    }

	public int getBitmapWidth()
	{
		return (int) (bitmap.getWidth()*bitmapiMul);//*(GameView.isScaledBitmaps()?2:1);
	}
	public int getBitmapHeight()
	{
		return (int) (bitmap.getHeight()*bitmapiMul);//*(GameView.isScaledBitmaps()?2:1);
	}
	public void setBitmapiMul(float imul)
	{
		bitmapiMul=imul;
	}

	public void setAngle(float angle)
	{
		currentAngle=angle;
		invalidate();
	}

	public float getAngle()
	{
		return currentAngle;
	}

	public int getVisiblePoints()
	{
		return visiblePoints;
	}

	public Point getVisiblePoint(int pointNum)
	{
		int visiblePoints=0;
		//int max_pos=bitmap.getWidth()*bitmap.getHeight();
		//int pixels1[]=new int[max_pos];
		//bitmap.getPixels(pixels1, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for(int x=0;x<bitmap.getWidth();x++)
			for(int y=0;y<bitmap.getHeight();y++)
			{
				int itemOfMask=y*bitmap.getWidth()+x;
				int posOfMask=itemOfMask/Integer.SIZE;
				int bitOfMask=itemOfMask%Integer.SIZE;
				if((maskOfBitmap[posOfMask] & (1 << bitOfMask))!=0) {
					visiblePoints++;
					if(visiblePoints==pointNum)
						return new Point((int)(x/((float)bitmap.getWidth()/getWidth())),(int)(y/((float)bitmap.getHeight()/getHeight())));
				}
			}
		return new Point(0,0);
	}

	public boolean isVisiblePoint(int x,int y)
	{
		int itemOfMask= (int) (y/bitmapiMul*bitmap.getWidth()+x/bitmapiMul);
		int posOfMask=itemOfMask/Integer.SIZE;
		int bitOfMask=itemOfMask%Integer.SIZE;
		if(posOfMask>=0 && posOfMask < maskOfBitmap.length && (maskOfBitmap[posOfMask] & (1 << bitOfMask))!=0)
			return true;
		return false;
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void setImageBitmap(Bitmap bmp)
	{
		bitmap=bmp;
		visiblePoints=0;
		int max_pos=bitmap.getWidth()*bitmap.getHeight();
		maskOfBitmap=new int[max_pos/Integer.SIZE+1];
		for(int i=0;i<maskOfBitmap.length;i++)
			maskOfBitmap[i]=0;
		int pixels1[]=new int[max_pos];
		bitmap.getPixels(pixels1, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
		for(int x=0;x<bitmap.getWidth();x++)
			for(int y=0;y<bitmap.getHeight();y++)
			{
				if((pixels1[y*bitmap.getWidth()+x] & 0xff000000)!=0) {
					visiblePoints++;
					int itemOfMask=y*bitmap.getWidth()+x;
					int posOfMask=itemOfMask/Integer.SIZE;
					int bitOfMask=itemOfMask%Integer.SIZE;
					maskOfBitmap[posOfMask]=maskOfBitmap[posOfMask] | (1 << bitOfMask);
				}
			}
	}

	public void setImageBitmap(Bitmap bmp,float iMul) {
		setImageBitmap(bmp);
		setBitmapiMul(iMul);
	}
	public void setDockPoint(float x,float y)
	{
		dockPointX=x;
		dockPointY=y;
	}

	void touchAnimation()
	{
		Animation animation=new ScaleAnimation(1,1.2f,1,1.2f,getWidth()/2,getHeight()/2);
		animation.setDuration(200);
		animation.setRepeatMode(Animation.REVERSE);
		this.startAnimation(animation);
	}

	@Override
	public boolean onTouch(View view, MotionEvent me) 
	{
		if(isSetOnPosition) {
			if(mGameView.CurrentScreenType!=1)
				return false;
			if(me.getAction()==MotionEvent.ACTION_DOWN && isVisiblePoint((int)me.getX(),(int)me.getY()) &&
					!mGameView.isHasMovingObjectAtPoint(new Point((int)(mClearObject.getLeft()+me.getX()),(int)(mClearObject.getTop()+me.getY())))) {
				mGameView.playItemName(mObjectNumber);
				touchAnimation();
			}
			return false;
		}
		float x=this.getLeft()+me.getX();
		float y=this.getTop()+me.getY();
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if(isVisiblePoint((int)me.getX(),(int)me.getY())==false && mGameView.isHasMovingObjectAtPoint(new Point((int)(mClearObject.getLeft()+me.getX()),(int)(mClearObject.getTop()+me.getY()))))
				return false;
			this.bringToFront();
			mLastTouchX=x;
			mLastTouchY=y;
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			//this.setX(this.getX()-(mLastTouchX-x));
			//this.setY(this.getY()-(mLastTouchY-y));
			int left=(int) (this.getLeft()-(mLastTouchX-x));
			int top=(int) (this.getTop()-(mLastTouchY-y));
			mLastTouchX=x;
			mLastTouchY=y;
			float w=getWidth();
			float h=getHeight();
			//ListObjects[num].setLeft((int) (event.getY()-PrevTouchPos1Y));
			//ListObjects[num].setTop((int)(event.getY()-PrevTouchPos1Y));
			if(left<0)
				left=0;
			if(left+w>=GameView.Owner.getViewWidth())
				left=(int) (GameView.Owner.getViewWidth()-w);
			if(top<0)
				top=0;
			if(top+h>=GameView.Owner.getViewHeight())
				top=(int) (GameView.Owner.getViewHeight()-h);
			mGameView.setBounds(this,left,top,left+w,top+h);
			//this.setX(this.getX()-(mLastTouchX-x));
			//this.setY(this.getY()-(mLastTouchY-y));
			//ListObjects[num].setBottom((int) (ListObjects[num].getTop()+h));
			Resources res=getResources();
        	float delta=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
			RectF check_rect=new RectF(
					mClearObject.getLeft()+(mClearObject.getRight()-mClearObject.getLeft())/2-delta,
					mClearObject.getTop()+(mClearObject.getBottom()-mClearObject.getTop())/2-delta,
					mClearObject.getLeft()+(mClearObject.getRight()-mClearObject.getLeft())/2+delta,
					mClearObject.getTop()+(mClearObject.getBottom()-mClearObject.getTop())/2+delta
					);
			if(check_rect.contains(this.getLeft()+(this.getRight()-this.getLeft())/2, 
					this.getTop()+(this.getBottom()-this.getTop())/2))
			{
				touchAnimation();
				mGameView.SmallClick();
				//this.setOnTouchListener(null);
				isSetOnPosition=true;
				mGameView.setBounds(this,
						mClearObject.getLeft(),
						mClearObject.getTop(),
						mClearObject.getRight(),
						mClearObject.getBottom());
				mGameView.CheckCompleteLevel(mObjectNumber);
			}
			return true;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			}
			break;
		}
		return false;
	}

	public void returnToDock()
	{
		Animation animation=new TranslateAnimation(0,dockPointX-getLeft(),0,dockPointY-getTop());
		animation.setDuration(200);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				MovingImageView.this.clearAnimation();
				mGameView.setBounds(MovingImageView.this,dockPointX,dockPointY,dockPointX+getWidth(),dockPointY+getHeight());
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		this.startAnimation(animation);
	}

	public void recycleBitmap(Bitmap bitmap)
	{
		if(bitmap!=null && bitmap.isRecycled()==false)
			bitmap.recycle();
	}

	@Override
	protected  void  onDetachedFromWindow() {
		super.onDetachedFromWindow();
		recycleBitmap(bitmap);
		bitmap=null;
	}


}
