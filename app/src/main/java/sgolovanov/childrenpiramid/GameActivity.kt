package sgolovanov.childrenpiramid

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout


class GameActivity : Activity() {
    var MyGameView: GameView? = null
    var pm: PowerManager? = null
    var wl: WakeLock? = null
    var mp: MediaPlayer? = null
    @JvmField
    var gameLayout: GameLayout? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.FullscreenTheme)
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(this)
        getSystemService(VIBRATOR_SERVICE)
        //pm = getSystemService(POWER_SERVICE) as PowerManager?
        //wl = pm!!.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag")
        //wl!!.acquire()

        setContentView(R.layout.activity_game)

        MyGameView = findViewById<View?>(R.id.game_view) as GameView?
        GameView.Owner = this
    }

    fun setBounds(image: View, left: Float, top: Float, right: Float, bottom: Float) {
        val lp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        lp.leftMargin = left.toInt()
        lp.topMargin = top.toInt()
        lp.width = (right - left).toInt()
        lp.height = (bottom - top).toInt()
        image.setLayoutParams(lp)
    }

    fun Play(resource: Int) {
        try {
            mp = MediaPlayer.create(this, resource)
            mp!!.start()
        } catch (ex: Exception) {
        }
    }

    override fun onPause() {
        if (MyGameView?.showStarsThread != null)
            MyGameView?.showStarsThread?.interrupt()
        super.onPause()
    }

    override fun onStop() {
        wl?.release()
        super.onStop()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        MyGameView?.post { MyGameView?.startView() }
    }

    override fun onResume() {
        super.onResume()
        wl?.acquire()
    }

    fun startNextStage(levelType: Int) {
        //btnSelectLevel.setImageResource(R.drawable.select_stage);
        val prevGameLayout = gameLayout
        //if(gameLayout!=null)
        //    MyGameView.removeView(gameLayout);
        gameLayout = null

        when (levelType) {
            0 -> {
                gameLayout = ExitScreen(this@GameActivity)
            }
        }
        if (gameLayout != null) {
            val layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gameLayout?.setLayoutParams(layoutParams)
            this@GameActivity.MyGameView!!.addView(gameLayout)
            gameLayout?.StartView()
            val animation = AnimationUtils.loadAnimation(this, R.anim.show)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (prevGameLayout != null) MyGameView!!.removeView(prevGameLayout)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            gameLayout!!.startAnimation(animation)
        }
    }

    fun returnToMenu(isStopSound: Boolean) {
        if (gameLayout != null) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.hide)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    MyGameView?.removeView(gameLayout)
                    gameLayout = null
                    //enableButtons(true);
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            gameLayout?.startAnimation(animation)
        }
    }

    val viewHeight: Int
        get() = MyGameView?.height.orZero()

    val viewWidth: Int
        get() {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }

    companion object {
        @JvmField
        var SHARED_PREFS_NAME: String = "SETTINGS"
    }
}
