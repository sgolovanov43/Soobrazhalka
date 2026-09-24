package sgolovanov.childrenpiramid

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import java.util.Random

/**
 * Created by Сергей Голованов on 03.08.2015.
 */
abstract class GameLayout(context: Context?) : RelativeLayout(context) {
    protected var Owner: GameActivity

    //protected  AudioManager audio;
    protected var mRnd: Random = Random()
    protected var VibrationType: Boolean = true
    protected var BlinkType: Boolean = true

    //protected  Vibrator vibrator;
    var Left: Float = 0f
    var Top: Float = 0f
    var screen_factor: Float = 1f
    var bmp_factor: Float = 1f
    var isComplete: Boolean = false

    init {
        Owner = context as GameActivity
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //Owner.Stop();
    }

    fun setBounds(image: View, left: Float, top: Float, right: Float, bottom: Float) {
        /*image.setLeft((int)left);
        image.setTop((int)top);
        image.setRight((int)right);
        image.setBottom((int)bottom);
        */
        val lp = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        lp.leftMargin = left.toInt()
        lp.topMargin = top.toInt()
        lp.width = (right - left).toInt()
        lp.height = (bottom - top).toInt()
        image.setLayoutParams(lp)
    }

    fun StartView() {
        try {
            //audio = (AudioManager) Owner.getSystemService(Context.AUDIO_SERVICE);
            //vibrator = (Vibrator)Owner.getSystemService(Context.VIBRATOR_SERVICE);
            StartLevel()
        } catch (ex: Exception) {
            Log.d("MY_TEST_LOG", ex.message!!)
        }
    }

    fun StartLevel() {
        this.post(object : Runnable {
            override fun run() {
                //isStartingLevel = true;
                //ClearLevel();
                val py =
                    Owner.viewHeight.toFloat() / 620.0f //(float) ListBackground.getDrawable().getIntrinsicHeight();
                val px =
                    Owner.viewWidth.toFloat() / 1200f //(float) ListBackground.getDrawable().getIntrinsicWidth();
                if (px < py) screen_factor = px
                else screen_factor = py
                //float screen_factor=px;//((float)getWidth()/(float)getHeight())/(640f/310f);
                bmp_factor =
                    screen_factor //620f/ListBackground.getHeight();///(620f/screen_factor);
                //float grid_scale = (ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
                Left = (Owner.viewWidth - bmp_factor * 1200f) / 2f
                Top = (Owner.viewHeight - bmp_factor * 620f) / 2f

                LoadLevel()
            }
        })
    }

    abstract fun LoadLevel()

    fun Play(resource: Int, is_looping: Boolean) {
        Owner.Play(resource)
    }
}