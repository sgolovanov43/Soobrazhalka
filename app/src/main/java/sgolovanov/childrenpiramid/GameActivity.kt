package sgolovanov.childrenpiramid

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import sgolovanov.childrenpiramid.GameView.Companion.Owner
import sgolovanov.childrenpiramid.GameView.Companion.currentScreenType
import sgolovanov.childrenpiramid.databinding.ActivityGameBinding
import kotlin.math.roundToInt


class GameActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameBinding

    private val adSize: BannerAdSize
        get() {
            // Calculate the width of the ad, taking into account the padding in the ad container.
            var adWidthPixels = binding.banner.width
            if (adWidthPixels == 0) {
                // If the ad hasn't been laid out, default to the full screen width
                adWidthPixels = resources.displayMetrics.widthPixels
            }
            val adWidth = (adWidthPixels / resources.displayMetrics.density).roundToInt()

            return BannerAdSize.sticky(this, adWidth)
        }

    private var bannerAd: BannerAdView? = null

    var pm: PowerManager? = null
    var wl: WakeLock? = null
    var mp: MediaPlayer? = null
    var gameLayout: GameLayout? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.FullscreenTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PreferenceManager.getDefaultSharedPreferences(this)
        getSystemService(VIBRATOR_SERVICE)
        pm = getSystemService(POWER_SERVICE) as PowerManager?
        wl = pm?.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.javaClass.simpleName)
        wl?.acquire(10*60*1000L /*10 minutes*/)

        initiateAds()

        GameView.Owner = this

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                with(myGameView()) {
                    when (currentScreenType) {
                        100 -> exit()
                        1000 -> {
                            removeView(Owner.gameLayout)
                            currentScreenType = 100
                        }

                        1, 200, 300 -> {
                            smallClick()
                            returnToMainMenu()
                            currentScreenType = 100
                        }
                        2 -> {
                            smallClick()
                            startSelectLevel(3)
                            currentScreenType = 200
                        }
                        102, 502, 3, 5, 6 -> {
                            smallClick()
                            initMainMenu()
                            currentScreenType = 100
                        }
                        else -> onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    fun play(resource: Int) {
        try {
            mp = MediaPlayer.create(this, resource)
            mp?.start()
        } catch (ex: Exception) {
        }
    }

    override fun onPause() {
        if (myGameView().showStarsThread != null)
            myGameView().showStarsThread?.interrupt()
        super.onPause()
    }

    override fun onStop() {
        wl?.release()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        myGameView().post { myGameView().startView() }
    }

    override fun onResume() {
        super.onResume()
        wl?.acquire(10*60*1000L /*10 minutes*/)
    }

    fun myGameView() = binding.gameView

    private fun loadBannerAd(adSize: BannerAdSize): BannerAdView {
        return binding.banner.apply {
            setAdSize(adSize)
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    if (isDestroyed) {
                        bannerAd?.destroy()
                        return
                    }
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    // Ad failed to load with AdRequestError.
                    // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                    Log.i("YandexAds", "loadBannerAd ${error.description}")
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    //addLevelError(10)
                }

                override fun onImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            loadAd(
                AdRequest.Builder("R-M-20111389-1").build()
            )
        }
    }

    private fun initiateAds() {
        binding.banner.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.banner.viewTreeObserver.removeOnGlobalLayoutListener(this);
                bannerAd = loadBannerAd(adSize)
            }
        })
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
            myGameView().addView(gameLayout)
            gameLayout?.StartView()
            val animation = AnimationUtils.loadAnimation(this, R.anim.show)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (prevGameLayout != null)
                        myGameView().removeView(prevGameLayout)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            gameLayout?.startAnimation(animation)
        }
    }

    fun returnToMenu(isStopSound: Boolean) {
        if (gameLayout != null) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.hide)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    myGameView().removeView(gameLayout)
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
        get() = myGameView().height.orZero()

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
