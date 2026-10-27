package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

/**
 * Created by Sergey on 19.06.2016.
 */
@SuppressLint("ViewConstructor")
class SymbolImageView(context: Context, parent: GameView, var symbol: Int) :
    MovingImageView(context, parent) {
    override fun onTouch(view: View?, me: MotionEvent): Boolean {
        if (isSetOnPosition) {
            return false//super.onTouch(view, me)
        }
        val posX = left + me.x
        val posY = top + me.y
        when (me.action) {
            MotionEvent.ACTION_DOWN -> {
                bringToFront()
                mLastTouchX = posX
                mLastTouchY = posY
                mGameView.playItemName(mObjectNumber)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                var leftPos = (left - (mLastTouchX - posX)).toInt()
                var topPos = (top - (mLastTouchY - posY)).toInt()
                mLastTouchX = posX
                mLastTouchY = posY
                val w = width.toFloat()
                val h = height.toFloat()
                if (leftPos < 0) leftPos = 0
                if (leftPos + w >= GameView.Owner.viewWidth)
                    leftPos = (GameView.Owner.viewWidth - w).toInt()
                if (topPos < 0) topPos = 0
                if (topPos + h >= GameView.Owner.viewHeight)
                    topPos = (GameView.Owner.viewHeight - h).toInt()
                mGameView.setBounds(
                    this,
                    leftPos.toFloat(),
                    topPos.toFloat(),
                    leftPos + w,
                    topPos + h
                )
                val delta = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f,
                    resources.displayMetrics
                )
                mGameView.listObjects
                    .filter {
                        (it as? SymbolImageView)?.let {
                            it.symbol == symbol && !isSetOnPosition
                        } ?: false
                    }
                    .forEach {
                        val currentSymbolImageView = it as SymbolImageView
                        currentSymbolImageView.mClearObject?.let { currentClearObject ->
                            val checkRect = RectF(
                                currentClearObject.left + (currentClearObject.right - currentClearObject.left) / 2 - delta,
                                currentClearObject.top + (currentClearObject.bottom - currentClearObject.top) / 2 - delta,
                                currentClearObject.left + (currentClearObject.right - currentClearObject.left) / 2 + delta,
                                currentClearObject.top + (currentClearObject.bottom - currentClearObject.top) / 2 + delta
                            )
                            if (checkRect.contains(
                                    (left + (right - left) / 2).toFloat(),
                                    (top + (bottom - top) / 2).toFloat()
                                )
                            ) {
                                if (currentSymbolImageView != this) {
                                    Log.i("currentSymbolImageView", "currentSymbolImageView != this")
                                    val tmpValue = currentSymbolImageView.mClearObject
                                    currentSymbolImageView.mClearObject = mClearObject
                                    mClearObject = tmpValue
                                } else {
                                    Log.i("currentSymbolImageView", "currentSymbolImageView == this")
                                }
                                mGameView.smallClick()
                                isSetOnPosition = true
                                mClearObject?.let { mClearObject ->
                                    mGameView.setBounds(
                                        this,
                                        mClearObject.left.toFloat(),
                                        mClearObject.top.toFloat(),
                                        mClearObject.right.toFloat(),
                                        mClearObject.bottom.toFloat()
                                    )
                                }
                                touchAnimation()
                                mGameView.checkCompleteLevelWithGoodSound(mObjectNumber)
                            }
                        }
                    }
                return true
            }

            else -> {
                return false
            }
        }
    }
}
