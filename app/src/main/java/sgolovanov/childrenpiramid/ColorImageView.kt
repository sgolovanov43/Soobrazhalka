package sgolovanov.childrenpiramid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Sergey on 12.06.2016.
 */
public class ColorImageView extends MovingImageView {
    String picture;
    public ColorImageView(Context context, GameView parent, String picture) {
        super(context, parent);
        this.picture=picture;
    }

    @Override
    public boolean onTouch(View view, MotionEvent me)
    {
        if(isSetOnPosition)
            return false;
        float x=this.getLeft()+me.getX();
        float y=this.getTop()+me.getY();
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN: {
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
                float delta= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
                RectF check_rect=new RectF(
                        mClearObject.getLeft(),
                        mClearObject.getTop(),
                        mClearObject.getRight(),
                        mClearObject.getBottom()
                );
                if(check_rect.contains(this.getLeft()+(this.getRight()-this.getLeft())/2,
                        this.getTop()+(this.getBottom()-this.getTop())/2))
                {
                    mGameView.SmallClick();
                    this.setOnTouchListener(null);
                    isSetOnPosition=true;
                    /*mGameView.setBounds(this,
                            mClearObject.getLeft(),
                            mClearObject.getTop(),
                            mClearObject.getRight(),
                            mClearObject.getBottom());*/
                    this.setVisibility(INVISIBLE);
                    mClearObject.setImageBitmap(mGameView.getImageFromObb(picture, ".png"));
                    mGameView.CheckCompleteLevel(mObjectNumber);
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                returnToDock();
            }
            break;
        }
        return false;
    }

}
