package sgolovanov.childrenpiramid

import android.graphics.drawable.AnimationDrawable

class AnimationDrawable2 : AnimationDrawable() {
    interface IAnimationFinishListener {
        fun onAnimationFinished()
    }

    private var finished = false
    var animationFinishListener: IAnimationFinishListener? = null

    override fun selectDrawable(idx: Int): Boolean {
        val ret = super.selectDrawable(idx)

        if ((idx != 0) && (idx == getNumberOfFrames() - 1)) {
            if (!finished) {
                finished = true
                if (animationFinishListener != null) animationFinishListener!!.onAnimationFinished()
            }
        }

        return ret
    }
}