package sgolovanov.childrenpiramid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Sergey on 15.06.2016.
 */
public class AnimatedImageView extends View {
    Point centralPoint;
    boolean isHorizontalReverse=false;
    //Bitmap backgroundBitmap=null;
    public AnimatedImageView(Context context) {
        super(context);
    }
    public void setCentralPoint(Point point)
    {
        this.centralPoint=point;
    }
    public Point getCentralPoint()
    {
        return centralPoint;
    }
    Bitmap bitmap=null;

    public void setHorizontalReverse(boolean isHorizontalReverse)
    {
        this.isHorizontalReverse=isHorizontalReverse;
        if(isHorizontalReverse)
            this.setScaleX(-1);
        else
            this.setScaleX(1);
    }

    public void setImageResource(int resID)
    {
        if(GameView.isScaledBitmaps())
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;
            bitmap=BitmapFactory.decodeResource(getResources(), resID, options);
        }
        else
            bitmap=BitmapFactory.decodeResource(getResources(),resID);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        Rect drawRect=new Rect(0,0,getWidth(),getHeight());
        /*if(backgroundBitmap!=null)
            canvas.drawBitmap(backgroundBitmap,null,drawRect,paint);*/
        if(bitmap!=null)
            canvas.drawBitmap(bitmap, null, drawRect,paint);
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
