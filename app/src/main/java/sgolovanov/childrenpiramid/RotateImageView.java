package sgolovanov.childrenpiramid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Sergey on 11.06.2016.
 */
public class RotateImageView  extends MovingImageView implements View.OnTouchListener
{
    PointF centralPoint=null;
    public RotateImageView(Context context, GameView parent)
    {
        super(context,parent);
        setOnTouchListener(this);
    }

    public void setCentralPoint(float x0,float y0)
    {
        centralPoint=new PointF(x0,y0);
    }

    public boolean isVisiblePoint(int x,int y)
    {
        float radius= (float) Math.sqrt((bitmap.getHeight()/2)*(bitmap.getHeight()/2)+(bitmap.getWidth()/2)*(bitmap.getWidth()/2));
        int obj_size= (int) (radius*2);
        int itemOfMask= (int) (((int)(y/bitmapiMul))*obj_size+x/bitmapiMul);
        int posOfMask=itemOfMask/Integer.SIZE;
        int bitOfMask=itemOfMask%Integer.SIZE;
        if(posOfMask>=0 && posOfMask < maskOfBitmap.length && (maskOfBitmap[posOfMask] & (1 << bitOfMask))!=0)
            return true;
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        if(bitmap!=null) {
            //Rect drawRect=new Rect(0, 0, getWidth(), getHeight());
            if(centralPoint==null)
                centralPoint=new PointF(getWidth()/2f,getHeight()/2f);
            if(currentAngle!=0) {
                canvas.save();
                float radius= (float) Math.sqrt((bitmap.getHeight()/2f)*(bitmap.getHeight()/2f)+(bitmap.getWidth()/2f)*(bitmap.getWidth()/2f));
                float obj_size=radius*2;
                Bitmap bitmapTmp=Bitmap.createBitmap((int)obj_size,(int)obj_size, Bitmap.Config.ARGB_8888);
                Canvas canvasTmp=new Canvas(bitmapTmp);
                canvasTmp.rotate(currentAngle, obj_size / 2, obj_size / 2);
                //canvas.rotate(currentAngle);
                canvasTmp.drawBitmap(bitmap, null, new RectF(
                        obj_size/2f-bitmap.getWidth()/2f,
                        obj_size/2f-bitmap.getHeight()/2f,
                        obj_size/2f+bitmap.getWidth()/2f,
                        obj_size/2f+bitmap.getHeight()/2f),paint);
                canvas.drawBitmap(bitmapTmp,null,new RectF(
                        centralPoint.x-obj_size/2f*bitmapiMul,
                        centralPoint.y-obj_size/2f*bitmapiMul,
                        centralPoint.x+obj_size/2f*bitmapiMul,
                        centralPoint.y+obj_size/2f*bitmapiMul
                ),paint);
                int max_pos=bitmapTmp.getWidth()*bitmapTmp.getHeight();
                int[] pixels1=new int[max_pos];
                maskOfBitmap=new int[max_pos/Integer.SIZE+1];
                for(int i=0;i<maskOfBitmap.length;i++)
                    maskOfBitmap[i]=0;
                bitmapTmp.getPixels(pixels1, 0, bitmapTmp.getWidth(), 0, 0, bitmapTmp.getWidth(), bitmapTmp.getHeight());
                for(int x=0;x<bitmapTmp.getWidth();x++)
                    for(int y=0;y<bitmapTmp.getHeight();y++)
                    {
                        if((pixels1[y*bitmapTmp.getWidth()+x] & 0xff000000)!=0) {
                            visiblePoints++;
                            int itemOfMask=y*bitmapTmp.getWidth()+x;
                            int posOfMask=itemOfMask/Integer.SIZE;
                            int bitOfMask=itemOfMask%Integer.SIZE;
                            maskOfBitmap[posOfMask]=maskOfBitmap[posOfMask] | (1 << bitOfMask);
                        }
                    }
                bitmapTmp.recycle();
                canvas.restore();
            }
            else {
                RectF bitmapRect=new RectF(centralPoint.x-bitmap.getWidth()/2f*bitmapiMul, centralPoint.y-bitmap.getHeight()/2f*bitmapiMul, centralPoint.x+bitmap.getWidth()/2f*bitmapiMul, centralPoint.y+bitmap.getHeight()/2f*bitmapiMul);
                canvas.drawBitmap(bitmap, null, bitmapRect, paint);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent me)
    {
        if(isSetOnPosition || me.getActionIndex()>0) {
            //mGameView.playItemName(mObjectNumber);
            return super.onTouch(view,me);
        }
        float x=me.getX();
        float y=me.getY();
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
                //float vectorAB=(y - getHeight()/2)
                float x1=x;
                float x0=getWidth()/2;
                float x2=mLastTouchX;
                float y1=y;
                float y0=getHeight()/2;
                float y2=mLastTouchY;
                float angle1 = (float) (Math.atan2(y1 - y0, x1 - x0) / Math.PI * 180);
                float angle2 = (float) (Math.atan2(y2 - y0, x2 - x0) / Math.PI * 180);
                //float currentAngle=this.getAngle();
                float angle = angle1-angle2;
                Log.v("Angle: ", String.format("a=%.1f x1=%.1f x2=%.1f y1=%.1f y2=%.1f",angle,x1,x2,y1,y2));
                if(Math.abs(angle)<=1)
                    return true;
                mLastTouchX=x;
                mLastTouchY=y;
                float w=getWidth();
                float h=getHeight();
                angle+=this.getAngle();
                angle = (angle < 0) ? angle + 360 : ((angle>360) ? angle - 360 : angle);
                if(angle<5 || angle>355)
                {
                    touchAnimation();
                    mGameView.SmallClick();
                    //this.setOnTouchListener(null);
                    isSetOnPosition=true;
                    this.setAngle(0);
                    mGameView.CheckCompleteLevel(mObjectNumber);
                }
                else
                    this.setAngle(/*getRotation()+*/angle);
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
            }
            break;
        }
        return false;
    }
}
