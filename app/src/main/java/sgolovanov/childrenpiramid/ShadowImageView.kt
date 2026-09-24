package sgolovanov.childrenpiramid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import java.nio.ByteBuffer;

/**
 * Created by Sergey on 13.06.2016.
 */
public class ShadowImageView extends View {
    Bitmap bitmap;
    int[] maskOfBitmap=null;
    int visiblePoints=0;
    int shadowColor=Color.BLACK;
    public ShadowImageView(Context context) {
        super(context);
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
        invalidate();
    }

    public void setShadowBitmap(Bitmap bmp,int color)
    {
        shadowColor=color;
        int max_pos=bmp.getWidth()*bmp.getHeight();
        int pixels1[]=new int[max_pos];
        byte pixels2[]=new byte[max_pos];
        bmp.getPixels(pixels1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        visiblePoints=0;
        maskOfBitmap=new int[max_pos/Integer.SIZE+1];
        for(int i=0;i<maskOfBitmap.length;i++)
            maskOfBitmap[i]=0;
        ByteBuffer buffer = ByteBuffer.allocate(max_pos);
        for(int y=0;y<bmp.getHeight();y++)
        {
            int row_pos=y*bmp.getWidth();
            int prev_row_pos=(y-1)*bmp.getWidth();
            int prev_prev_row_pos=(y-2)*bmp.getWidth();
            int prev_prev_prev_row_pos=(y-3)*bmp.getWidth();
            int next_row_pos=(y+1)*bmp.getWidth();
            int next_next_row_pos=(y+2)*bmp.getWidth();
            int next_next_next_row_pos=(y+3)*bmp.getWidth();
            for(int x=0;x<bmp.getWidth();x++)
            {
                int pixel=pixels1[row_pos+x];
                if(pixel!= Color.TRANSPARENT)
                {
                    visiblePoints++;
                    int itemOfMask=row_pos+x;
                    int posOfMask=itemOfMask/Integer.SIZE;
                    int bitOfMask=itemOfMask%Integer.SIZE;
                    maskOfBitmap[posOfMask]=maskOfBitmap[posOfMask] | (1 << bitOfMask);
                    boolean isPrevRowTransparent=true;
                    boolean isPrevPrevRowTransparent=true;
                    boolean isPrevPrevPrevRowTransparent=true;
                    if(prev_row_pos>=0)
                        isPrevRowTransparent=pixels1[prev_row_pos+x]==Color.TRANSPARENT;
                    if(prev_prev_row_pos>=0)
                        isPrevPrevRowTransparent=pixels1[prev_prev_row_pos+x]==Color.TRANSPARENT;
                    if(prev_prev_prev_row_pos>=0)
                        isPrevPrevPrevRowTransparent=pixels1[prev_prev_prev_row_pos+x]==Color.TRANSPARENT;
                    boolean isPrevPosTransparent=true;
                    boolean isPrevPrevPosTransparent=true;
                    boolean isPrevPrevPrevPosTransparent=true;
                    if(x>0)
                        isPrevPosTransparent=pixels1[row_pos+x-1]==Color.TRANSPARENT;
                    if(x>1)
                        isPrevPrevPosTransparent=pixels1[row_pos+x-2]==Color.TRANSPARENT;
                    if(x>2)
                        isPrevPrevPrevPosTransparent=pixels1[row_pos+x-3]==Color.TRANSPARENT;

                    boolean isNextRowTransparent=true;
                    boolean isNextNextRowTransparent=true;
                    boolean isNextNextNextRowTransparent=true;
                    if(y<bmp.getHeight()-1)
                        isNextRowTransparent=pixels1[next_row_pos+x]==Color.TRANSPARENT;
                    if(y<bmp.getHeight()-2)
                        isNextNextRowTransparent=pixels1[next_next_row_pos+x]==Color.TRANSPARENT;
                    if(y<bmp.getHeight()-3)
                        isNextNextNextRowTransparent=pixels1[next_next_next_row_pos+x]==Color.TRANSPARENT;
                    boolean isNextPosTransparent=true;
                    boolean isNextNextPosTransparent=true;
                    boolean isNextNextNextPosTransparent=true;
                    if(x<bmp.getWidth()-1)
                        isNextPosTransparent=pixels1[row_pos+x+1]==Color.TRANSPARENT;
                    if(x<bmp.getWidth()-2)
                        isNextNextPosTransparent=pixels1[row_pos+x+2]==Color.TRANSPARENT;
                    if(x<bmp.getWidth()-3)
                        isNextNextNextPosTransparent=pixels1[row_pos+x+3]==Color.TRANSPARENT;

                    if(isPrevPosTransparent || isPrevPrevPosTransparent || isPrevPrevPrevRowTransparent || isPrevRowTransparent || isPrevPrevRowTransparent || isPrevPrevPrevPosTransparent ||
                            isNextPosTransparent || isNextNextPosTransparent || isNextNextNextRowTransparent || isNextRowTransparent || isNextNextRowTransparent || isNextNextNextPosTransparent)
                        buffer.put(row_pos+x, (byte) Color.alpha(pixel));//|0x00ffffff;
                    else
                        buffer.put(row_pos+x, (byte) (Color.alpha(pixel) / 2));
                }
                else
                    buffer.put(row_pos+x, (byte) 0);
            }
        }
        bitmap=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ALPHA_8);
        bitmap.copyPixelsFromBuffer(buffer);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        if(bitmap!=null) {
            Rect drawRect = new Rect(0, 0, getWidth(), getHeight());
            paint.setColor(shadowColor);
            canvas.drawBitmap(bitmap, null, drawRect, paint);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
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
        int itemOfMask=y*bitmap.getWidth()+x;
        int posOfMask=itemOfMask/Integer.SIZE;
        int bitOfMask=itemOfMask%Integer.SIZE;
        if(posOfMask < maskOfBitmap.length && (maskOfBitmap[posOfMask] & (1 << bitOfMask))!=0)
            return true;
        return false;
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
