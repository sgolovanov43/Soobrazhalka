package sgolovanov.childrenpiramid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Sergey on 19.06.2016.
 */
public class SymbolImageView extends MovingImageView {
    int symbol;

    public SymbolImageView(Context context, GameView parent,int symbol) {
        super(context, parent);
        this.symbol=symbol;
    }

    @Override
    public boolean onTouch(View view, MotionEvent me)
    {
        if(isSetOnPosition) {
            /*if(me.getAction()==MotionEvent.ACTION_DOWN)
                touchAnimation();
                mGameView.playItemName(mObjectNumber);*/
            return super.onTouch(view,me);
        }
        float x=this.getLeft()+me.getX();
        float y=this.getTop()+me.getY();
        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                this.bringToFront();
                mLastTouchX=x;
                mLastTouchY=y;
                mGameView.playItemName(mObjectNumber);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                //this.setX(this.getX()-(mLastTouchX-x));
                //this.setY(this.getY()-(mLastTouchY-y));
                int left = (int) (this.getLeft() - (mLastTouchX - x));
                int top = (int) (this.getTop() - (mLastTouchY - y));
                mLastTouchX = x;
                mLastTouchY = y;
                float w = getWidth();
                float h = getHeight();
                //ListObjects[num].setLeft((int) (event.getY()-PrevTouchPos1Y));
                //ListObjects[num].setTop((int)(event.getY()-PrevTouchPos1Y));
                if (left < 0)
                    left = 0;
                if (left + w >= GameView.Owner.getViewWidth())
                    left = (int) (GameView.Owner.getViewWidth() - w);
                if (top < 0)
                    top = 0;
                if (top + h >= GameView.Owner.getViewHeight())
                    top = (int) (GameView.Owner.getViewHeight() - h);
                mGameView.setBounds(this, left, top, left + w, top + h);
                //this.setX(this.getX()-(mLastTouchX-x));
                //this.setY(this.getY()-(mLastTouchY-y));
                //ListObjects[num].setBottom((int) (ListObjects[num].getTop()+h));
                Resources res = getResources();
                float delta = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, res.getDisplayMetrics());
                for (int i = 0; i < mGameView.ListObjects.length; i++) {
                    SymbolImageView currentSymbolImageView=(SymbolImageView)mGameView.ListObjects[i];
                    if(currentSymbolImageView.symbol!=symbol || currentSymbolImageView.isSetOnPosition)
                        continue;
                    RectF check_rect = new RectF(
                            currentSymbolImageView.mClearObject.getLeft() + (currentSymbolImageView.mClearObject.getRight() - currentSymbolImageView.mClearObject.getLeft()) / 2 - delta,
                            currentSymbolImageView.mClearObject.getTop() + (currentSymbolImageView.mClearObject.getBottom() - currentSymbolImageView.mClearObject.getTop()) / 2 - delta,
                            currentSymbolImageView.mClearObject.getLeft() + (currentSymbolImageView.mClearObject.getRight() - currentSymbolImageView.mClearObject.getLeft()) / 2 + delta,
                            currentSymbolImageView.mClearObject.getTop() + (currentSymbolImageView.mClearObject.getBottom() - currentSymbolImageView.mClearObject.getTop()) / 2 + delta
                    );
                    if (check_rect.contains(this.getLeft() + (this.getRight() - this.getLeft()) / 2,
                            this.getTop() + (this.getBottom() - this.getTop()) / 2)) {
                        if(currentSymbolImageView!=this)
                        {
                            ShadowImageView tmpClearObject=currentSymbolImageView.mClearObject;
                            currentSymbolImageView.mClearObject=this.mClearObject;
                            this.mClearObject=tmpClearObject;
                        }
                        touchAnimation();
                        mGameView.SmallClick();
                        //this.setOnTouchListener(null);
                        isSetOnPosition = true;
                        mGameView.setBounds(this,
                                mClearObject.getLeft(),
                                mClearObject.getTop(),
                                mClearObject.getRight(),
                                mClearObject.getBottom());
                        mGameView.CheckCompleteLevelWithGoodSound(mObjectNumber);
                    }
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
