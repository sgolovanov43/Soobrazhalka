package sgolovanov.childrenpiramid

import android.content.Context
import android.graphics.RectF
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * Created by Sergey on 12.06.2016.
 */
class ColorImageView(context: Context, parent: GameView, var picture: String?) :
    MovingImageView(context, parent) {
    public override fun onTouch(view: View?, me: MotionEvent): Boolean {
        if (isSetOnPosition) return false
        val xPos = left + me.x
        val yPos = top + me.y
        when (me.action) {
            MotionEvent.ACTION_DOWN -> {
                this.bringToFront()
                mLastTouchX = xPos
                mLastTouchY = yPos
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                //this.setX(this.getX()-(mLastTouchX-x));
                //this.setY(this.getY()-(mLastTouchY-y));
                var left = (left - (mLastTouchX - xPos)).toInt()
                var top = (top - (mLastTouchY - yPos)).toInt()
                mLastTouchX = xPos
                mLastTouchY = yPos
                val w = width.toFloat()
                val h = height.toFloat()
                if (left < 0) left = 0
                if (left + w >= GameView.Owner?.viewWidth.orZero()) left =
                    (GameView.Owner?.viewWidth.orZero() - w).toInt()
                if (top < 0) top = 0
                if (top + h >= GameView.Owner?.viewHeight.orZero()) top =
                    (GameView.Owner?.viewHeight.orZero() - h).toInt()
                mGameView.setBounds(this, left.toFloat(), top.toFloat(), left + w, top + h)
                val res = resources
                val delta = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    res.displayMetrics
                )
                val checkRect = RectF(
                    mClearObject?.left.orZero().toFloat(),
                    mClearObject?.top.orZero().toFloat(),
                    mClearObject?.right.orZero().toFloat(),
                    mClearObject?.bottom.orZero().toFloat()
                )
                if (checkRect.contains(
                        (this.left + (this.right - this.left) / 2).toFloat(),
                        (this.top + (this.bottom - this.top) / 2).toFloat()
                    )
                ) {
                    mGameView.smallClick()
                    setOnTouchListener(null)
                    isSetOnPosition = true
                    visibility = INVISIBLE
                    mClearObject?.setImageBitmap(mGameView.getImageFromObb(picture))
                    mGameView.checkCompleteLevel(mObjectNumber)
                }
                return true
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                returnToDock()
            }
        }
        return false
    }
}
