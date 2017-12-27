package sgolovanov.childrenpiramid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by Sergey on 08.06.2016.
 */

public class StarView extends ImageView {
    int[] starsRes={R.drawable.back_star1, R.drawable.back_star2, R.drawable.back_star3, R.drawable.back_star4, R.drawable.back_star5, R.drawable.back_star6};
    static Bitmap[] starBitmaps=null;
    public StarView(Context context) {
        super(context);
        if(starBitmaps==null)
        {
            starBitmaps=new Bitmap[starsRes.length];
            for(int i=0;i<starsRes.length;i++)
            {
                starBitmaps[i]=BitmapFactory.decodeResource(getResources(),starsRes[i]);
            }
        }
        Random mRnd=new Random();
        this.setImageBitmap(starBitmaps[mRnd.nextInt(starsRes.length)]);
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
        if(backgroundBitmap!=null)
            canvas.drawBitmap(backgroundBitmap,null,new Rect(0,0,getWidth(),getHeight()),paint);
        if(bitmap!=null) {
            Rect drawRect=(imageRect==null)?new Rect(0, 0, getWidth(), getHeight()):imageRect;
            float factorX=1;
            float factorY=1;
            if(isScaleFit) {
                float factorWidth = (float) bitmap.getWidth() / (float) drawRect.width();
                float factorHeight = (float) bitmap.getHeight() / (float) drawRect.height();
                if(factorHeight<factorWidth)
                    factorY=factorHeight/factorWidth;
                else
                    factorX=factorWidth/factorHeight;
            }
            canvas.drawBitmap(bitmap, null, new Rect(
                    (int)((drawRect.width()-drawRect.width()*factorX)/2)+drawRect.left,
                    (int)((drawRect.height()-drawRect.height()*factorY)/2)+drawRect.top,
                    (int)((drawRect.width()+drawRect.width()*factorX)/2)+drawRect.left,
                    (int)((drawRect.height()+drawRect.height()*factorY)/2)+drawRect.top),paint);
        }
    }*/
}
