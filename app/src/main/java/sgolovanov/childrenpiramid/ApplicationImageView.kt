package sgolovanov.childrenpiramid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 15.06.2016.
 */
public class ApplicationImageView extends MovingImageView {
    List<Point> points=null;
    float strokeWidth=2;
    int currentPoint=0;
    //static Bitmap nozhnicy=null;
    ImageView nozhnicyView=null;
    Context mContext;
    int rotateAngle=0;
    final int stepPoints=5;
    public ApplicationImageView(Context context, GameView parent) {
        super(context, parent);
        mContext=context;
        setOnTouchListener(null);
    }

    @Override
    protected  void  onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(nozhnicyView!=null)
            mGameView.removeView(nozhnicyView);
        nozhnicyView=null;
    }

    public void setRotateAngle(int rotateAngle)
    {
        this.rotateAngle=rotateAngle;
        //this.setRotation(rotateAngle);
    }
    public void setStrokeWidth(float strokeWidth)
    {
        this.strokeWidth=strokeWidth;
    }
    public void setImageBitmap(Bitmap bmp,float iMul) {
        super.setImageBitmap(bmp,iMul);
        points = new ArrayList<Point>();
        int[] pixels2 = new int[bitmap.getHeight() * bitmap.getWidth()];
        bitmap.getPixels(pixels2, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int row=bitmap.getHeight()/2;
        int col=0;
        int startSearchNum=0;
        for (int x = 0; x < bitmap.getWidth(); x++) {
            if(Color.alpha(pixels2[row*bitmap.getWidth()+x])!=0)
            {
                col=x;
                points.add(new Point(col,row));
                break;
            }
        }
        while(true)
        {
            int searchNum=getSearchPoint(pixels2,col,row,bitmap.getWidth(),bitmap.getHeight(),startSearchNum);
            if(searchNum<0)
                return;
            row=getRowBySearchNum(row,searchNum);
            col=getColBySearchNum(col,searchNum);
            if(points.get(0).x==col && points.get(0).y==row)
                break;
            points.add(new Point(col,row));
            startSearchNum=getSearchNumByPoints(points.get(points.size()-2),points.get(points.size()-1));
        }
        nozhnicyView=new ImageView(mContext);
        nozhnicyView.setBackgroundResource(R.drawable.animaciya_nozhnicy);
        mGameView.addView(nozhnicyView);
        AnimationDrawable startAnimation=(AnimationDrawable)nozhnicyView.getBackground();
        startAnimation.start();
        setNozhnicyViewPosition();
        nozhnicyView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                switch (me.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE: {
                        nozhnicyView.bringToFront();
                        int delta= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());
                        RectF rectF=getNozhnicyRect();
                        PointF curPoint=getCurrentScreenPoint();
                        PointF touchPoint=new PointF(rectF.left+me.getX(),rectF.top+me.getY());
                        RectF checkRect=new RectF(
                                touchPoint.x-delta,
                                touchPoint.y-delta,
                                touchPoint.x+delta,
                                touchPoint.y+delta);
                        if(checkRect.contains(curPoint.x,curPoint.y))
                        {
                            currentPoint+=stepPoints;
                            if(currentPoint>=points.size())
                            {
                                //currentPoint--;
                                mGameView.removeView(nozhnicyView);
                                nozhnicyView=null;
                                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) ApplicationImageView.this.getLayoutParams();
                                int imageLeft=layoutParams.leftMargin;//imageView.getLeft();
                                final int imageTop=layoutParams.topMargin;//imageView.getTop();
                                final RelativeLayout.LayoutParams layoutParams2= (RelativeLayout.LayoutParams) ApplicationImageView.this.mClearObject.getLayoutParams();
                                final int imageLeft2=layoutParams2.leftMargin;//imageView.getLeft();
                                final int imageTop2=layoutParams2.topMargin;//imageView.getTop();
                                Animation animationTranslate=new TranslateAnimation(0,imageLeft2-imageLeft,0,imageTop2-imageTop);
                                Animation animationRotate=new RotateAnimation(0,rotateAngle,layoutParams.width/2,layoutParams.height/2);
                                AnimationSet animationSet=new AnimationSet(true);
                                animationSet.addAnimation(animationRotate);
                                animationSet.addAnimation(animationTranslate);
                                animationSet.setDuration(600);
                                //animation.setFillAfter(true);
                                animationTranslate.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        isSetOnPosition=true;
                                        mGameView.setBounds(ApplicationImageView.this,
                                                imageLeft2,
                                                imageTop2,
                                                imageLeft2+layoutParams2.width,
                                                imageTop2+layoutParams2.height);
                                        ApplicationImageView.this.setRotation(rotateAngle);
                                        ApplicationImageView.this.clearAnimation();
                                        //ApplicationImageView.this.invalidate();
                                        mGameView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mGameView.CheckCompleteLevel(mObjectNumber);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                ApplicationImageView.this.startAnimation(animationSet);
                                //nozhnicyView.setOnTouchListener(null);
                            }
                            else
                            {
                                setNozhnicyViewPosition();
                            }
                            ApplicationImageView.this.invalidate();
                        }
                    }
                }
                return false;
            }
        });
    }

    public float getNozhnicyWidth()
    {
        return strokeWidth*60;
    }

    public PointF getCurrentScreenPoint()
    {
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) this.getLayoutParams();
        int imageLeft=layoutParams.leftMargin;//imageView.getLeft();
        int imageTop=layoutParams.topMargin;//imageView.getTop();
        return new PointF(imageLeft+points.get(currentPoint).x*bitmapiMul,imageTop+points.get(currentPoint).y*bitmapiMul);
    }

    public RectF getNozhnicyRect()
    {
        float nozhnicyWidth=getNozhnicyWidth();
        PointF screenPoint=getCurrentScreenPoint();
        RectF rectF=new RectF(
                screenPoint.x-nozhnicyWidth/2,
                screenPoint.y-nozhnicyWidth/2,
                screenPoint.x+nozhnicyWidth/2,
                screenPoint.y+nozhnicyWidth/2);
        return rectF;
    }

    void setNozhnicyViewPosition()
    {
        mGameView.setBounds(nozhnicyView,getNozhnicyRect());
        Point point1=points.get(currentPoint);
        Point point2=null;
        if(currentPoint<points.size()-stepPoints-1)
            point2=points.get(currentPoint+stepPoints);
        else
            point2=points.get(0);
        int angle=getAngleNumByPoints(point1,point2);
        nozhnicyView.setRotation(angle);
    }

    int getSearchPoint(int[] pixels,int x,int y,int width,int height,int startSearchNum)
    {
        int searchNum=startSearchNum;
        while(true)
        {
            searchNum++;
            if(searchNum>7)
                searchNum=0;
            if(searchNum==startSearchNum)
                return -1;
            int row=getRowBySearchNum(y,searchNum);
            int col=getColBySearchNum(x,searchNum);
            if(col<0 || col>=width || row<0 || row>=height)
            {
                //searchNum++;
                continue;
            }
            if(Color.alpha(pixels[row*width+col])!=0)
                return searchNum;
        }
    }

    int getColBySearchNum(int x,int searchNum)
    {
        int col=x;
        if(searchNum==0 || searchNum==1 || searchNum==7) {
            col--;
        }
        if(searchNum==3 || searchNum==4 || searchNum==5) {
            col++;
        }
        return col;
    }

    int getRowBySearchNum(int y,int searchNum)
    {
        int row=y;
        if(searchNum==5 || searchNum==6 || searchNum==7) {
            row++;
        }
        if(searchNum==1 || searchNum==2 || searchNum==3) {
            row--;
        }
        return row;
    }

    int getSearchNumByPoints(Point p1,Point p2)
    {
        int deltaX=p1.x-p2.x;
        int deltaY=p1.y-p2.y;
        if(deltaX==0)
        {
            if(deltaY==-1)
                return 2;
            else if(deltaY==1)
                return 6;
        }
        else if(deltaX==-1)
        {
            if(deltaY==-1)
                return 1;
            else if(deltaY==1)
                return 7;
            else if(deltaY==0)
                return 0;
        }
        else if(deltaX==1)
        {
            if(deltaY==-1)
                return 3;
            else if(deltaY==1)
                return 5;
            else if(deltaY==0)
                return 4;
        }
        return -1;
    }

    public int getAngleNumByPoints(Point p1,Point p2)
    {
        int deltaX=p2.x-p1.x;
        int deltaY=p2.y-p1.y;
        float angle1 = (float) (Math.atan2(deltaY, deltaX) / Math.PI * 180)+90;
        return (int) angle1;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(points!=null && points.size()>1 && isSetOnPosition==false) {
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.STROKE);
            {
                paint.setStrokeWidth(strokeWidth);
                paint.setPathEffect(new DashPathEffect(new float[]{strokeWidth * 5, strokeWidth * 2}, 0));
                Path path = new Path();
                path.moveTo(points.get(points.size() - 1).x * bitmapiMul, points.get(0).y * bitmapiMul);
                for (int i = points.size() - 1; i >= currentPoint; i--) {
                    path.lineTo(points.get(i).x * bitmapiMul, points.get(i).y * bitmapiMul);
                }
                canvas.drawPath(path,paint);
            }
            if(currentPoint>0)
            {
                paint.setStrokeWidth(strokeWidth/2);
                paint.setColor(Color.BLACK);
                int startPoint=currentPoint;
                if(currentPoint>=points.size())
                {
                    startPoint=points.size()-1;
                    canvas.drawLine(points.get(0).x * bitmapiMul, points.get(0).y * bitmapiMul, points.get(startPoint).x * bitmapiMul, points.get(startPoint).y * bitmapiMul, paint);
                }
                for (int i = startPoint; i > 0; i--) {
                    canvas.drawLine(points.get(i).x * bitmapiMul, points.get(i).y * bitmapiMul, points.get(i - 1).x * bitmapiMul, points.get(i - 1).y * bitmapiMul, paint);
                }
            }
        }
    }

}
