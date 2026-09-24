package sgolovanov.childrenpiramid

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.graphics.drawable.AnimationDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.edit
import java.io.IOException
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withRotation
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.core.view.isVisible

class GameView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var mp: MediaPlayer? = null
    var isVibrator: Boolean = true
    var isSound: Boolean = true
    var isStars: Boolean = true
        set(value) {
            field = value
            starsSettings?.setImageResource(R.drawable.animaciya_stars00)
            if (value) {
                starsSettings?.setBackgroundResource(R.drawable.animaciya_stars)
                (starsSettings?.background as? AnimationDrawable)?.apply {
                    start()
                }
            } else starsSettings?.setBackgroundColor(Color.TRANSPARENT)

        }
    var isAnimateBackground: Boolean = true
        set(value) {
            field = value
            if (value) {
                animateBackgroundSettings2?.setBackgroundResource(R.drawable.animaciya_babochka_1)
                (animateBackgroundSettings2?.background as? AnimationDrawable)?.apply {
                    start()
                }
            } else animateBackgroundSettings2?.setBackgroundColor(Color.TRANSPARENT)

        }
    var GameSettings: SharedPreferences? = null

    var listObjects: Array<MovingImageView?> = arrayOf() //new MovingImageView[maximum_objects];
    var listClearObjects: Array<ShadowImageView?> = arrayOf() //new ImageView[maximum_objects];
    var listAnimatedObjects: Array<AnimatedImageView> = arrayOf()
    var animationLayout: RelativeLayout? = null
    var levelsImages: Array<ImageView?> = arrayOfNulls(6)

    var objectLevelPosition: IntArray? = null
    var audio: AudioManager? = null
    var isLoadingLevels: Boolean = true

    internal inner class ShowStarsRunnable : Runnable {
        override fun run() {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    this@GameView.post {
                        val starSize = 45
                        val objectNum: Int = mRnd.nextInt(listObjects.size)
                        if (listObjects[objectNum] != null && listObjects[objectNum]?.isSetOnPosition != true) {
                            val starView = StarView(Owner)
                            val objectPoints = listObjects[objectNum]?.mClearObject?.visiblePoints.orZero()
                            val point = listObjects[objectNum]?.mClearObject?.getVisiblePoint(
                                    mRnd.nextInt(objectPoints)
                                )
                            val left =
                                listObjects[objectNum]?.mClearObject?.left.orZero() + point?.x.orZero() - starSize / 2 //mRnd.nextInt(Owner.getViewWidth()-starSize)+starSize/2;
                            val top =
                                listObjects[objectNum]?.mClearObject?.top.orZero() + point?.y.orZero() - starSize / 2 //mRnd.nextInt(Owner.getViewHeight()-starSize)+starSize/2;
                            setBounds(
                                starView, left.toFloat(),
                                top.toFloat(),
                                left + screenFactor * starSize,
                                top + screenFactor * starSize
                            )
                            this@GameView.addView(starView)
                            val anim = AlphaAnimation(0f, 1f)
                            anim.setDuration(500)
                            anim.setAnimationListener(object : AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {
                                }

                                override fun onAnimationEnd(animation: Animation?) {
                                    starView.clearAnimation()
                                    this@GameView.removeView(starView)
                                }

                                override fun onAnimationRepeat(animation: Animation?) {
                                }
                            })
                            //anim.setStartOffset(500);
                            anim.repeatMode = Animation.REVERSE
                            //anim.setRepeatCount(Animation.INFINITE);
                            starView.startAnimation(anim)
                        }
                    }
                    val sleeptime: Long = 50
                    Thread.sleep(sleeptime)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                } catch (e: Exception) {
                    Log.v("TEST", e.message!!)
                }
            }
        }
    }

    fun startView() {
        try {
            soundFromResId(R.raw.soobrazhalka)
            requestFocus()
            setFocusableInTouchMode(true)
            audio = Owner.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            vibrator = Owner.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
            GameSettings = Owner.getSharedPreferences(GameActivity.SHARED_PREFS_NAME, 0)
            initThread(GameSettings)
            isLoadingLevels = true
            isLoadingLevels = false


            if (currentScreenType == 200) makeFourLevelsIcons()
            initMainMenu()

            if (currentScreenType == 5 || currentScreenType == 6) {
                topPosition = 0
                //InitGameResults();
            }

            //setRunning(true);
            if (currentScreenType != 100 && currentScreenType != 102 && currentScreenType != 201) {
                initGameScreen()
            }
        } catch (ex: Exception) {
            val msg = ex.message
            Log.d("MY_TEST_LOG", ex.message!!)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            event.startTracking()
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            audio?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI
            )
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            audio?.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI
            )
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking && !event.isCanceled) {
            if (currentScreenType == 100) {
                exit()
            }
            if (currentScreenType == 1000) {
                removeView(Owner.gameLayout)
                currentScreenType = 100
            } else if (currentScreenType == 1 || currentScreenType == 200 || currentScreenType == 300) {
                smallClick()
                returnToMainMenu()
                currentScreenType = 100
            } else if (currentScreenType == 2)  // || CurrentScreenType==1)
            {
                smallClick()
                startSelectLevel(3)
                currentScreenType = 200
            } else if (currentScreenType == 102 || currentScreenType == 502 || currentScreenType == 3 || currentScreenType == 5 || currentScreenType == 6) {
                smallClick()
                initMainMenu()
                currentScreenType = 100
            }
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun isImageContains(image: ImageView?, x: Int, y: Int): Boolean {
        if (image != null && image.top <= y && image.bottom >= y && image.left <= x && image.right >= x) return true
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isLoadingLevels) return false
        if (event.action != MotionEvent.ACTION_DOWN) return false
        if (currentScreenType == 100) {
            if (isImageContains(startGameButton, event.x.toInt(), event.y.toInt())) {
                soundFromResId(R.raw.start_game)
                smallClick()
                selectLevelPart = 0
                startSelectLevel(1)
                currentScreenType = 200
            } else if (isImageContains(
                    exitGameButton,
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                exit()
            } else if (isImageContains(
                    settingsButton,
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                soundFromResId(R.raw.start_game)
                smallClick()
                startSelectLevel(2)
                currentScreenType = 300
            }
            return false
        }
        if (currentScreenType == 300) {
            val oldBackType = backgroundType
            val oldSymbolsType: Int = SymbolsType
            val oldVibrationType = vibrationType
            val isAnimateBackground1 = isAnimateBackground
            val isStars1 = isStars
            if (isImageContains(symbolsTypeSettings, event.x.toInt(), event.y.toInt())) {
                soundFromResId(R.raw.start_game)
                setSymbolsType(if (SymbolsType == 0) 1 else 0)
                smallClick()
            }
            if (isImageContains(vibrationSettings, event.x.toInt(), event.y.toInt())) {
                soundFromResId(R.raw.start_game)
                vibrationType = !vibrationType
                smallClick()
            }
            if (isImageContains(
                    animateBackgroundSettings,
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                soundFromResId(R.raw.start_game)
                isAnimateBackground = !isAnimateBackground
                smallClick()
            }
            if (isImageContains(starsSettings, event.x.toInt(), event.y.toInt())) {
                soundFromResId(R.raw.start_game)
                isStars = !isStars
                smallClick()
            }
            if (oldBackType != backgroundType) {
                //SetNextBackgroundTime();
                GameSettings?.edit {
                    putInt(Owner.getString(R.string.background_key), backgroundType)
                }
            }
            if (oldSymbolsType != SymbolsType) {
                GameSettings?.edit {
                    putInt(Owner.getString(R.string.bukvy_key), SymbolsType)
                }
            }
            if (oldVibrationType != vibrationType) {
                GameSettings?.edit {
                    putBoolean(Owner.getString(R.string.vibration_key), vibrationType)
                }
            }
            if (isStars1 != isStars) {
                GameSettings?.edit {
                    putBoolean(Owner.getString(R.string.stars_key), isStars)
                }
            }
            if (isAnimateBackground1 != isAnimateBackground) {
                GameSettings?.edit {
                    putBoolean(
                        Owner.getString(R.string.animate_background_key),
                        isAnimateBackground
                    )
                }
            }
            return false
        } else if (currentScreenType == 2) {
            if (isImageContains(gameButtonMoon, event.run { x.toInt() }, event.y.toInt())) {
                smallClick()
                prevLevel()
                return true
            } else if (isImageContains(
                    gameButtonFlower,
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                smallClick()
                startLevel()
                return true
            } else if (isImageContains(
                    gameButtonSunshine,
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                smallClick()
                nextLevel()
                return true
            }
        } else if (currentScreenType == 200) {
            if (levelsImages!![4] != null && isImageContains(
                    levelsImages!![4],
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                //soundFromObb("start_game");
                if (selectLevelPart > 0) {
                    selectLevelPart--
                    makeFourLevelsIcons()
                }
            } else if (levelsImages!![5] != null && isImageContains(
                    levelsImages!![5],
                    event.x.toInt(),
                    event.y.toInt()
                )
            ) {
                //soundFromObb("start_game");
                if ((selectLevelPart + 1) * 4 < levels.size) {
                    selectLevelPart++
                    makeFourLevelsIcons()
                }
            }
            for (i in 0..3) {
                if (levelsImages!![i] != null && isImageContains(
                        levelsImages!![i],
                        event.x.toInt(),
                        event.y.toInt()
                    ) && selectLevelPart * 4 + i < levels.size
                ) {
                    currentLevelNum = selectLevelPart * 4 + i
                    setLevelBackground()
                    clearSelectLevels()
                    startLevel()
                }
            }
            return false
        } else if (currentScreenType == 1) {
            return true
        }

        return false //super.onTouchEvent(event);
    }

    var mRun: Boolean = false
    var vibrator: Vibrator? = null
    var backgroundType: Int = 3
    var vibrationType: Boolean = true
        set(value) {
            field = value
            vibrationSettings?.setImageResource(R.drawable.no_vibration)
            if (value) {
                vibrationSettings?.setBackgroundResource(R.drawable.animaciya_vibraciya)
                (vibrationSettings?.background as? AnimationDrawable)?.apply {
                    start()
                }
            } else vibrationSettings?.setBackgroundColor(Color.TRANSPARENT)

        }
    var startGameButton: ImageView? = null
    var exitGameButton: ImageView? = null
    var levelGameButton: ImageView? = null
    var startCvetyImage: ImageView? = null
    var startListiyaImage: ImageView? = null
    var startListikImage: ImageView? = null
    var startStar1Image: ImageView? = null
    var startStar2Image: ImageView? = null
    var startStar3Image: ImageView? = null
    var startStar4Image: ImageView? = null
    var startStar5Image: ImageView? = null

    var symbolsTypeSettings: ImageView? = null
    var vibrationSettings: ImageView? = null
    var animateBackgroundSettings: ImageView? = null
    var animateBackgroundSettings2: ImageView? = null
    var starsSettings: ImageView? = null

    var glassView: ImageView? = null

    var listBackground: ImageView? = null


    var centerX: Float = 0f
    var topPos: Float = 0f
    var leftPos: Float = 0f
    var screenFactor: Float = 1f
    var bmpFactor: Float = 1f

    var goodBoy = arrayOf(
        R.raw.molodec,
        R.raw.otlichno,
        R.raw.velikolepno,
        R.raw.ya_toboj_gorzhus,
        R.raw.prevoshodno,
        R.raw.blestjasche,
        R.raw.chudesno,
    )

    var goodStep = arrayOf(
        R.raw.da,
        R.raw.pravilno,
        R.raw.umnica,
        R.raw.horosho,
        R.raw.prekrasno,
    )

    var gameButtonMoon: ImageView? = null
    var gameButtonFlower: ImageView? = null
    var gameButtonSunshine: ImageView? = null
    var gameButtonMoonWhite: ImageView? = null
    var gameButtonSunshineWhite: ImageView? = null

    var settingsButton: ImageView? = null

    var isStartingLevel: Boolean = true

    private fun initGameScreen() {
        startLevel()
    }

    fun initThread(settings: SharedPreferences?) {
        backgroundType = GameSettings!!.getInt(Owner.getString(R.string.background_key), 3)
        SymbolsType = GameSettings!!.getInt(Owner.getString(R.string.bukvy_key), 0)
        vibrationType = GameSettings!!.getBoolean(Owner.getString(R.string.vibration_key), true)
        isStars = GameSettings!!.getBoolean(
            Owner.getString(R.string.stars_key),
            Owner.viewHeight > 600
        )
        isAnimateBackground = GameSettings!!.getBoolean(
            Owner.getString(R.string.animate_background_key),
            Owner.viewHeight > 600
        )
    }

    fun onDestroy() {
        mRun = false
        recycleBitmaps()
    }

    private fun recycleBitmap(image: View?) {
        if (image != null) {
            image.clearAnimation()
            removeView(image)
        }
    }

    private fun clearSettings() {
        recycleBitmap(symbolsTypeSettings)
        recycleBitmap(vibrationSettings)
        recycleBitmap(shtoraImageView)
        recycleBitmap(animateBackgroundSettings)
        recycleBitmap(animateBackgroundSettings2)
        recycleBitmap(starsSettings)
    }

    private fun recycleBitmaps() {
        this.post {
            recycleBitmap(exitGameButton)
            recycleBitmap(startGameButton)
            recycleBitmap(levelGameButton)

            recycleBitmap(gameButtonMoon)
            recycleBitmap(gameButtonFlower)
            recycleBitmap(gameButtonSunshine)
            recycleBitmap(gameButtonMoonWhite)
            recycleBitmap(gameButtonSunshineWhite)
            recycleBitmap(settingsButton)

            clearSettings()

            recycleBitmap(listBackground)

            levelsImages.forEach { recycleBitmap(it) }
            listObjects.forEach { recycleBitmap(it) }
            listClearObjects.forEach { recycleBitmap(it) }
        }
    }

    private fun nextLevel() {
        currentLevelNum++
        if (currentLevelNum >= levels.size) currentLevelNum = 0
        startLevel()
    }

    private fun prevLevel() {
        currentLevelNum--
        if (currentLevelNum < 0) currentLevelNum = levels.size - 1
        startLevel()
    }

    private fun newDecodeResourceWithSize(bmpName: String?, factor: Float): MovingImageView {
        val image = MovingImageView(Owner, this)
        this.addView(image)
        val tmp = getImageFromObb(bmpName)
        tmp?.let { tmp ->
            if (factor >= 1f) {
                image.setImageBitmap(tmp, factor)
                return image
            }
            val res = tmp.scale(
                (tmp.getWidth() * (if (isScaledBitmaps) 2 else 1) * factor).toInt(),
                (tmp.getHeight() * (if (isScaledBitmaps) 2 else 1) * factor).toInt(),
                false
            )
            tmp.recycle()
            image.setImageBitmap(res, 1f)
        }
        return image
    }

    private fun newDecodeResourceWithSize2(bmpName: String?, factor: Float): RotateImageView {
        val image = RotateImageView(Owner, this)
        this.addView(image)
        getImageFromObb(bmpName)?.let { tmp ->
            if (factor >= 1f) {
                image.setImageBitmap(tmp, factor)
                return image
            }
            val res =
                tmp.scale(
                    (tmp.width * factor).toInt(),
                    (tmp.height * factor).toInt(),
                    false
                )
            tmp.recycle()
            image.setImageBitmap(res, 1f)
        }
        return image
    }

    private fun newDecodeResourceWithSize3(bmpName: String?, factor: Float): DrawImageView {
        val image = DrawImageView(Owner, this)
        addView(image)
        getImageFromObb(bmpName)?.let { tmp ->
            if (factor == 1f) {
                image.setImageBitmap(tmp)
                return image
            }
            val res =
                tmp.scale(
                    width = (tmp.width * factor).toInt(),
                    height = (tmp.height * factor).toInt(),
                    filter = false
                )
            tmp.recycle()
            image.setImageBitmap(res, Owner.viewHeight / 10f)
        }
        return image
    }

    private fun clearLevel() {
        showStarsThread?.interrupt()
        showStarsThread = null
        ClearMainMenu()
        recycleBitmap(glassView)
        recycleBitmap(gameButtonMoon)
        recycleBitmap(gameButtonFlower)
        recycleBitmap(gameButtonSunshine)
            for (i in listObjects.indices) {
                recycleBitmap(listObjects[i])
                listObjects[i] = null
            }
        listClearObjects.let { listClearObjects ->
            for (i in listClearObjects.indices) {
                recycleBitmap(listClearObjects[i])
            }
        }
        listClearObjects = arrayOf()
        for (i in listAnimatedObjects.indices) {
            listAnimatedObjects[i].clearAnimation()
            recycleBitmap(listAnimatedObjects[i])
            animationLayout!!.removeView(listAnimatedObjects[i])
        }
        listAnimatedObjects = arrayOf()
        recycleBitmap(listBackground)
        if (animationLayout != null) removeView(animationLayout)
        animationLayout = null
    }

    var flakes: IntArray? = null
    var flakes2: IntArray? = null
    var maxSize: Point? = null
    var sleepTimes = 1000
    
    fun setSnowflake(imageView: AnimatedImageView, startOffset: Int) {
        val iMul: Float = mRnd.nextFloat() / 2f + 0.5f
        val objectNum: Int = mRnd.nextInt(flakes?.size.orZero())
        val posX: Int = mRnd.nextInt(Owner.viewWidth)
        //final ImageView imageView=new ImageView(Owner);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setBounds(
            imageView,
            posX - maxSize?.x.orZero() * iMul / 2,
            -maxSize?.y.orZero() * iMul,
            posX + maxSize?.x.orZero() * iMul / 2,
            0f
        )
        imageView.setImageResource(flakes!![objectNum])
        val animationRotate: Animation =
            RotateAnimation(0f, 360f, maxSize?.x.orZero() * iMul / 2, maxSize?.y.orZero() * iMul / 2)
        val animationTranslate: Animation =
            TranslateAnimation(0f, 0f, 0f, Owner.viewHeight + maxSize?.y.orZero() * iMul * 2)
        animationTranslate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //AnimationLayout.removeView(imageView);
                if (imageView.getAnimation() != null) setSnowflake(imageView, mRnd.nextInt(2000))
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationRotate)
        animationSet.addAnimation(animationTranslate)
        animationSet.setDuration((mRnd.nextInt(6000) + 2000).toLong())
        animationSet.setStartOffset(startOffset.toLong())
        imageView.startAnimation(animationSet)
    }

    fun setSnowflake3D(imageView: AnimatedImageView, startOffset: Int) {
        val iMul = mRnd.nextFloat() / 3f + 0.67f
        val objectNum = mRnd.nextInt(flakes!!.size)
        val posX = mRnd.nextInt(Owner.viewWidth)
        //final ImageView imageView=new ImageView(Owner);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setBounds(
            imageView,
            posX - maxSize?.x.orZero() * iMul / 2,
            -maxSize?.y.orZero() * iMul,
            posX + maxSize?.x.orZero() * iMul / 2,
            0f
        )
        imageView.setImageResource(flakes!![objectNum])
        val outValue = TypedValue()

        /*Resources.Theme currentTheme = Owner.getTheme();
		currentTheme.resolveAttribute(R.attr.actionBarWidgetTheme,
				outValue, true);
		final int targetThemeRes = outValue.resourceId;*/
        val animation3DRotate: Animation = Rotate3DAnimation(
            ContextThemeWrapper(Owner, R.style.FullscreenTheme),
            0f,
            360f,
            maxSize?.x.orZero() * iMul / 2,
            maxSize?.y.orZero() * iMul / 2,
            mRnd.nextInt(360).toFloat(),
            false
        )
        val animationRotate =
            RotateAnimation(0f, 360f, maxSize?.x.orZero() * iMul / 2, maxSize?.y.orZero() * iMul / 2)
        val animationTranslate =
            TranslateAnimation(0f, 0f, 0f, Owner.viewHeight + maxSize?.y.orZero() * iMul * 2)
        animationTranslate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //AnimationLayout.removeView(imageView);
                if (imageView.getAnimation() != null) setSnowflake3D(imageView, mRnd.nextInt(2000))
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animation3DRotate)
        animationSet.addAnimation(animationRotate)
        animationSet.addAnimation(animationTranslate)
        animationSet.setDuration((mRnd.nextInt(2000) + 6000).toLong())
        animationSet.setStartOffset(startOffset.toLong())
        imageView.startAnimation(animationSet)
    }

    fun setPyatno(imageView: AnimatedImageView) {
        val layoutParams = imageView.getLayoutParams() as LayoutParams
        val imageLeft = layoutParams.leftMargin //imageView.getLeft();
        val imageTop = layoutParams.topMargin //imageView.getTop();
        val posX = mRnd.nextInt((Owner.viewWidth - layoutParams.width))
        val posY = mRnd.nextInt((Owner.viewHeight - layoutParams.height))
        //final ImageView imageView=new ImageView(Owner);
        val animationTranslate: Animation =
            TranslateAnimation(0f, (posX - imageLeft).toFloat(), 0f, (posY - imageTop).toFloat())
        animationTranslate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //AnimationLayout.removeView(imageView);
                if (imageView.getAnimation() != null) {
                    setBounds(
                        imageView,
                        posX.toFloat(),
                        posY.toFloat(),
                        (posX + layoutParams.width).toFloat(),
                        (posY + layoutParams.height).toFloat()
                    )
                    setPyatno(imageView)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val size =
            sqrt(((imageLeft - posX) * (imageLeft - posX) + (imageTop - posY) * (imageTop - posY)).toDouble()).toFloat()
        animationTranslate.setDuration((1200 * (size / 620)).toLong())
        imageView.startAnimation(animationTranslate)
    }

    fun setOblako(imageView: AnimatedImageView) {
        val left = imageView.centralPoint!!.x
        val top = imageView.centralPoint!!.y
        val posX = (left + (mRnd.nextInt(80) - 40) * bmpFactor).toInt()
        val posY = (top + (mRnd.nextInt(20) - 10) * bmpFactor).toInt()
        val layoutParams = imageView.getLayoutParams() as LayoutParams
        val imageLeft = layoutParams.leftMargin //imageView.getLeft();
        val imageTop = layoutParams.topMargin //imageView.getTop();
        //final ImageView imageView=new ImageView(Owner);
        val animationTranslate: Animation =
            TranslateAnimation(0f, (posX - imageLeft).toFloat(), 0f, (posY - imageTop).toFloat())
        animationTranslate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //AnimationLayout.removeView(imageView);
                if (imageView.getAnimation() != null) {
                    setBounds(
                        imageView,
                        posX.toFloat(),
                        posY.toFloat(),
                        (posX + imageView.getWidth()).toFloat(),
                        (posY + imageView.getHeight()).toFloat()
                    )
                    setOblako(imageView)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val size =
            sqrt(((imageLeft - posX) * (imageLeft - posX) + (imageTop - posY) * (imageTop - posY)).toDouble()).toFloat()
        animationTranslate.setDuration((mRnd.nextInt(2000) + 2000).toLong())
        imageView.startAnimation(animationTranslate)
    }

    fun setPerekatiPole(imageView: AnimatedImageView, startOffset: Int, top: Int, bottom: Int) {
        val iMul = mRnd.nextFloat() / 2f + 0.5f
        val objectNum = mRnd.nextInt(flakes!!.size)
        val posY = top + mRnd.nextInt(bottom - top)
        //final ImageView imageView=new ImageView(Owner);
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setBounds(
            imageView,
            -maxSize?.x.orZero() * iMul,
            posY.toFloat(),
            0f,
            posY + maxSize?.y.orZero() * iMul
        )
        imageView.setImageResource(flakes!![objectNum])
        val animationRotate =
            RotateAnimation(0f, 360f, maxSize?.x.orZero() * iMul / 2, maxSize?.y.orZero() * iMul / 2)
        val animationTranslate =
            TranslateAnimation(0f, Owner.viewWidth + maxSize?.x.orZero() * iMul * 2, 0f, 0f)
        animationTranslate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                //AnimationLayout.removeView(imageView);
                if (imageView.animation != null) setPerekatiPole(
                    imageView,
                    mRnd.nextInt(2000),
                    top,
                    bottom
                )
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationRotate)
        animationSet.addAnimation(animationTranslate)
        animationSet.setDuration((mRnd.nextInt(6000) + 2000).toLong())
        animationSet.setStartOffset(startOffset.toLong())
        imageView.startAnimation(animationSet)
    }

    fun getPointByAroundLength(startPoint: Int): PointF {
        if (startPoint < Owner.viewWidth) {
            return PointF(startPoint.toFloat(), -maxSize?.y.orZero() / 2f)
        } else if (startPoint < Owner.viewWidth + Owner.viewHeight) {
            return PointF(
                Owner.viewWidth + maxSize?.x.orZero() / 2f,
                (startPoint - Owner.viewWidth).toFloat()
            )
        } else if (startPoint < Owner.viewWidth + Owner.viewHeight + Owner.viewWidth) {
            return PointF(
                (startPoint - Owner.viewWidth - Owner.viewHeight).toFloat(),
                Owner.viewHeight + maxSize?.y.orZero() / 2f
            )
        }
        return PointF(
            -maxSize?.x.orZero() / 2f,
            (startPoint - Owner.viewWidth - Owner.viewHeight - Owner.viewWidth).toFloat()
        )
    }

    fun setBabochkaPath(imageView: AnimatedImageView) {
        imageView.setBackgroundResource(flakes!![mRnd.nextInt(flakes!!.size)])
        val babochkaAnimation = imageView.background as AnimationDrawable
        babochkaAnimation.start()
        val summaryAroundLength = Owner.viewHeight * 2 + Owner.viewWidth * 2
        val startPoint = getPointByAroundLength(mRnd.nextInt(summaryAroundLength))
        var endPoint: PointF
        do {
            endPoint = getPointByAroundLength(mRnd.nextInt(summaryAroundLength))
        } while (abs(endPoint.x - startPoint.x) < maxSize?.x.orZero() * 2 || abs(endPoint.y - startPoint.y) < maxSize?.y.orZero() * 2)
        setBounds(
            imageView,
            startPoint.x - maxSize?.x.orZero() / 2f,
            startPoint.y - maxSize?.y.orZero() / 2f,
            startPoint.x + maxSize?.x.orZero() / 2f,
            startPoint.y + maxSize?.y.orZero() / 2f
        )
        val angle = (atan2(
            (endPoint.y - startPoint.y).toDouble(),
            (endPoint.x - startPoint.x).toDouble()
        ) / Math.PI * 180).toFloat() + 90
        imageView.setRotation(angle)
        val animation: Animation =
            TranslateAnimation(0f, endPoint.x - startPoint.x, 0f, endPoint.y - startPoint.y)
        animation.setDuration((5000f / Owner.viewWidth * sqrt(((endPoint.x - startPoint.x) * (endPoint.x - startPoint.x) + (endPoint.y - startPoint.y) * (endPoint.y - startPoint.y)).toDouble())).toLong())
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setBabochkaPath(imageView)
            }
        })
        imageView.startAnimation(animation)
    }

    fun setRomashkaPath(imageView: AnimatedImageView) {
        imageView.setImageResource(flakes!![mRnd.nextInt(flakes!!.size)])
        val summaryAroundLength = Owner.viewHeight * 2 + Owner.viewWidth * 2
        val startPoint = getPointByAroundLength(mRnd.nextInt(summaryAroundLength))
        var endPoint: PointF
        do {
            endPoint = getPointByAroundLength(mRnd.nextInt(summaryAroundLength))
        } while (abs(endPoint.x - startPoint.x) < maxSize?.x.orZero() * 2 || abs(endPoint.y - startPoint.y) < maxSize?.y.orZero() * 2)
        setBounds(
            imageView,
            startPoint.x - maxSize?.x.orZero() / 2f,
            startPoint.y - maxSize?.y.orZero() / 2f,
            startPoint.x + maxSize?.x.orZero() / 2f,
            startPoint.y + maxSize?.y.orZero() / 2f
        )
        val animation =
            TranslateAnimation(0f, endPoint.x - startPoint.x, 0f, endPoint.y - startPoint.y)
        val animationRotate =
            RotateAnimation(0f, 360f, maxSize?.x.orZero() / 2f, maxSize?.y.orZero() / 2f)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setRomashkaPath(imageView)
            }
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationRotate)
        animationSet.addAnimation(animation)
        animationSet.setDuration((5000f / Owner.viewWidth * sqrt(((endPoint.x - startPoint.x) * (endPoint.x - startPoint.x) + (endPoint.y - startPoint.y) * (endPoint.y - startPoint.y)).toDouble())).toLong())
        imageView.startAnimation(animationSet)
    }

    fun setRomashkaCurl(imageView: AnimatedImageView) {
        imageView.setImageResource(flakes!![mRnd.nextInt(flakes!!.size)])
        val posX = mRnd.nextInt(Owner.viewWidth)
        val posY = mRnd.nextInt(Owner.viewHeight)
        setBounds(
            imageView,
            posX - maxSize?.x.orZero() / 2f,
            posY - maxSize?.y.orZero() / 2f,
            posX + maxSize?.x.orZero() / 2f,
            posY + maxSize?.y.orZero() / 2f
        )
        val animationScale: Animation =
            ScaleAnimation(0f, 1.2f, 0f, 1.2f, maxSize?.x.orZero() / 2f, maxSize?.y.orZero() / 2f)
        val animationRotate: Animation =
            RotateAnimation(0f, 359f, maxSize?.x.orZero() / 2f, maxSize?.y.orZero() / 2f)
        val animationAlpha: Animation = AlphaAnimation(1f, 0f)
        animationRotate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.animation != null) setRomashkaCurl(imageView)
            }
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationRotate)
        animationSet.addAnimation(animationScale)
        animationSet.addAnimation(animationAlpha)
        animationSet.setDuration((1500 + mRnd.nextInt(1000)).toLong())
        imageView.startAnimation(animationSet)
    }

    fun setRybkaPath(imageView: AnimatedImageView) {
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setBackgroundResource(flakes2!![resNum])
        imageView.setImageResource(flakes!![resNum])
        val babochkaAnimation = imageView.getBackground() as AnimationDrawable
        babochkaAnimation.start()
        val posY = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero())
        val direction = mRnd.nextInt(2)
        val startX = if (direction == 1) -maxSize?.x.orZero() else Owner.viewWidth + maxSize?.x.orZero()
        val endX = if (direction == 1) Owner.viewWidth + maxSize?.x.orZero() else -maxSize?.x.orZero()
        imageView.isHorizontalReverse = direction != 1
        setBounds(
            imageView,
            startX - maxSize?.x.orZero() / 2f,
            posY - maxSize?.y.orZero() / 2f,
            startX + maxSize?.x.orZero() / 2f,
            posY + maxSize?.y.orZero() / 2f
        )
        val animation: Animation = TranslateAnimation(0f, (endX - startX).toFloat(), 0f, 0f)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.animation != null) setRybkaPath(imageView)
            }
        })
        animation.setDuration((10000 + mRnd.nextInt(6000)).toLong())
        imageView.startAnimation(animation)
    }

    fun setSalut() {
        val objectNum = mRnd.nextInt(flakes!!.size)
        val posX = mRnd.nextInt(Owner.viewWidth)
        val posY = mRnd.nextInt(Owner.viewHeight)
        val points = arrayOf<PointF>(
            PointF(67f, 66.5f),
            PointF(149.5f, 32.5f),
            PointF(233f, 66.5f),
            PointF(267.5f, 149.5f),
            PointF(233f, 233.5f),
            PointF(149.5f, 267.5f),
            PointF(67f, 233.5f),
            PointF(32.5f, 149.5f),
        )

        val imageView = listAnimatedObjects[0]
        for (i in listAnimatedObjects.indices) {
            //ListAnimatedObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
            setBounds(
                listAnimatedObjects[i],
                (posX - maxSize?.x.orZero() / 2).toFloat(),
                (posY - maxSize?.y.orZero() / 2).toFloat(),
                (posX + maxSize?.x.orZero() / 2).toFloat(),
                (posY + maxSize?.y.orZero() / 2).toFloat()
            )
            listAnimatedObjects[i].setImageResource(flakes!![objectNum])
            val animationTranslate: Animation = TranslateAnimation(
                0f,
                (points[i].x * 4 / 3 - 200) * bmpFactor,
                0f,
                (points[i].y * 4 / 3 - 200) * bmpFactor
            )
            if (i == listAnimatedObjects.size - 1) {
                val finalI = i
                animationTranslate.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        //AnimationLayout.removeView(imageView);
                        if (imageView.animation != null) setSalut()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
            }
            animationTranslate.setDuration(2000)
            //animationTranslate.setStartOffset(startOffset);
            listAnimatedObjects[i].startAnimation(animationTranslate)
        }
    }

    fun setSharikUp(imageView: AnimatedImageView, startOffset: Int) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setImageResource(flakes!![resNum])
        val posX1 = mRnd.nextInt(Owner.viewWidth)
        val posX2 = posX1 + mRnd.nextInt(Owner.viewWidth / 5) - Owner.viewWidth / 10
        setBounds(
            imageView,
            posX1 - maxSize?.x.orZero() / 2f,
            Owner.viewHeight.toFloat(),
            posX1 + maxSize?.x.orZero() / 2f,
            (Owner.viewHeight + maxSize?.y.orZero()).toFloat()
        )
        val animation: Animation = TranslateAnimation(
            0f,
            (posX2 - posX1).toFloat(),
            0f,
            (-Owner.viewHeight - maxSize?.y.orZero() * 2).toFloat()
        )
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.animation != null) setSharikUp(imageView, 0)
            }
        })
        animation.setDuration((3000 + mRnd.nextInt(3000)).toLong())
        animation.startOffset = startOffset.toLong()
        imageView.startAnimation(animation)
    }

    fun setVozdushnyjSharUp(imageView: AnimatedImageView, startOffset: Int) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setImageResource(flakes!![resNum])
        val posX1 = mRnd.nextInt(Owner.viewWidth)
        val posX2 = posX1 + mRnd.nextInt(Owner.viewWidth / 3) - Owner.viewWidth / 6
        setBounds(
            imageView,
            posX1 - maxSize?.x.orZero() / 2f,
            Owner.viewHeight.toFloat(),
            posX1 + maxSize?.x.orZero() / 2f,
            (Owner.viewHeight + maxSize?.y.orZero()).toFloat()
        )
        val animation: Animation = TranslateAnimation(
            0f,
            (posX2 - posX1).toFloat(),
            0f,
            (-Owner.viewHeight - maxSize?.y.orZero() * 2).toFloat()
        )
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setVozdushnyjSharUp(imageView, 0)
            }
        })
        animation.setDuration((6000 + mRnd.nextInt(3000)).toLong())
        animation.setStartOffset(startOffset.toLong())
        imageView.startAnimation(animation)
    }

    fun setVozdushnyjShar2Up(imageView: AnimatedImageView, startOffset: Int) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setBackgroundResource(flakes!![resNum])
        val babochkaAnimation = imageView.getBackground() as AnimationDrawable
        babochkaAnimation.start()
        val posX1 = mRnd.nextInt(Owner.viewWidth)
        val posX2 = posX1 + mRnd.nextInt(Owner.viewWidth / 3) - Owner.viewWidth / 6
        setBounds(
            imageView,
            posX1 - maxSize?.x.orZero() / 2f,
            Owner.viewHeight.toFloat(),
            posX1 + maxSize?.x.orZero() / 2f,
            (Owner.viewHeight + maxSize?.y.orZero()).toFloat()
        )
        val animation = TranslateAnimation(
            0f,
            (posX2 - posX1).toFloat(),
            0f,
            (-Owner.viewHeight - maxSize?.y.orZero() * 2).toFloat()
        )
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.animation != null) setVozdushnyjShar2Up(imageView, 0)
            }
        })
        animation.setDuration((6000 + mRnd.nextInt(3000)).toLong())
        animation.startOffset = startOffset.toLong()
        imageView.startAnimation(animation)
    }

    fun setMeduzaPath(imageView: AnimatedImageView) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setImageResource(flakes!![resNum])
        imageView.setBackgroundResource(flakes2!![resNum])
        val babochkaAnimation = imageView.getBackground() as AnimationDrawable
        babochkaAnimation.start()
        val posX1 = mRnd.nextInt(Owner.viewWidth)
        val posX2 = posX1 + mRnd.nextInt(Owner.viewWidth / 3) - Owner.viewWidth / 6
        val direction = mRnd.nextInt(2)
        imageView.isHorizontalReverse = direction != 1
        setBounds(
            imageView,
            posX1 - maxSize?.x.orZero() / 2f,
            Owner.viewHeight.toFloat(),
            posX1 + maxSize?.x.orZero() / 2f,
            (Owner.viewHeight + maxSize?.y.orZero()).toFloat()
        )
        val animation: Animation = TranslateAnimation(
            0f,
            (posX2 - posX1).toFloat(),
            0f,
            (-Owner.viewHeight - maxSize?.y.orZero() * 2).toFloat()
        )
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setMeduzaPath(imageView)
            }
        })
        animation.setDuration((10000 + mRnd.nextInt(3000)).toLong())
        imageView.startAnimation(animation)
    }

    fun setRaketaUp(imageView: AnimatedImageView, startOffset: Int) {
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        val resNum = mRnd.nextInt(flakes!!.size)
        imageView.setImageResource(flakes!![resNum])
        val posX1= mRnd.nextInt(Owner.viewWidth)
        val posX2 = posX1 + mRnd.nextInt(Owner.viewWidth / 5) - Owner.viewWidth / 10
        setBounds(
            imageView,
            posX1 - maxSize?.x.orZero() / 2f,
            Owner.viewHeight.toFloat(),
            posX1 + maxSize?.x.orZero() / 2f,
            (Owner.viewHeight + maxSize?.y.orZero()).toFloat()
        )
        val animation: Animation = TranslateAnimation(
            0f,
            (posX2 - posX1).toFloat(),
            0f,
            (-Owner.viewHeight - maxSize?.y.orZero() * 2).toFloat()
        )
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.animation != null) setRaketaUp(imageView, 0)
            }
        })
        animation.setDuration((3000 + mRnd.nextInt(3000)).toLong())
        animation.startOffset = startOffset.toLong()
        imageView.startAnimation(animation)
    }

    fun setParovozikPath(imageView1: AnimatedImageView, imageView2: AnimatedImageView) {
        val resNum: Int = mRnd.nextInt(flakes!!.size)
        imageView1.setImageResource(flakes!![resNum])
        //imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setBackgroundResource(flakes2!![resNum])
        val babochkaAnimation = imageView2.getBackground() as AnimationDrawable
        babochkaAnimation.start()
        val posY: Int = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero()) + maxSize?.y.orZero() / 2
        val direction: Int = mRnd.nextInt(2)
        val startX = if (direction == 1) -maxSize?.x.orZero() else Owner.viewWidth + maxSize?.x.orZero()
        val endX = if (direction == 1) Owner.viewWidth + maxSize?.x.orZero() else -maxSize?.x.orZero()
        imageView1.isHorizontalReverse = direction != 1
        imageView2.isHorizontalReverse = direction != 1
        setBounds(
            imageView1,
            startX - maxSize?.x.orZero() / 2f,
            posY - maxSize?.y.orZero() / 2f,
            startX + maxSize?.x.orZero() / 2f,
            posY + maxSize?.y.orZero() / 2f
        )
        setBounds(
            imageView2,
            startX - maxSize?.x.orZero() / 2f,
            posY - maxSize?.y.orZero() / 2f,
            startX + maxSize?.x.orZero() / 2f,
            posY + maxSize?.y.orZero() / 2f
        )
        val animation: Animation = TranslateAnimation(0f, (endX - startX).toFloat(), 0f, 0f)
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView1.getAnimation() != null) setParovozikPath(imageView1, imageView2)
            }
        })
        animation.setDuration((6000 + mRnd.nextInt(2000)).toLong())
        imageView1.startAnimation(animation)
        imageView2.startAnimation(animation)
    }

    fun setKometaPath(imageView: AnimatedImageView) {
        val resNum: Int = mRnd.nextInt(flakes!!.size)
        imageView.setBackgroundResource(flakes!![resNum])
        val babochkaAnimation = imageView.background as AnimationDrawable
        babochkaAnimation.start()
        val posY1: Int = mRnd.nextInt(Owner.viewHeight)
        val posY2: Int = mRnd.nextInt(Owner.viewHeight)
        val direction: Int = mRnd.nextInt(2)
        val startX = if (direction == 1) -maxSize?.x.orZero() else Owner.viewWidth + maxSize?.x.orZero()
        val endX = if (direction == 1) Owner.viewWidth + maxSize?.x.orZero() else -maxSize?.x.orZero()
        setBounds(
            imageView,
            (startX - maxSize?.x.orZero() / 2).toFloat(),
            posY1 - maxSize?.y.orZero() / 2f,
            (startX + maxSize?.x.orZero() / 2).toFloat(),
            posY1 + maxSize?.y.orZero() / 2f
        )
        val angle = (atan2(
            (posY2 - posY1).toDouble(),
            (endX - startX).toDouble()
        ) / Math.PI * 180).toFloat() + 180
        imageView.rotation = angle
        val animation: Animation =
            TranslateAnimation(0f, (endX - startX).toFloat(), 0f, (posY2 - posY1).toFloat())
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setKometaPath(imageView)
            }
        })
        animation.setDuration((8000 + mRnd.nextInt(4000)).toLong())
        imageView.startAnimation(animation)
    }

    fun setSamoletikPath(imageView: AnimatedImageView) {
        val resNum: Int = mRnd.nextInt(flakes!!.size)
        imageView.setBackgroundResource(flakes!![resNum])
        val babochkaAnimation = imageView.background as AnimationDrawable
        babochkaAnimation.start()
        val posY1: Int = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero())
        val posY2: Int = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero())
        val direction: Int = mRnd.nextInt(2)
        val startX = if (direction == 1) -maxSize?.x.orZero() else Owner.viewWidth + maxSize?.x.orZero()
        val endX = if (direction == 1) Owner.viewWidth + maxSize?.x.orZero() else -maxSize?.x.orZero()
        setBounds(
            imageView,
            (startX - maxSize?.x.orZero() / 2).toFloat(),
            posY1 - maxSize?.y.orZero() / 2f,
            (startX + maxSize?.x.orZero() / 2).toFloat(),
            posY1 + maxSize?.y.orZero() / 2f
        )
        imageView.isHorizontalReverse = direction != 1
        val angle = (atan2(
            (posY2 - posY1).toDouble(),
            abs(endX - startX).toDouble()
        ) / Math.PI * 180).toFloat()
        imageView.rotation = angle
        val animation: Animation =
            TranslateAnimation(0f, (endX - startX).toFloat(), 0f, (posY2 - posY1).toFloat())
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setSamoletikPath(imageView)
            }
        })
        animation.setDuration((8000 + mRnd.nextInt(4000)).toLong())
        imageView.startAnimation(animation)
    }

    fun setSamoletPath(imageView: AnimatedImageView) {
        val resNum: Int = mRnd.nextInt(flakes!!.size)
        imageView.setImageResource(flakes!![resNum])
        val posY1: Int = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero())
        val posY2: Int = mRnd.nextInt(Owner.viewHeight - maxSize?.y.orZero())
        val direction: Int = mRnd.nextInt(2)
        val startX = if (direction == 1) -maxSize?.x.orZero() else Owner.viewWidth + maxSize?.x.orZero()
        val endX = if (direction == 1) Owner.viewWidth + maxSize?.x.orZero() else -maxSize?.x.orZero()
        setBounds(
            imageView,
            (startX - maxSize?.x.orZero() / 2).toFloat(),
            posY1 - maxSize?.y.orZero() / 2f,
            (startX + maxSize?.x.orZero() / 2).toFloat(),
            posY1 + maxSize?.y.orZero() / 2f
        )
        imageView.isHorizontalReverse = direction != 1
        val angle = (atan2(
            (posY2 - posY1).toDouble(),
            abs(endX - startX).toDouble()
        ) / Math.PI * 180).toFloat()
        imageView.setRotation(angle)
        val animation: Animation =
            TranslateAnimation(0f, (endX - startX).toFloat(), 0f, (posY2 - posY1).toFloat())
        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (imageView.getAnimation() != null) setSamoletPath(imageView)
            }
        })
        animation.setDuration((8000 + mRnd.nextInt(4000)).toLong())
        imageView.startAnimation(animation)
    }

    fun makeAnimatedBackground(backgroundType: Int) {
        animationLayout = RelativeLayout(Owner)
        animationLayout!!.setLayoutParams(
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
        this.addView(animationLayout)
        when (backgroundType) {
            2 -> {
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animaciya_solnyshko)
                        val rectF = RectF(
                            centerX + (-150 - 600) * bmpFactor,
                            topPos + (-150) * bmpFactor,
                            centerX + (150 - 600) * bmpFactor,
                            topPos + 150 * bmpFactor
                        )
                        setBounds(this, rectF)
                        animationLayout?.addView(this)

                        val animation =
                            RotateAnimation(0f, 180f, rectF.width() / 2, rectF.height() / 2)
                        animation.setDuration(10000)
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        this.startAnimation(animation)
                    }
                }
            }

            3 -> {
                listAnimatedObjects = arrayOf(AnimatedImageView(Owner))
                run {
                    listAnimatedObjects[0].setImageResource(R.drawable.luna)
                    //ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
                    val rectF = RectF(
                        Owner.viewWidth - 260 * bmpFactor,
                        0f,
                        Owner.viewWidth.toFloat(),
                        260 * bmpFactor
                    )
                    setBounds(listAnimatedObjects[0], rectF)
                    animationLayout!!.addView(listAnimatedObjects[0])

                    val animation: Animation =
                        RotateAnimation(0f, 22f, rectF.width() / 2, rectF.height() / 2)
                    animation.setDuration(2500)
                    animation.repeatMode = Animation.REVERSE
                    animation.setRepeatCount(Animation.INFINITE)
                    listAnimatedObjects[0].startAnimation(animation)
                }
            }

            4 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_snezhinka1,
                    R.drawable.animatiya_snezhinka2,
                    R.drawable.animatiya_snezhinka3,
                    R.drawable.animatiya_snezhinka4,
                    R.drawable.animatiya_snezhinka5
                )
                maxSize = Point((100 * bmpFactor).toInt(), (100 * bmpFactor).toInt())
                listAnimatedObjects = Array(10) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setSnowflake(this, mRnd.nextInt(8000))
                    }
                }
            }

            29 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_snezhinki1,
                    R.drawable.animatiya_snezhinki2,
                    R.drawable.animatiya_snezhinki3,
                    R.drawable.animatiya_snezhinki4,
                    R.drawable.animatiya_snezhinki5,
                    R.drawable.animatiya_snezhinki6,
                    R.drawable.animatiya_snezhinki7,
                    R.drawable.animatiya_snezhinki8,
                    R.drawable.animatiya_snezhinki9,
                    R.drawable.animatiya_snezhinki10,
                    R.drawable.animatiya_snezhinki11,
                    R.drawable.animatiya_snezhinki12,
                    R.drawable.animatiya_snezhinki13,
                    R.drawable.animatiya_snezhinki14,
                    R.drawable.animatiya_snezhinki15,
                    R.drawable.animatiya_snezhinki16,
                    R.drawable.animatiya_snezhinki17,
                    R.drawable.animatiya_snezhinki18,
                    R.drawable.animatiya_snezhinki19,
                    R.drawable.animatiya_snezhinki20
                )
                maxSize = Point((150 * bmpFactor).toInt(), (150 * bmpFactor).toInt())
                listAnimatedObjects = Array(10) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setSnowflake3D(this, mRnd.nextInt(8000))
                    }
                }
            }

            30 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_snezhinki1,
                    R.drawable.animatiya_snezhinki2,
                    R.drawable.animatiya_snezhinki3,
                    R.drawable.animatiya_snezhinki4,
                    R.drawable.animatiya_snezhinki5,
                    R.drawable.animatiya_snezhinki6,
                    R.drawable.animatiya_snezhinki7,
                    R.drawable.animatiya_snezhinki8,
                    R.drawable.animatiya_snezhinki9,
                    R.drawable.animatiya_snezhinki10,
                    R.drawable.animatiya_snezhinki11,
                    R.drawable.animatiya_snezhinki12,
                    R.drawable.animatiya_snezhinki13,
                    R.drawable.animatiya_snezhinki14,
                    R.drawable.animatiya_snezhinki15,
                    R.drawable.animatiya_snezhinki16,
                    R.drawable.animatiya_snezhinki17,
                    R.drawable.animatiya_snezhinki18,
                    R.drawable.animatiya_snezhinki19,
                    R.drawable.animatiya_snezhinki20
                )
                maxSize = Point((200 * bmpFactor).toInt(), (200 * bmpFactor).toInt())
                listAnimatedObjects = Array(3) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setRomashkaCurl(this)
                    }
                }
            }

            31 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_raketa01,
                    R.drawable.animaciya_raketa02,
                    R.drawable.animaciya_raketa03,
                    R.drawable.animaciya_raketa04
                )
                maxSize = Point((127 * bmpFactor).toInt(), (300 * bmpFactor).toInt())
                listAnimatedObjects = Array(6) { num ->
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        when (num) {
                            0, 1 -> {
                                setBackgroundResource(R.drawable.animaciya_raketa1)
                                (background as? AnimationDrawable)?.start()
                                setAlpha(0.4f)
                                setRaketaUp(this, mRnd.nextInt(3000))
                            }
                            2 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (390 - 600) * bmpFactor,
                                    topPos + 165 * bmpFactor,
                                    centerX + (490 - 600) * bmpFactor,
                                    topPos + 265 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.5f, 1f)
                                animation.setDuration(1000)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            3 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (900 - 600) * bmpFactor,
                                    topPos + 420 * bmpFactor,
                                    centerX + (970 - 600) * bmpFactor,
                                    topPos + 490 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.2f, 1f)
                                animation.setDuration(1300)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            4 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (680 - 600) * bmpFactor,
                                    topPos + 80 * bmpFactor,
                                    centerX + (760 - 600) * bmpFactor,
                                    topPos + 160 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.3f, 1f)
                                animation.setDuration(1600)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            5 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (980 - 600) * bmpFactor,
                                    topPos + 130 * bmpFactor,
                                    centerX + (1080 - 600) * bmpFactor,
                                    topPos + 230 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.1f, 1f)
                                animation.setDuration(1400)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                        }
                    }
                }
            }

            32 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_vozd_shar1,
                    R.drawable.animaciya_vozd_shar2,
                    R.drawable.animaciya_vozd_shar3
                )
                maxSize = Point((213 * bmpFactor).toInt(), (300 * bmpFactor).toInt())
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setAlpha(0.6f)
                        setVozdushnyjShar2Up(this, mRnd.nextInt(3000))
                    }
                }
            }

            33 -> {
                flakes = intArrayOf(R.drawable.animaciya_samoletik1)
                maxSize = Point((300 * bmpFactor).toInt(), (72 * bmpFactor).toInt())
                listAnimatedObjects = Array(2) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setAlpha(0.6f)
                        setSamoletikPath(this)
                    }
                }
            }

            34 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_samolet01,
                    R.drawable.animaciya_samolet02,
                    R.drawable.animaciya_samolet03,
                    R.drawable.animaciya_samolet04,
                    R.drawable.animaciya_samolet05,
                    R.drawable.animaciya_samolet06,
                    R.drawable.animaciya_samolet07,
                    R.drawable.animaciya_samolet08
                )
                maxSize = Point((600 * bmpFactor).toInt(), (148 * bmpFactor).toInt())
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setBackgroundResource(R.drawable.animaciya_samolet)
                        (background as? AnimationDrawable)?.let { babochkaAnimation ->
                            babochkaAnimation.start()
                            animationLayout?.addView(this)
                            setAlpha(0.6f)
                            setSamoletPath(this)
                        }
                    }
                }
            }

            37 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_nlo01,
                    R.drawable.animaciya_nlo02,
                    R.drawable.animaciya_nlo03,
                    R.drawable.animaciya_nlo04,
                    R.drawable.animaciya_nlo05
                )
                maxSize = Point((300 * bmpFactor).toInt(), (159 * bmpFactor).toInt())
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setBackgroundResource(R.drawable.animaciya_nlo1)
                        (background as? AnimationDrawable)?.let { babochkaAnimation ->
                        babochkaAnimation.start()
                        animationLayout?.addView(this)
                        setAlpha(0.6f)
                        setSamoletPath(this)
                            }
                    }
                }
            }

            16 -> {
                flakes = intArrayOf(R.drawable.animaciya_perekati_pole)
                maxSize = Point((100 * bmpFactor).toInt(), (100 * bmpFactor).toInt())
                listAnimatedObjects = Array(5) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setPerekatiPole(
                            this,
                            mRnd.nextInt(8000),
                            (330 * bmpFactor).toInt(),
                            (554 * bmpFactor).toInt()
                        )
                    }
                }
            }

            24 -> {
                flakes = intArrayOf(R.drawable.animaciya_perekati_pole)
                maxSize = Point((150 * bmpFactor).toInt(), (150 * bmpFactor).toInt())
                listAnimatedObjects = Array(2) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setPerekatiPole(
                            this,
                            mRnd.nextInt(8000),
                            (554 * bmpFactor).toInt(),
                            (615 * bmpFactor).toInt()
                        )
                    }
                }
            }

            17 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_volny1,
                    R.drawable.animaciya_volny2,
                    R.drawable.animaciya_volny3,
                    R.drawable.animaciya_volny4,
                    R.drawable.animaciya_volny5
                )
                val rectF = arrayOf(
                    RectF(-10f, 330f, (Owner.viewWidth + 20).toFloat(), 450f),
                    RectF(-10f, 383f, (Owner.viewWidth + 20).toFloat(), 533f),
                    RectF(-10f, 436f, (Owner.viewWidth + 20).toFloat(), 556f),
                    RectF(-10f, 489f, (Owner.viewWidth + 20).toFloat(), 609f),
                    RectF(-10f, 542f, (Owner.viewWidth + 20).toFloat(), 620f),
                )
                maxSize = Point((100 * bmpFactor).toInt(), (100 * bmpFactor).toInt())
                listAnimatedObjects = Array(rectF.size) {
                    AnimatedImageView(Owner).apply {
                        flakes?.let { it1 -> setImageResource(it1[it]) }
                        setBounds(
                            this,
                            rectF[it].left * bmpFactor,
                            rectF[it].top * bmpFactor,
                            rectF[it].right * bmpFactor,
                            rectF[it].bottom * bmpFactor
                        )
                        val animation = TranslateAnimation(0f, 10 * bmpFactor, 0f, 0f)
                        animation.setDuration((1000 + it * 200).toLong())
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        animationLayout!!.addView(this)
                        startAnimation(animation)
                    }
                }
            }

            18 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_pyanto1,
                    R.drawable.animatiya_pyanto2,
                    R.drawable.animatiya_pyanto3,
                    R.drawable.animatiya_pyanto4,
                    R.drawable.animatiya_pyanto5,
                    R.drawable.animatiya_pyanto6,
                    R.drawable.animatiya_pyanto7,
                    R.drawable.animatiya_pyanto8
                )
                maxSize = Point((100 * bmpFactor).toInt(), (100 * bmpFactor).toInt())
                listAnimatedObjects = Array(8) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                    }
                }
                setSalut()
            }

            5 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_pyanto1,
                    R.drawable.animatiya_pyanto2,
                    R.drawable.animatiya_pyanto3,
                    R.drawable.animatiya_pyanto4
                )
                maxSize = Point((200 * bmpFactor).toInt(), (200 * bmpFactor).toInt())
                listAnimatedObjects = Array(flakes?.size.orZero()) {
                    AnimatedImageView(Owner).apply {
                        flakes?.let { it1 -> setImageResource(it1[it]) }
                        val posX: Int = mRnd.nextInt((Owner.viewWidth - 200 * bmpFactor).toInt())
                        val posY: Int = mRnd.nextInt((Owner.viewHeight - 200 * bmpFactor).toInt())
                        setBounds(
                            this,
                            posX.toFloat(),
                            posY.toFloat(),
                            posX + 200 * bmpFactor,
                            posY + 200 * bmpFactor
                        )
                        animationLayout?.addView(this)
                        setPyatno(this)
                    }
                }
            }

            20 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_babochka_1,
                    R.drawable.animaciya_babochka_2,
                    R.drawable.animaciya_babochka_3,
                    R.drawable.animaciya_babochka_4,
                    R.drawable.animaciya_babochka_5,
                    R.drawable.animaciya_strekoza_1,
                    R.drawable.animaciya_strekoza_2,
                    R.drawable.animaciya_strekoza_3,
                    R.drawable.animaciya_strekoza_4,
                    R.drawable.animaciya_strekoza_5
                )
                maxSize = Point((204 * bmpFactor).toInt(), (158 * bmpFactor).toInt())
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setBabochkaPath(this)
                    }
                }
            }

            21 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_romashka1,
                    R.drawable.animaciya_romashka2,
                    R.drawable.animaciya_romashka3,
                    R.drawable.animaciya_romashka4,
                    R.drawable.animaciya_romashka5,
                    R.drawable.animaciya_romashka6,
                    R.drawable.animaciya_romashka7,
                    R.drawable.animaciya_romashka8,
                    R.drawable.animaciya_romashka9
                )
                maxSize = Point((200 * bmpFactor).toInt(), (200 * bmpFactor).toInt())
                listAnimatedObjects = Array(3) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setAlpha(0.6f)
                        setRomashkaPath(this)
                    }
                }
            }

            22 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_rybka01,
                    R.drawable.animaciya_rybka02,
                    R.drawable.animaciya_rybka03
                )
                flakes2 = intArrayOf(
                    R.drawable.animaciya_rybka1,
                    R.drawable.animaciya_rybka2,
                    R.drawable.animaciya_rybka3
                )
                maxSize = Point((160 * bmpFactor).toInt(), (115 * bmpFactor).toInt())
                listAnimatedObjects = Array(4) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setRybkaPath(this)
                    }
                }
            }

            36 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_podlodka01,
                    R.drawable.animaciya_podlodka02,
                    R.drawable.animaciya_podlodka03,
                    R.drawable.animaciya_podlodka04
                )
                flakes2 = intArrayOf(
                    R.drawable.animaciya_podlodka1,
                    R.drawable.animaciya_podlodka1,
                    R.drawable.animaciya_podlodka1,
                    R.drawable.animaciya_podlodka1
                )
                maxSize = Point((585 * bmpFactor).toInt(), (297 * bmpFactor).toInt())
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setRybkaPath(this)
                    }
                }
            }

            35 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_meduza01,
                    R.drawable.animaciya_meduza02,
                    R.drawable.animaciya_meduza03
                )
                flakes2 = intArrayOf(
                    R.drawable.animaciya_meduza1,
                    R.drawable.animaciya_meduza2,
                    R.drawable.animaciya_meduza3
                )
                maxSize = Point((112 * bmpFactor).toInt(), (183 * bmpFactor).toInt())
                listAnimatedObjects = Array(3) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setMeduzaPath(this)
                    }
                }
            }

            23 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_romashka1,
                    R.drawable.animaciya_romashka2,
                    R.drawable.animaciya_romashka3,
                    R.drawable.animaciya_romashka4,
                    R.drawable.animaciya_romashka5,
                    R.drawable.animaciya_romashka6,
                    R.drawable.animaciya_romashka7,
                    R.drawable.animaciya_romashka8,
                    R.drawable.animaciya_romashka9
                )
                maxSize = Point((200 * bmpFactor).toInt(), (200 * bmpFactor).toInt())
                listAnimatedObjects = Array(3) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setRomashkaCurl(this)
                    }
                }
            }

            25 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_sharik1,
                    R.drawable.animaciya_sharik2,
                    R.drawable.animaciya_sharik3,
                    R.drawable.animaciya_sharik4,
                    R.drawable.animaciya_sharik5,
                    R.drawable.animaciya_sharik6,
                    R.drawable.animaciya_sharik7
                )
                maxSize = Point((141 * bmpFactor).toInt(), (250 * bmpFactor).toInt())
                listAnimatedObjects = Array(5) {
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        setSharikUp(this, mRnd.nextInt(3000))
                    }
                }
            }

            26 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_parovozik01,
                    R.drawable.animaciya_parovozik02,
                    R.drawable.animaciya_avtobus01,
                    R.drawable.animaciya_avtobus02,
                    R.drawable.animaciya_gruzovik01
                )
                flakes2 = intArrayOf(
                    R.drawable.animaciya_parovozik1,
                    R.drawable.animaciya_parovozik1,
                    R.drawable.animaciya_avtobus1,
                    R.drawable.animaciya_avtobus1,
                    R.drawable.animaciya_gruzovik1
                )
                maxSize = Point((527 * bmpFactor).toInt(), (234 * bmpFactor).toInt())
                listAnimatedObjects = Array(2) { num ->
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        flakes?.let { setImageResource(it[num]) }
                    }
                }
            }

            28 -> {
                flakes = intArrayOf(
                    R.drawable.animaciya_kometa,
                    R.drawable.animaciya_kometa02,
                    R.drawable.animaciya_kometa03
                )
                maxSize = Point((403 * bmpFactor).toInt(), (128 * bmpFactor).toInt())
                listAnimatedObjects = Array(6) { num ->
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        when (num) {
                            0, 1 -> {
                                setAlpha(0.6f)
                                setKometaPath(this)
                            }
                            2 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (390 - 600) * bmpFactor,
                                    topPos + 165 * bmpFactor,
                                    centerX + (490 - 600) * bmpFactor,
                                    topPos + 265 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.5f, 1f)
                                animation.setDuration(1000)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            3 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (900 - 600) * bmpFactor,
                                    topPos + 420 * bmpFactor,
                                    centerX + (970 - 600) * bmpFactor,
                                    topPos + 490 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.2f, 1f)
                                animation.setDuration(1300)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            4 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (680 - 600) * bmpFactor,
                                    topPos + 80 * bmpFactor,
                                    centerX + (760 - 600) * bmpFactor,
                                    topPos + 160 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation: Animation = AlphaAnimation(0.3f, 1f)
                                animation.setDuration(1600)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            5 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (980 - 600) * bmpFactor,
                                    topPos + 130 * bmpFactor,
                                    centerX + (1080 - 600) * bmpFactor,
                                    topPos + 230 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation: Animation = AlphaAnimation(0.1f, 1f)
                                animation.setDuration(1400)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                        }
                    }
                }
            }

            12 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_pyanto5,
                    R.drawable.animatiya_pyanto5,
                    R.drawable.animatiya_pyanto5
                )
                maxSize = Point((400 * bmpFactor).toInt(), (400 * bmpFactor).toInt())
                listAnimatedObjects = Array(flakes?.size.orZero()) { num ->
                    AnimatedImageView(Owner).apply {
                        flakes?.get(num)?.let { setImageResource(it) }
                        val posX: Int = mRnd.nextInt((Owner.viewWidth - 400 * bmpFactor).toInt())
                        val posY: Int = mRnd.nextInt((Owner.viewHeight - 400 * bmpFactor).toInt())
                        setBounds(
                            this,
                            posX.toFloat(),
                            posY.toFloat(),
                            posX + 400 * bmpFactor,
                            posY + 400 * bmpFactor
                        )
                        animationLayout?.addView(this)
                        setPyatno(this)
                    }
                }
            }

            13 -> {
                //flakes=new int[]{R.drawable.animacya_koleso1,R.drawable.animatiya_pyanto5,R.drawable.animatiya_pyanto5};
                val points = arrayOf<PointF>(
                    PointF(427f, 498f),
                    PointF(351f, 498f),
                    PointF(273f, 498f),
                    PointF(197f, 498f),
                    PointF(122f, 498f),
                    PointF(46f, 498f),
                    PointF(503f, 498f),
                    PointF(579f, 498f),
                    PointF(881f, 499f),
                    PointF(954f, 499f),
                    PointF(789f, 477f),
                    PointF(673f, 477f),
                )
                listAnimatedObjects = Array(points.size) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animacya_koleso4)
                        val radius = 41.5f * (if (it < points.size - 2) 1f else 1.5f)
                        setBounds(
                            this,
                            leftPos + (points[it].x - radius) * bmpFactor,
                            topPos + (points[it].y - radius) * bmpFactor,
                            leftPos + (points[it].x + radius) * bmpFactor,
                            (points[it].y + radius) * bmpFactor
                        )
                        animationLayout?.addView(this)
                        val animation: Animation =
                            RotateAnimation(0f, 359f, radius * bmpFactor, radius * bmpFactor)
                        animation.setDuration(2500)
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            6 -> {
                flakes = intArrayOf(
                    R.drawable.animatiya_oblako1,
                    R.drawable.animatiya_oblako2,
                    R.drawable.animatiya_oblako3,
                    R.drawable.animatiya_oblako4,
                    R.drawable.animatiya_oblako1
                )
                maxSize = Point((100 * bmpFactor).toInt(), (100 * bmpFactor).toInt())
                val startPoints = arrayOf(
                    Rect(884, 18, 1126, 166),
                    Rect(693, 18, 865, 122),
                    Rect(606, 34, 736, 138),
                    Rect(64, 27, 342, 157),
                    Rect(388, 14, 560, 118),
                )
                listAnimatedObjects = Array(flakes?.size.orZero()) {
                    AnimatedImageView(Owner).apply {
                        flakes?.let { it1 -> setImageResource(it1[it]) }
                        setBounds(
                            this,
                            startPoints[it].left * bmpFactor,
                            startPoints[it].top * bmpFactor,
                            startPoints[it].right * bmpFactor,
                            startPoints[it].bottom * bmpFactor
                        )
                        animationLayout?.addView(this)
                        centralPoint = Point(
                            (startPoints[it].left * bmpFactor).toInt(),
                            (startPoints[it].top * bmpFactor).toInt()
                        )
                        setOblako(this)
                    }
                }
            }

            7 -> {
                listAnimatedObjects = Array(2) { num ->
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        when (num) {
                            0 -> {
                                setImageResource(R.drawable.animacya_barboskiny1)
                                val rectF = RectF(
                                    centerX - 205 * bmpFactor,
                                    0f,
                                    centerX + 205 * bmpFactor,
                                    173 * bmpFactor
                                )
                                setBounds(this, rectF)

                                val animation =
                                    RotateAnimation(0f, -2 * 4.17f, rectF.width() / 2, 24 * bmpFactor)
                                animation.setDuration(1500)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            1 -> {
                                setImageResource(R.drawable.animacya_barboskiny2)
                                //ListAnimatedObjects[1].setScaleType(ImageView.ScaleType.FIT_XY);
                                val rectF = RectF(
                                    centerX - 10.5f * bmpFactor,
                                    0f,
                                    centerX + 10.5f * bmpFactor,
                                    28 * bmpFactor
                                )
                                setBounds(this, rectF)
                            }
                        }
                    }
                }
            }

            8 -> {
                //flakes=new int[]{R.drawable.animatiya_oblako1,R.drawable.animatiya_oblako2,R.drawable.animatiya_oblako3,R.drawable.animatiya_oblako4,R.drawable.animatiya_oblako1};
                //maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
                val startPoints = arrayOf(
                    Rect(189, 269, 289, 365),
                    Rect(360, 95, 460, 195),
                    Rect(416, 442, 516, 542),
                    Rect(923, 255, 1023, 355),
                    Rect(927, 154, 1027, 254),
                    Rect(752, 38, 852, 138),
                    Rect(799, 493, 899, 593),
                    Rect(181, 470, 281, 570),
                    Rect(181, 470, 281, 570),
                    Rect(144, 334, 244, 434),
                    Rect(144, 192, 244, 292),
                    Rect(266, 180, 366, 280),
                    Rect(466, 109, 566, 209),
                    Rect(480, 280, 580, 380),
                    Rect(507, 384, 607, 484),
                    Rect(522, 453, 622, 553),
                    Rect(829, 142, 929, 242),
                    Rect(741, 134, 841, 234),
                    Rect(701, 80, 801, 180),
                    Rect(690, 279, 790, 379),
                )
                val iMulY: Float = Owner.viewHeight / 620f
                val iMulX: Float = Owner.viewWidth / 1013f
                listAnimatedObjects = Array(startPoints.size) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animatiya_pyanto1)
                        setBounds(
                            this,
                            startPoints[it].left * iMulX,
                            startPoints[it].top * iMulY,
                            startPoints[it].right * iMulX,
                            startPoints[it].bottom * iMulY
                        )
                        animationLayout?.addView(listAnimatedObjects[it])

                        val animation = AlphaAnimation(0f, 1f)
                        animation.setDuration((1000 + mRnd.nextInt(1000)).toLong())
                        animation.startOffset = mRnd.nextInt(10000).toLong()
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            9 -> {
                flakes = intArrayOf(
                    R.drawable.animacya_dom1,
                    R.drawable.animacya_dom2,
                    R.drawable.animacya_dom3,
                    R.drawable.animacya_dom4,
                    R.drawable.animacya_dom5,
                    R.drawable.animacya_dom6,
                    R.drawable.animacya_dom7,
                    R.drawable.animacya_dom8,
                    R.drawable.animacya_dom9
                )
                //maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
                val startPoints = arrayOf(
                    Rect(484, 366, 583, 433),
                    Rect(450, 473, 493, 535),
                    Rect(862, 488, 996, 556),
                    Rect(310, 592, 373, 620),
                    Rect(219, 502, 400, 549),
                    Rect(489, 538, 578, 620),
                    Rect(2, 409, 106, 498),
                    Rect(173, 473, 195, 553),
                    Rect(210, 350, 265, 403),
                )
                val iMulY: Float = Owner.viewHeight / 620f
                val iMulX: Float = Owner.viewWidth / 1200f
                listAnimatedObjects = Array(startPoints.size) { num ->
                    AnimatedImageView(Owner).apply {
                        flakes?.let { setImageResource(it[num]) }
                        setBounds(
                            this,
                            startPoints[num].left * iMulX,
                            startPoints[num].top * iMulY,
                            startPoints[num].right * iMulX,
                            startPoints[num].bottom * iMulY
                        )
                        animationLayout?.addView(this)

                        val animation = AlphaAnimation(0f, 1f)
                        animation.setDuration((2000 + mRnd.nextInt(2000)).toLong())
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            10 -> {
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animacya_smeshariki)
                        val rectF = RectF(
                            Owner.viewWidth / 2 - 175 * bmpFactor,
                            0f,
                            Owner.viewWidth / 2 + 175 * bmpFactor,
                            166 * bmpFactor
                        )
                        setBounds(this, rectF)
                        animationLayout?.addView(this)
                        val animation: Animation =
                            RotateAnimation(0f, -2 * 10f, rectF.width() / 2, rectF.height() / 2)
                        animation.setDuration(1500)
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            19 -> {
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animaciya_fixiki)
                        val rectF = RectF(
                            Owner.viewWidth / 2 - 169 * bmpFactor,
                            0f,
                            Owner.viewWidth / 2 + 169 * bmpFactor,
                            104 * bmpFactor
                        )
                        setBounds(this, rectF)
                        animationLayout?.addView(this)

                        val animation =
                            RotateAnimation(0f, -2 * 7f, rectF.width() / 2, 48 * bmpFactor)
                        animation.setDuration(1500)
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            14 -> {
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animaciya_chasy2)
                        //ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
                        val rectF = RectF(
                            leftPos + 837 * bmpFactor,
                            31 * bmpFactor,
                            leftPos + 846 * bmpFactor,
                            94 * bmpFactor
                        )
                        setBounds(this, rectF)
                        animationLayout?.addView(this)

                        val animation =
                            RotateAnimation(0f, 359f, rectF.width() / 2, 50.5f * bmpFactor)
                        animation.setDuration(10000)
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            15 -> {
                listAnimatedObjects = Array(1) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.animaciya_mama_na_kuhne)
                        val rectF = RectF(
                            leftPos + 546 * bmpFactor,
                            328 * bmpFactor,
                            leftPos + 690 * bmpFactor,
                            416 * bmpFactor
                        )
                        setBounds(this, rectF)
                        animationLayout?.addView(this)

                        val animation: Animation = AlphaAnimation(0f, 1f)
                        animation.setDuration(5000)
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            1 -> {
                listAnimatedObjects = Array(6) { num ->
                    AnimatedImageView(Owner).apply {
                        animationLayout?.addView(this)
                        when (num) {
                            0 -> {
                                setImageResource(R.drawable.kometa_head)
                                val rectF = RectF(
                                    centerX + (168 - 600) * bmpFactor,
                                    topPos + 109 * bmpFactor,
                                    centerX + (244 - 600) * bmpFactor,
                                    topPos + 182 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation =
                                    RotateAnimation(0f, 359f, rectF.width() / 2, rectF.height() / 2)
                                animation.setDuration(10000)
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            1 -> {
                                setImageResource(R.drawable.kometa_tail)
                                val rectF = RectF(
                                    centerX + (81 - 600) * bmpFactor,
                                    topPos + 51 * bmpFactor,
                                    centerX + (188 - 600) * bmpFactor,
                                    topPos + 133 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation =
                                    ScaleAnimation(1f, 1.2f, 1f, 1.2f, rectF.width(), rectF.height())
                                animation.setDuration(2000)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            2-> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (390 - 600) * bmpFactor,
                                    topPos + 165 * bmpFactor,
                                    centerX + (490 - 600) * bmpFactor,
                                    topPos + 265 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.5f, 1f)
                                animation.setDuration(1000)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            3-> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (900 - 600) * bmpFactor,
                                    topPos + 420 * bmpFactor,
                                    centerX + (970 - 600) * bmpFactor,
                                    topPos + 490 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.2f, 1f)
                                animation.setDuration(1300)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            4 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (680 - 600) * bmpFactor,
                                    topPos + 80 * bmpFactor,
                                    centerX + (760 - 600) * bmpFactor,
                                    topPos + 160 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation: Animation = AlphaAnimation(0.3f, 1f)
                                animation.setDuration(1600)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                            5 -> {
                                setImageResource(R.drawable.menu_star)
                                val rectF = RectF(
                                    centerX + (980 - 600) * bmpFactor,
                                    topPos + 130 * bmpFactor,
                                    centerX + (1080 - 600) * bmpFactor,
                                    topPos + 230 * bmpFactor
                                )
                                setBounds(this, rectF)
                                val animation = AlphaAnimation(0.1f, 1f)
                                animation.setDuration(1400)
                                animation.repeatMode = Animation.REVERSE
                                animation.setRepeatCount(Animation.INFINITE)
                                startAnimation(animation)
                            }
                        }
                    }
                }
            }

            11 -> {
                val startPoints = arrayOf(
                    Rect(189, 269, 289, 365),  //new Rect(360,95, 460,195),
                    Rect(416, 442, 516, 542),  //new Rect(923,255, 1023,355),
                    Rect(927, 154, 1027, 254),  //new Rect(752,38, 852,138),
                    Rect(799, 493, 899, 593),  //new Rect(181,470, 281,570),
                    Rect(181, 470, 281, 570),  //new Rect(144,334, 244,434),
                    Rect(144, 192, 244, 292),  //new Rect(266,180, 366,280),
                    Rect(466, 109, 566, 209),  //new Rect(480,280, 580,380),
                    Rect(507, 384, 607, 484),  //new Rect(522,453, 622,553),
                    Rect(829, 142, 929, 242),  //new Rect(741,134, 841,234),
                    Rect(701, 80, 801, 180),  //new Rect(690,279, 790,379),
                )
                val iMulY: Float = Owner.viewHeight / 620f
                val iMulX: Float = Owner.viewWidth / 1013f
                listAnimatedObjects = Array(startPoints.size) {
                    AnimatedImageView(Owner).apply {
                        setImageResource(R.drawable.star7)
                        setBounds(
                            this,
                            startPoints[it].left * iMulX,
                            startPoints[it].top * iMulY,
                            startPoints[it].right * iMulX,
                            startPoints[it].bottom * iMulY
                        )
                        animationLayout?.addView(this)

                        val animation: Animation = AlphaAnimation(0f, 1f)
                        animation.setDuration((1000 + mRnd.nextInt(1000)).toLong())
                        animation.startOffset = mRnd.nextInt(10000).toLong()
                        animation.repeatMode = Animation.REVERSE
                        animation.setRepeatCount(Animation.INFINITE)
                        startAnimation(animation)
                    }
                }
            }

            0 -> {
                val animationResources = intArrayOf(18, 5, 20, 21, 12, 23, 25)
                val randomAnimation: Int = mRnd.nextInt(animationResources.size)
                makeAnimatedBackground(animationResources[randomAnimation])
            }

            -1 -> {
                val animationResources = intArrayOf(28, 31, 33, 34, 37)
                val randomAnimation: Int = mRnd.nextInt(animationResources.size)
                makeAnimatedBackground(animationResources[randomAnimation])
            }

            -2 -> {
                val animationResources = intArrayOf(4, 29, 30, 12)
                val randomAnimation: Int = mRnd.nextInt(animationResources.size)
                makeAnimatedBackground(animationResources[randomAnimation])
            }

            -3 -> {
                val animationResources = intArrayOf(33, 31, 25, 26, 27, 33, 34)
                val randomAnimation: Int = mRnd.nextInt(animationResources.size)
                makeAnimatedBackground(animationResources[randomAnimation])
            }

            -4 -> {
                val animationResources = intArrayOf(22, 35, 36)
                val randomAnimation = mRnd.nextInt(animationResources.size)
                makeAnimatedBackground(animationResources[randomAnimation])
            }
        }
    }

    //Thread showStarsThread=null;
    var showStarsThread: Thread? = null

    fun setLevelBackground() {
        this@GameView.background =
            getImageFromObb(levels[currentLevelNum].background)?.toDrawable(Owner.resources)
    }

    fun startShowStarsTread() {
        showStarsThread = Thread(ShowStarsRunnable())
        showStarsThread?.start()
    }

    private fun startLevel() {
        post {
            isStartingLevel = true
            clearLevel()
            listClearObjects =
                arrayOfNulls(levels[currentLevelNum].items.size)
            if (currentLevelNum >= levels.size) currentLevelNum = 0

            listObjects = arrayOfNulls(levels[currentLevelNum].items.size)
            this@GameView.background = getImageFromObb(
                levels[currentLevelNum].background
            )?.toDrawable(Owner.getResources())
            if (levels[currentLevelNum].animatedBackground == 0 && levels[currentLevelNum].levelType != 7) {
                glassView = ImageView(Owner)
                glassView!!.setBackgroundColor(-0x7f000001) //Color.TRANSPARENT);
                this@GameView.setBounds(
                    glassView!!,
                    0f,
                    0f,
                    Owner.viewWidth.toFloat(),
                    Owner.viewHeight.toFloat()
                )
                this@GameView.addView(glassView)
            } else glassView = null
            var levelType = levels[currentLevelNum].levelType
            if (levelType == 0) {
                levelType = mRnd.nextInt(3) + 1
                if (levelType == 3) levelType = 4
            }
            animationLayout = null
            listBackground = ImageView(Owner)
            listBackground?.setImageBitmap(
                getImageFromObb(
                    levels[currentLevelNum].list
                )
            )
            //ListBackground.setImageResource(R.drawable.list_4);
            val r = resources
            val w = listBackground?.getDrawable()?.intrinsicWidth.orZero().toFloat()
            val h = listBackground?.getDrawable()?.intrinsicHeight.orZero().toFloat()
            val py = Owner.viewHeight.toFloat() / 620f
            val backWidth = listBackground?.getDrawable()
                ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
                ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
            val px= (Owner.viewWidth - TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                50f,
                r.displayMetrics
            )) / backWidth //(float) ListBackground.getDrawable().getIntrinsicWidth();
            if (px < py) screenFactor = px
            else screenFactor = py
            //float screen_factor=px;//((float)getWidth()/(float)getHeight())/(640f/310f);
            bmpFactor =
                screenFactor //620f/ListBackground.getHeight();///(620f/screen_factor);

            if (levelType == 1) centerX =
                Owner.viewWidth - bmpFactor * backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f
            else if (levelType == 5) centerX =
                bmpFactor * backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f
            else centerX = Owner.viewWidth / 2f
            topPos =
                (Owner.viewHeight - bmpFactor * 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/) / 2f
            leftPos =
                centerX - bmpFactor * backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f

            this@GameView.addView(listBackground)

            if (levelType != 5 && isAnimateBackground) {
                this@GameView.makeAnimatedBackground(levels[currentLevelNum].animatedBackground)
            }

            if (levelType == 1) createLevelType1()
            else if (levelType == 2) createLevelType2()
            else if (levelType == 3) createLevelType3()
            else if (levelType == 4) createLevelType4()
            else if (levelType == 5) createLevelType5()
            else if (levelType == 6) createLevelType6()
            else if (levelType == 7) createLevelType7()
            isStartingLevel = false
        }
    }

    fun createLevelType1() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        //float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
        listBackground?.let {
            setBounds(
                it,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }
        /*if(levels[current_level_num].animatedBackground!=0) {
				GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
			}*/
        objectLevelPosition = getRandomIndexes(levels[currentLevelNum].items.size)
        for (i in levels[currentLevelNum].items.indices) {
            val index = objectLevelPosition!![i]
            levels[currentLevelNum].items[i].pictureResource?.let {
                listClearObjects[index] =
                    newShadowImageView2(it)
            }
            listObjects[index] = newDecodeResourceWithSize(
                levels[currentLevelNum].items[i].pictureResource,
                bmpFactor
            )
            listObjects[index]?.mClearObject = listClearObjects[index]
            listObjects[index]?.mObjectNumber = i

            val objHeight = listObjects[index]?.bitmapHeight.orZero().toFloat() //getDrawable().getIntrinsicHeight();//*bmp_factor;
            val objWidth = listObjects[index]?.bitmapWidth.orZero().toFloat() //getDrawable().getIntrinsicWidth();//*bmp_factor;
            val posY = (mRnd.nextInt(Owner.viewHeight - objHeight.toInt())).toFloat()
            listObjects[index]?.let {
                setBounds(
                    it,
                    0f,
                    posY,
                    objWidth,
                    posY + objHeight
                )
            }
            listClearObjects[index]?.let {
                setBounds(
                    it,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2 + objHeight
                )
            }
            run {
                val animation = ScaleAnimation(0f, 1.2f, 0f, 1.2f)
                animation.setDuration(500)
                animation.startOffset = (i * 200).toLong()
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animation2: Animation = ScaleAnimation(1.2f, 1f, 1.2f, 1f)
                        animation2.setDuration(500)
                        listClearObjects[index]?.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                listClearObjects[index]?.startAnimation(animation)
            }
        }

        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType2() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        listBackground?.let {
            setBounds(
                it,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }

        for (i in levels[currentLevelNum].items.indices) {
            listClearObjects[i] =
                newShadowImageView2(levels[currentLevelNum].items[i].pictureResource)
            listObjects[i] = newDecodeResourceWithSize2(
                levels[currentLevelNum].items.get(index = i).pictureResource,
                bmpFactor
            )
            listObjects[i]?.mClearObject = listClearObjects[i]
            listObjects[i]?.mObjectNumber = i

            val objHeight =
                listObjects[i]?.bitmapHeight.orZero().toFloat() //getDrawable().getIntrinsicHeight();//*bmp_factor;
            val objWidth =
                listObjects[i]?.bitmapWidth.orZero().toFloat() //getDrawable().getIntrinsicWidth();//*bmp_factor;
            val radius =
                sqrt(((objHeight / 2) * (objHeight / 2) + (objWidth / 2) * (objWidth / 2)).toDouble()).toFloat()
            val objSize = radius * 2
            val posY = (mRnd.nextInt(Owner.viewHeight - objHeight.toInt())).toFloat()
            listObjects[i]?.let {
                setBounds(
                    it,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale - objSize / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objSize / 2f,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale + objSize / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale + objSize / 2f
                )
            }
            (listObjects[i] as? RotateImageView)?.setCentralPoint(objSize / 2f, objSize / 2f)
            listObjects[i]?.angle = (mRnd.nextInt(300) + 30).toFloat()
            listClearObjects[i]?.let {
                setBounds(
                    it,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2f,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale + objHeight / 2f
                )
            }
            run {
                val animation: Animation =
                    ScaleAnimation(0f, 1.2f, 0f, 1.2f, objWidth / 2, objHeight / 2)
                animation.setDuration(500)
                animation.startOffset = (i * 200).toLong()
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animation2: Animation =
                            ScaleAnimation(1.2f, 1f, 1.2f, 1f, objWidth / 2, objHeight / 2)
                        animation2.setDuration(500)
                        listClearObjects[i]?.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                listClearObjects[i]?.startAnimation(animation)
            }
            run {
                val animationRotate: Animation =
                    RotateAnimation(0f, 359f, objSize / 2, objSize / 2)
                //animationRotate.setDuration(400);
                animationRotate.repeatMode = 2
                animationRotate.startOffset = (i * 200).toLong()
                animationRotate.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animation2: Animation = RotateAnimation(
                            0f,
                            listObjects[i]?.angle.orZero(),
                            objSize / 2,
                            objSize / 2
                        )
                        animation2.setDuration((400 * (listObjects[i]?.angle.orZero()) / 360).toLong())
                        listObjects[i]?.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                val animationAlpha: Animation = AlphaAnimation(0f, 1f)
                val animationSet = AnimationSet(true)
                animationSet.setDuration(800)
                animationSet.setStartOffset((i * 200).toLong())
                animationSet.addAnimation(animationAlpha)
                animationSet.addAnimation(animationRotate)
                listObjects[i]?.startAnimation(animationSet)
            }
        }
        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType3() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        listBackground?.let {
            setBounds(
                it,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }

        val randomIndexes: IntArray = getRandomIndexes(levels[currentLevelNum].items.size)
        for (i in levels[currentLevelNum].items.indices) {
            listClearObjects[i] =
                newShadowImageView2(levels[currentLevelNum].items[i].pictureResource)
            listObjects[i] = ColorImageView(
                Owner,
                this,
                levels[currentLevelNum].items[i].pictureResource
            )
            listObjects[i]?.setImageBitmap(
                BitmapFactory.decodeResource(
                    resources,
                    levels[currentLevelNum].items[i].colorResource
                )
            )
            //ListObjects[i].setBackgroundColor(Color.RED);
            listObjects[i]?.mClearObject = listClearObjects[i]
            listObjects[i]?.mObjectNumber = i
            this.addView(listObjects[i])

            val objHeight = listClearObjects[i]?.bitmap?.getHeight().orZero() * bmpFactor
            val objWidth = listClearObjects[i]?.bitmap?.getWidth().orZero() * bmpFactor
            listObjects[i]?.let {
                setBounds(
                    it,
                    centerX - (levels[currentLevelNum].items.size * 100 * gridScale) / 2 + randomIndexes[i] * 100 * gridScale,
                    Owner.viewHeight - 100 * gridScale,
                    centerX - (levels[currentLevelNum].items.size * 100 * gridScale) / 2 + (randomIndexes[i] + 1) * 100 * gridScale,
                    Owner.viewHeight.toFloat()
                )
            }
            //ListClearObjects[i].setBackgroundColor(Color.RED);
            listObjects[i]?.setDockPoint(
                centerX - (levels[currentLevelNum].items.size * 100 * gridScale) / 2 + randomIndexes[i] * 100 * gridScale,
                Owner.viewHeight - 100 * gridScale
            )
            listClearObjects[i]?.let {
                setBounds(
                    it,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2 + objHeight
                )
            }
            //ListClearObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
            run {
                val animation: Animation = AlphaAnimation(0f, 1f)
                animation.setDuration(500)
                animation.startOffset = (i * 200).toLong()
                listClearObjects[i]?.startAnimation(animation)
                listObjects[i]?.startAnimation(animation)
            }
        }
        listBackground?.bringToFront()
        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType4() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        //float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
        //CenterX = Owner.getViewWidth() / 2f;
        //Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
        setBounds(
            listBackground!!,
            centerX -  /*ListBackground.getDrawable().getIntrinsicWidth()*/backWidth / 2f * bmpFactor,
            topPos,
            centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
            topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
        )

        /*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/
        for (i in levels[currentLevelNum].items.indices) {
            listClearObjects[i] =
                newShadowImageView2(levels[currentLevelNum].items[i].pictureResource)
            listObjects[i] = newDecodeResourceWithSize3(
                levels[currentLevelNum].items[i].pictureResource,
                bmpFactor
            )
            listObjects[i]?.mClearObject = listClearObjects[i]
            listObjects[i]?.mObjectNumber = i

            val objHeight = listObjects[i]?.bitmap?.getHeight().orZero().toFloat() //getDrawable().getIntrinsicHeight();//*bmp_factor;
            val objWidth = listObjects[i]?.bitmap?.getWidth().orZero().toFloat() //getDrawable().getIntrinsicWidth();//*bmp_factor;
            val pos_y = (mRnd.nextInt(Owner.viewHeight - objHeight.toInt())).toFloat()
            setBounds(
                listObjects[i]!!,
                centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2,
                topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2,
                centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2,
                topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2 + objHeight
            )
            //ListObjects[i].setRotation(mRnd.nextInt(300)+30);
            setBounds(
                listClearObjects!![i]!!,
                centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2,
                topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2,
                centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2,
                topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2 + objHeight
            )
            run {
                val animation: Animation =
                    ScaleAnimation(0f, 1.2f, 0f, 1.2f, objWidth / 2, objHeight / 2)
                animation.setDuration(500)
                animation.startOffset = (i * 200).toLong()
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animation2 =
                            ScaleAnimation(1.2f, 1f, 1.2f, 1f, objWidth / 2, objHeight / 2)
                        animation2.setDuration(500)
                        listClearObjects[i]?.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                listClearObjects[i]?.startAnimation(animation)
            }
            /*{
				Animation animationRotate = new RotateAnimation(0, 359, obj_width/2, obj_height/2);
				//animationRotate.setDuration(400);
				animationRotate.setRepeatMode(2);
				animationRotate.setStartOffset(i * 200);
				final int finalI = i;
				animationRotate.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new RotateAnimation(0, ListObjects[finalI].getRotation(), obj_width/2, obj_height/2);
						animation2.setDuration((long) (400*(ListObjects[finalI].getRotation())/360));
						ListObjects[finalI].startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				Animation animationAlpha = new AlphaAnimation(0, 1);
				//animationAlpha.setDuration(800);
				AnimationSet animationSet=new AnimationSet(true);
				animationSet.setDuration(800);
				animationSet.setStartOffset(i*200);
				animationSet.addAnimation(animationAlpha);
				animationSet.addAnimation(animationRotate);
				ListObjects[i].startAnimation(animationSet);
			}*/
        }
        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType5() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.intrinsicWidth.orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.intrinsicHeight.orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        //float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
        //CenterX = Owner.getViewWidth() / 2f;
        //Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
        listBackground?.let {
            setBounds(
                it,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }

        /*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/
        val randomIndexes = getRandomIndexes(levels[currentLevelNum].items.size)
        val resultLeft = backWidth //ListBackground.getDrawable().getIntrinsicHeight()*bmp_factor);
        val resultCenter = (resultLeft + (1200 - backWidth) / 2) * gridScale
        for (i in levels[currentLevelNum].items.indices) {
            listClearObjects[i] =
                newShadowImageView2(levels[currentLevelNum].items[i].pictureResource)
            listObjects[i] = ApplicationImageView(Owner, this).apply {
                //ListObjects[i].setBackgroundColor(Color.RED);
                mClearObject = listClearObjects!![i]
                mObjectNumber = i
                addView(this)
            }

            val objHeight = listClearObjects[i]?.bitmap?.getHeight().orZero() * bmpFactor
            val objWidth = listClearObjects[i]?.bitmap?.getWidth().orZero() * bmpFactor
            val pictureItem = levels[currentLevelNum].items[i] as ApplicationPictureItem
            val rectF = RectF(
                pictureItem.listX0 * bmpFactor - objWidth / 2f,
                topPos + pictureItem.listY0 * bmpFactor - objHeight / 2f,
                pictureItem.listX0 * bmpFactor + objWidth / 2f,
                topPos + pictureItem.listY0 * bmpFactor + objHeight / 2f
            )
            setBounds(listObjects[i]!!, rectF)
            (listObjects[i] as ApplicationImageView).strokeWidth = Owner.viewHeight / 200f
            (listObjects[i] as ApplicationImageView).rotateAngle = pictureItem.rotateAngle
            getImageFromObb(
                levels[currentLevelNum].items[i].pictureResource
            )?.let {
                (listObjects[i] as ApplicationImageView).setImageBitmap(
                    it, bmpFactor
                )
            }
            //ListClearObjects[i].setBackgroundColor(Color.RED);
            //ListObjects[i].setDockPoint(CenterX -(levels[current_level_num].items.length*100*grid_scale)/2 + randomIndexes[i] * 100 * grid_scale,
            //		Owner.getViewHeight() - 100 * grid_scale);
            val item = levels[currentLevelNum].items[i] as? ApplicationPictureItem
            listClearObjects[i]?.let {
                setBounds(
                    it,
                    resultCenter + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2,
                    resultCenter + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2 + objHeight
                )
            }
            listClearObjects[i]?.setRotation(pictureItem.rotateAngle.toFloat())
            //ListClearObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
            run {
                val animation: Animation = AlphaAnimation(0f, 1f)
                animation.setDuration(500)
                animation.startOffset = (i * 200).toLong()
                listClearObjects[i]?.startAnimation(animation)
                listObjects[i]?.startAnimation(animation)
            }
        }
        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType6() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.getIntrinsicWidth().orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground?.getDrawable()
            ?.getIntrinsicHeight().orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        listBackground?.let {
            setBounds(
                it,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }
        /*if(levels[current_level_num].animatedBackground!=0) {
				GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
			}*/
        val objectsIndexes = getRandomIndexes(levels[currentLevelNum].items.size)
        for (i in levels[currentLevelNum].items.indices) {
            //if (current_level_num != 29)
            newShadowImageView2(levels[currentLevelNum].items[i].pictureResource).apply {
                listClearObjects[i] = this

                listObjects[i] = SymbolImageView(
                    Owner,
                    this@GameView,
                    levels[currentLevelNum].items[i].colorResource
                )
                getImageFromObb(
                    levels[currentLevelNum].items[i].pictureResource
                )?.let {
                    listObjects[i]?.setImageBitmap(
                        it, bmpFactor
                    )
                }
                listObjects[i]?.mClearObject = listClearObjects!![i]
                listObjects[i]?.mObjectNumber = i
                addView(listObjects[i])
            }

            val objHeight = (levels[currentLevelNum] as WordLevelItem).symbolWidth * bmpFactor
            val objWidth = (levels[currentLevelNum] as WordLevelItem).symbolWidth * bmpFactor
            val posY = (mRnd.nextInt(Owner.viewHeight - objHeight.toInt())).toFloat()
            listObjects[i]?.let {
                setBounds(
                    it,
                    (Owner.viewWidth - levels[currentLevelNum].items.size * objWidth) / 2f + objectsIndexes[i] * objWidth,
                    Owner.viewHeight - objHeight,
                    (Owner.viewWidth - levels[currentLevelNum].items.size * objWidth) / 2f + (objectsIndexes[i] + 1) * objWidth,
                    Owner.viewHeight.toFloat()
                )
            }
            listObjects[i]?.setDockPoint(
                (Owner.viewWidth - levels[currentLevelNum].items.size * objWidth) / 2f + objectsIndexes[i] * objWidth,
                Owner.viewHeight - objHeight
            )
            listClearObjects[i]?.let {
                setBounds(
                    it,
                    (Owner.viewWidth - levels[currentLevelNum].items.size * objWidth) / 2f + i * objWidth,
                    (Owner.viewHeight - objHeight) / 2f,
                    (Owner.viewWidth - levels[currentLevelNum].items.size * objWidth) / 2f + (i + 1) * objWidth,
                    (Owner.viewHeight + objHeight) / 2f
                )
            }
                val animation: Animation = ScaleAnimation(0f, 1.2f, 0f, 1.2f)
                animation.setDuration(500)
                animation.setStartOffset((i * 200).toLong())
                val finalI = i
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animation2 = ScaleAnimation(1.2f, 1f, 1.2f, 1f)
                        animation2.setDuration(500)
                        listClearObjects[finalI]?.startAnimation(animation2)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                listClearObjects[i]?.startAnimation(animation)
        }

        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    fun createLevelType7() {
        val gridScale =
            bmpFactor //(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
        val backWidth = listBackground?.getDrawable()
            ?.getIntrinsicWidth()
            .orZero() * (if (isScaledBitmaps) 2 else 1) / (listBackground!!.getDrawable()
            ?.getIntrinsicHeight().orZero() * (if (isScaledBitmaps) 2 else 1) / 620f)
        listBackground?.let { listBackground ->
            setBounds(
                listBackground,
                centerX - backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos,
                centerX + backWidth /*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmpFactor,
                topPos + 620 /*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmpFactor
            )
        }
        val objectsIndexes: IntArray = getRandomIndexes(levels[currentLevelNum].items.size)
        val moveToSidesLevelItem = levels[currentLevelNum] as MoveToSidesLevelItem
        val currentSizePositions = IntArray(moveToSidesLevelItem.sidesItems?.size.orZero())
        for (i in currentSizePositions.indices) {
            currentSizePositions[i] = 0
        }
        for (i in levels[currentLevelNum].items.indices) {
            listClearObjects[i] =
                newShadowImageView2(levels[currentLevelNum].items[i].pictureResource)
            listObjects[i] = SymbolImageView(
                Owner,
                this,
                levels[currentLevelNum].items[i].colorResource
            )
            getImageFromObb(levels[currentLevelNum].items[i].pictureResource)?.let {
                listObjects[i]?.setImageBitmap(
                    it, bmpFactor
                )
            }
            listObjects[i]?.mClearObject = listClearObjects[i]
            listObjects[i]?.mObjectNumber = i
            this.addView(listObjects[i])

            val objHeight = moveToSidesLevelItem.symbolWidth * bmpFactor
            val objWidth = moveToSidesLevelItem.symbolWidth * bmpFactor
            listObjects[i]?.let {
                setBounds(
                    it,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2f,
                    centerX + levels[currentLevelNum].items[i].x0 * gridScale + objWidth / 2f,
                    topPos + levels[currentLevelNum].items[i].y0 * gridScale + objHeight / 2f
                )
            }
            listObjects[i]?.setDockPoint(
                centerX + levels[currentLevelNum].items[i].x0 * gridScale - objWidth / 2f,
                topPos + levels[currentLevelNum].items[i].y0 * gridScale - objHeight / 2f
            )
            if (levels[currentLevelNum].items[i].colorResource == 0) {
                val topPosition = (Owner.viewHeight - moveToSidesLevelItem.sidesItems?.getOrNull(0)
                    .orZero() * objWidth) / 2f
                listClearObjects[i]?.let {
                    setBounds(
                        it,
                        0f,
                        topPosition + currentSizePositions[0] * objHeight,
                        objWidth,
                        topPosition + (currentSizePositions[0] + 1) * objHeight
                    )
                }
            }
            if (levels[currentLevelNum].items[i].colorResource == 1) {
                val topPosition: Float =
                    (Owner.viewHeight - moveToSidesLevelItem.sidesItems!![1] * objWidth) / 2f
                listClearObjects[i]?.let {
                    setBounds(
                        it,
                        Owner.viewWidth - objWidth,
                        topPosition + currentSizePositions[1] * objHeight,
                        Owner.viewWidth.toFloat(),
                        topPosition + (currentSizePositions[1] + 1) * objHeight
                    )
                }
            }
            if (levels[currentLevelNum].items[i].colorResource == 2) {
                val leftPosition: Float =
                    (Owner.viewWidth - moveToSidesLevelItem.sidesItems!![2] * objWidth) / 2f
                listClearObjects[i]?.let {
                    setBounds(
                        it,
                        leftPosition + currentSizePositions[2] * objWidth,
                        0f,
                        leftPosition + (currentSizePositions[2] + 1) * objWidth,
                        objHeight
                    )
                }
            }
            if (levels[currentLevelNum].items[i].colorResource == 3) {
                val leftPosition = (Owner.viewWidth - moveToSidesLevelItem.sidesItems?.getOrNull(3)
                    .orZero() * objWidth) / 2f
                listClearObjects[i]?.let {
                    setBounds(
                        it,
                        leftPosition + currentSizePositions[3] * objWidth,
                        Owner.viewHeight - objHeight,
                        leftPosition + (currentSizePositions[3] + 1) * objWidth,
                        Owner.viewHeight.toFloat()
                    )
                }
            }
            currentSizePositions[levels[currentLevelNum].items[i].colorResource]++
            val animation = ScaleAnimation(0f, 1.2f, 0f, 1.2f)
            animation.setDuration(500)
            animation.startOffset = (i * 200).toLong()
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val animation2: Animation = ScaleAnimation(1.2f, 1f, 1.2f, 1f)
                    animation2.setDuration(500)
                    listClearObjects[i]?.startAnimation(animation2)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            listClearObjects[i]?.startAnimation(animation)
        }

        soundFromResId(levels[currentLevelNum].sound)
        currentScreenType = 1
        start_time = System.currentTimeMillis()
        isStartingLevel = false
    }

    var selectLevelsBackground: ImageView? = null
    var selectLevelsMask: ImageView? = null
    
    private fun clearSelectLevels() {
        for (i in levelsImages!!.indices) {
            recycleBitmap(levelsImages!![i])
        }
        recycleBitmap(selectLevelsBackground)
        recycleBitmap(selectLevelsMask)
        recycleBitmap(shtoraImageView)
    }

    private fun startSelectLevel(nextScreen: Int) {
        if (nextScreen == 3) {
            shtoraImageView = ImageView(Owner)
            addView(shtoraImageView)
        }
        shtoraImageView?.setBackgroundResource(R.drawable.animaciya_shtora2)
        val animationDrawable = shtoraImageView?.background as? AnimationDrawable
        animationDrawable?.start()
        shtoraImageView?.let {
            setBounds(
                it,
                0f,
                0f,
                Owner.viewWidth.toFloat(),
                Owner.viewHeight.toFloat()
            )
        }
        var totalDuration: Long = 0
        for (i in 0..<animationDrawable?.numberOfFrames.orZero()) {
            totalDuration += animationDrawable?.getDuration(i).orZero().toLong()
        }
        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                shtoraImageView?.post {
                    ClearMainMenu()
                    if (nextScreen == 3) clearLevel()
                    if (nextScreen == 1 || nextScreen == 3) makeFourLevelsIcons()
                    else if (nextScreen == 2) InitSettings()
                    recycleBitmap(shtoraImageView)
                    shtoraImageView = ImageView(Owner)
                    shtoraImageView?.setBackgroundResource(R.drawable.animaciya_shtora1)
                    addView(shtoraImageView)
                    val animationDrawable = shtoraImageView?.background as? AnimationDrawable
                    animationDrawable?.start()
                    shtoraImageView?.let {
                        setBounds(
                            it,
                            0f,
                            0f,
                            Owner.viewWidth.toFloat(),
                            Owner.viewHeight.toFloat()
                        )
                    }
                    var totalDuration: Long = 0
                    for (i in 0..<animationDrawable?.numberOfFrames.orZero()) {
                        totalDuration += animationDrawable?.getDuration(i).orZero().toLong()
                    }
                    val timer = Timer()
                    val timerTask: TimerTask = object : TimerTask() {
                        override fun run() {
                            shtoraImageView?.post { shtoraImageView?.setBackgroundColor(Color.TRANSPARENT) }
                        }
                    }
                    timer.schedule(timerTask, totalDuration)
                }
            }
        }
        timer.schedule(timerTask, totalDuration)
    }

    private fun makeFourLevelsIcons() {
        clearSelectLevels()
        selectLevelsBackground = ImageView(Owner)
        setBounds(
            selectLevelsBackground!!,
            0f,
            0f,
            Owner.viewWidth.toFloat(),
            Owner.viewHeight.toFloat()
        )
        selectLevelsBackground?.setBackgroundColor(Color.WHITE)
        addView(selectLevelsBackground)
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            getResources().getDisplayMetrics()
        )
        val px2 = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            50f,
            resources.displayMetrics
        )
        val width: Float = Owner.viewWidth / 2 - px2 - px
        val height: Float = Owner.viewHeight / 2 - px
        val iMulWidth: Float = Owner.viewWidth / 1200f
        val iMulHeight: Float = Owner.viewHeight / 620f
        val levelsRects = arrayOf(
            Rect(105, 191, 469, 430),
            Rect(415, 41, 780, 280),
            Rect(415, 340, 780, 579),
            Rect(732, 191, 1096, 430),
        )
        val levelsExcludeRects = arrayOf(
            Rect(402, 191, 469, 288),
            Rect(716, 175, 780, 280),
            Rect(415, 340, 485, 446),
            Rect(732, 322, 798, 430),
        )
        for (i in levelsRects.indices) {
            if (selectLevelPart * 4 + i < levels.size) {
                val excludeRect = Rect(
                    ((levelsExcludeRects[i].left - levelsRects[i].left) * iMulWidth).toInt(),
                    ((levelsExcludeRects[i].top - levelsRects[i].top) * iMulHeight).toInt(),
                    ((levelsExcludeRects[i].right - levelsRects[i].left) * iMulWidth).toInt(),
                    ((levelsExcludeRects[i].bottom - levelsRects[i].top) * iMulHeight).toInt()
                )
                levelsImages[i] = newLevelIcon(
                    selectLevelPart * 4 + i,
                    levelsRects[i].width() * iMulWidth,
                    levelsRects[i].height() * iMulHeight,
                    excludeRect
                )
                val col = i / 2
                val row = i % 2
                val centerX1 =
                    (Owner.viewWidth / 2 + (if (col == 0) -1 else 1) * (levelsImages[i]?.getDrawable()
                        ?.intrinsicWidth.orZero() / 2 + px / 2)).toInt()
                val centerY =
                    (Owner.viewHeight / 2 + (if (row == 0) -1 else 1) * (levelsImages[i]?.getDrawable()
                        ?.intrinsicHeight.orZero() / 2 + px / 2)).toInt()
                levelsImages[i]?.let {
                    setBounds(
                        it,
                        levelsRects[i].left * iMulWidth,
                        levelsRects[i].top * iMulHeight,
                        levelsRects[i].right * iMulWidth,
                        levelsRects[i].bottom * iMulHeight
                    )
                }
                levelsImages[i]?.setScaleType(ImageView.ScaleType.FIT_XY)
                val animationAlpha: Animation = AlphaAnimation(0f, 1f)
                animationAlpha.setDuration(500)
                animationAlpha.startOffset = (i * 100).toLong()
                var x0 = excludeRect.left.toFloat()
                var y0 = excludeRect.top.toFloat()
                if (i == 0 || i == 2) y0 = excludeRect.bottom.toFloat()
                if (i == 2 || i == 3) x0 = excludeRect.right.toFloat()
                val animationScale: Animation = ScaleAnimation(0.5f, 1.04f, 0.5f, 1.04f, x0, y0)
                animationScale.setDuration(500)
                animationScale.startOffset = (i * 100).toLong()
                val animationSet = AnimationSet(true)
                animationSet.addAnimation(animationAlpha)
                animationSet.addAnimation(animationScale)
                val finalX = x0
                val finalY = y0
                animationSet.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        val animationScale: Animation =
                            ScaleAnimation(1.04f, 1f, 1.04f, 1f, finalX, finalY)
                        animationScale.setDuration(300)
                        levelsImages[i]?.startAnimation(animationScale)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
                levelsImages[i]?.startAnimation(animationSet)
            }
        }
        selectLevelsMask = newImageView(R.drawable.select_level_fon)
        selectLevelsMask?.setScaleType(ImageView.ScaleType.FIT_XY)
        selectLevelsMask?.let {
            setBounds(
                it,
                0f,
                0f,
                Owner.viewWidth.toFloat(),
                Owner.viewHeight.toFloat()
            )
        }
        if (selectLevelPart > 0) {
            newImageView(R.drawable.prev).apply {
                levelsImages[4] = this
                setScaleType(ImageView.ScaleType.FIT_XY)
                setBounds(
                    this,
                    0f,
                    Owner.viewHeight / 2 - px2,
                    px2,
                    Owner.viewHeight / 2 + px2
                )
                setBlinkView(this)
            }
        }
        if (selectLevelPart * 4 + 3 < levels.size) {
            newImageView(R.drawable.next).apply {
                levelsImages[5] = this
                setScaleType(ImageView.ScaleType.FIT_XY)
                setBounds(
                    this,
                    Owner.viewWidth - px2,
                    Owner.viewHeight / 2 - px2,
                    Owner.viewWidth.toFloat(),
                    Owner.viewHeight / 2 + px2
                )
            }
            setBlinkView(this)
        }
    }

    private fun makeLevelIcon(num: Int, width: Float, height: Float, excludeRect: Rect): Bitmap {
        val opts1 = BitmapFactory.Options()
        opts1.inScaled = false
        opts1.inSampleSize = 4

        //Bitmap ListBackground=Bitmap.createBitmap((int)width, (int)height, Config.ARGB_8888);
        val ret = createBitmap(width.toInt(), height.toInt())
        val listObjects = arrayOfNulls<Bitmap>(levels[num].items.size)
        val listObjectsRect = arrayOfNulls<RectF>(levels[num].items.size)
        val listBackground1 = getImageFromObb(levels[num].list) //, opts1);
        val background1 = getImageFromObb(levels[num].background) //, opts1);

        //Resources r=getResources();
        val py = height / listBackground1?.getHeight().orZero().toFloat()
        val px = width / listBackground1?.getWidth().orZero().toFloat()
        val screenFactor = if (px < py) px
                           else py
        val bmpFactor = screenFactor //620f/ListBackground.getHeight();///(620f/screen_factor);
        val gridScale = (listBackground1?.getHeight().orZero() * bmpFactor) / 620
        val centerX = width / 2f
        val top = (height - bmpFactor * listBackground1?.getHeight().orZero()) / 2f

        /*int first_object_num=0;
        	for(int level_num=0;level_num<num;level_num++)
        	{
        		if(level_num==num)
        			break;
        		for(;first_object_num<PagesObjects.length;first_object_num++)
        		{
        			if(PagesObjects[first_object_num].equals("-"))
        			{
        				first_object_num++;
        				break;
        			}
        		}
        	}*/

        //int objects_num=0;
        for (i in levels[num].items.indices) {
            listObjects[i] = getImageFromObb(levels[num].items[i].pictureResource) //, opts1);

            var objHeight = listObjects[i]?.getHeight().orZero() * bmpFactor
            var objWidth = listObjects[i]?.getWidth().orZero() * bmpFactor
            if (levels[num].levelType == 6) {
                objWidth = (levels[num] as WordLevelItem).symbolWidth * bmpFactor
                objHeight = objWidth
            }
            if (levels[num].levelType == 7) {
                objWidth = (levels[num] as MoveToSidesLevelItem).symbolWidth * bmpFactor
                objHeight = objWidth
            }
            listObjectsRect[i] = RectF(
                (centerX + (levels[num].items[i].x0 * gridScale)) - (objWidth / 2),
                top + levels[num].items[i].y0 * gridScale - objHeight / 2,
                centerX + levels[num].items[i].x0 * gridScale + objWidth / 2,
                top + levels[num].items[i].y0 * gridScale - objHeight / 2 + objHeight
            )
        }
        val paint = Paint()
        val paint2 = Paint()
        paint2.setColor(Color.WHITE)
        //paint2.setAlpha(0x80);
        val canvas = Canvas(ret)
        canvas.clipRect(excludeRect, Region.Op.DIFFERENCE)
        canvas.drawRect(0f, 0f, ret.getWidth().toFloat(), ret.getHeight().toFloat(), paint2)
        background1?.let { canvas.drawBitmap(it, null, RectF(0f, 0f, width, height), paint) }
        if (levels[num].levelType != 5) listBackground1?.let {
            canvas.drawBitmap(
                it, null,
                RectF(
                    centerX - listBackground1.getWidth() / 2f * bmpFactor,
                    top,
                    centerX + listBackground1.getWidth() / 2f * bmpFactor,
                    top + listBackground1.getHeight() * bmpFactor
                ), paint
            )
        }
        for (i in levels[num].items.indices) {
            if (levels[num].levelType != 5) canvas.drawBitmap(
                listObjects[i]!!,
                null,
                listObjectsRect[i]!!,
                paint
            )
            else {
                canvas.withRotation(
                    (levels[num].items.get(i) as ApplicationPictureItem).rotateAngle.toFloat(),
                    listObjectsRect[i]!!.left + listObjectsRect[i]!!.width() / 2f,
                    listObjectsRect[i]!!.top + listObjectsRect[i]!!.height() / 2f
                ) {
                    drawBitmap(listObjects[i]!!, null, listObjectsRect[i]!!, paint)
                }
            }
        }
        for (i in listObjects.indices) {
            listObjects[i]?.recycle()
            listObjects[i] = null
        }
        listBackground1?.recycle()
        return ret
    }

    /*private Bitmap MakeBackgoundFromPiece(Bitmap bmp)
        {
    		int max_pos=bmp.getWidth()*bmp.getHeight();
    		int pixels1[]=new int[max_pos];
    		this.setTi
    		Bitmap tmp_bmp=Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
    		bmp.getPixels(pixels1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		for(int y=0;y<getHeight();y+=bmp.getHeight())
    		{
        		for(int x=0;x<getWidth();x+=bmp.getWidth())
        		{
        			int w=bmp.getWidth();
        			if(x+w>=getWidth())
        				w=getWidth()-x;
        			int h=bmp.getHeight();
        			if(y+h>=getHeight())
        				h=getHeight()-y;
            		tmp_bmp.setPixels(pixels1, 0, bmp.getWidth(), x, y, w, h);
        		}
    		}
    		//tmp_bmp.setPixels(pixels1, 0, bmp.getWidth(), 0, 0, getWidth(), getHeight());
    		return tmp_bmp;
        }*/
    @SuppressLint("UseKtx")
    private fun makeShadowBitmap(bmp: Bitmap): Bitmap {
        val maxPos = bmp.width * bmp.height
        val pixels1 = IntArray(maxPos)
        val pixels2 = IntArray(maxPos)
        val tmpBmp = createBitmap(bmp.width, bmp.height)
        bmp.getPixels(pixels1, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        for (y in 0..<bmp.height) {
            val rowPos = y * bmp.width
            val prevRowPos = (y - 1) * bmp.width
            val prevPrevRowPos = (y - 2) * bmp.width
            val prevPrevPrevRowPos = (y - 3) * bmp.width
            val nextRowPos = (y + 1) * bmp.width
            val nextNextRowPos = (y + 2) * bmp.width
            val nextNextNextRowPos = (y + 3) * bmp.width
            for (x in 0..<bmp.width) {
                val pixel = pixels1[rowPos + x]
                if (pixel != Color.TRANSPARENT) {
                    var isPrevRowTransparent = true
                    var isPrevPrevRowTransparent = true
                    var isPrevPrevPrevRowTransparent = true
                    if (prevRowPos >= 0) isPrevRowTransparent =
                        pixels1[prevRowPos + x] == Color.TRANSPARENT
                    if (prevPrevRowPos >= 0) isPrevPrevRowTransparent =
                        pixels1[prevPrevRowPos + x] == Color.TRANSPARENT
                    if (prevPrevPrevRowPos >= 0) isPrevPrevPrevRowTransparent =
                        pixels1[prevPrevPrevRowPos + x] == Color.TRANSPARENT
                    var isPrevPosTransparent = true
                    var isPrevPrevPosTransparent = true
                    var isPrevPrevPrevPosTransparent = true
                    if (x > 0) isPrevPosTransparent =
                        pixels1[rowPos + x - 1] == Color.TRANSPARENT
                    if (x > 1) isPrevPrevPosTransparent =
                        pixels1[rowPos + x - 2] == Color.TRANSPARENT
                    if (x > 2) isPrevPrevPrevPosTransparent =
                        pixels1[rowPos + x - 3] == Color.TRANSPARENT

                    var isNextRowTransparent = true
                    var isNextNextRowTransparent = true
                    var isNextNextNextRowTransparent = true
                    if (y < bmp.getHeight() - 1) isNextRowTransparent =
                        pixels1[nextRowPos + x] == Color.TRANSPARENT
                    if (y < bmp.getHeight() - 2) isNextNextRowTransparent =
                        pixels1[nextNextRowPos + x] == Color.TRANSPARENT
                    if (y < bmp.getHeight() - 3) isNextNextNextRowTransparent =
                        pixels1[nextNextNextRowPos + x] == Color.TRANSPARENT
                    var isNextPosTransparent = true
                    var isNextNextPosTransparent = true
                    var isNextNextNextPosTransparent = true
                    if (x < bmp.getWidth() - 1) isNextPosTransparent =
                        pixels1[rowPos + x + 1] == Color.TRANSPARENT
                    if (x < bmp.getWidth() - 2) isNextNextPosTransparent =
                        pixels1[rowPos + x + 2] == Color.TRANSPARENT
                    if (x < bmp.getWidth() - 3) isNextNextNextPosTransparent =
                        pixels1[rowPos + x + 3] == Color.TRANSPARENT

                    if (isPrevPosTransparent || isPrevPrevPosTransparent || isPrevPrevPrevRowTransparent || isPrevRowTransparent || isPrevPrevRowTransparent || isPrevPrevPrevPosTransparent ||
                        isNextPosTransparent || isNextNextPosTransparent || isNextNextNextRowTransparent || isNextRowTransparent || isNextNextRowTransparent || isNextNextNextPosTransparent
                    ) pixels2[rowPos + x] = (pixel and -0x1000000) //|0x8f000000;//|0x00ffffff;
                    else pixels2[rowPos + x] = (pixel and -0x41000000)
                } else pixels2[rowPos + x] = Color.TRANSPARENT
            }
        }
        tmpBmp.setPixels(pixels2, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight())
        return tmpBmp
    }

    private fun makeShadowBitmap2(bmp: Bitmap): Bitmap {
        val maxPos = bmp.width * bmp.height
        val pixels1 = IntArray(maxPos)
        val pixels2 = IntArray(maxPos)
        val tmpBmp = createBitmap(bmp.width, bmp.height)
        bmp.getPixels(pixels1, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        for (y in 0..<bmp.height) {
            val rowPos = y * bmp.width
            for (x in 0..<bmp.width) {
                val pixel = pixels1[rowPos + x]
                if (pixel != Color.TRANSPARENT) {
                    pixels2[rowPos + x] = pixel //Color.argb(a, r, g, b);
                } else pixels2[rowPos + x] = Color.TRANSPARENT
            }
        }
        tmpBmp.setPixels(pixels2, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        return tmpBmp
    }

    private fun makeWhiteBitmap(bmp: Bitmap): Bitmap {
        val maxPos = bmp.width * bmp.height
        val pixels1 = IntArray(maxPos)
        val pixels2 = IntArray(maxPos)
        val tmpBmp = createBitmap(bmp.width, bmp.height)
        bmp.getPixels(pixels1, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        for (y in 0..<bmp.height) {
            val rowPos = y * bmp.width
            for (x in 0..<bmp.width) {
                val pixel = pixels1[rowPos + x]
                if (pixel != Color.TRANSPARENT) pixels2[rowPos + x] =
                    (pixel and -0x1000000) or 0x00ffffff
                else pixels2[rowPos + x] = Color.TRANSPARENT
            }
        }
        tmpBmp.setPixels(pixels2, 0, bmp.width, 0, 0, bmp.width, bmp.height)
        return tmpBmp
    }

    private fun vibrate(value: Int) {
        if (vibrationType) vibrator?.vibrate(value.toLong())
    }

    private fun stageComplete() {
        soundFromResId(R.raw.change_level)
        for (i in listObjects.indices) {
            listObjects[i]?.clearAnimation()
        }
        if (glassView != null) removeView(glassView)
        glassView = null
        //GameFrame=newImageView(R.drawable.frame);
        //GameFrame.setVisibility(View.INVISIBLE);
        gameButtonMoon = newImageView(R.drawable.prev)
        gameButtonFlower = newImageView(R.drawable.refresh)
        gameButtonSunshine = newImageView(R.drawable.next)
        val height = (Owner.viewHeight * 0.20f).toInt()
        val left: Int = Owner.viewWidth / 4 - height / 2
        val top = (Owner.viewHeight * 0.8f).toInt()
        val center: Int = Owner.viewWidth / 2 - height / 2
        val right: Int = Owner.viewWidth * 3 / 4 - height / 2

        gameButtonMoon?.let {
            setBounds(
                it,
                left.toFloat(),
                top.toFloat(),
                (left + height).toFloat(),
                (top + height).toFloat()
            )
        }
        gameButtonFlower?.let {
            setBounds(
                it,
                center.toFloat(),
                top.toFloat(),
                (center + height).toFloat(),
                (top + height).toFloat()
            )
        }
        gameButtonSunshine?.let {
            setBounds(
                it,
                right.toFloat(),
                top.toFloat(),
                (right + height).toFloat(),
                (top + height).toFloat()
            )
        }

        run {
            val animation: Animation =
                TranslateAnimation(0f, 0f, Owner.viewHeight * 0.2f, -height * 0.5f)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    val animation2 = TranslateAnimation(0f, 0f, -height * 0.5f, 0f)
                    animation2.setDuration(200)
                    gameButtonMoon?.startAnimation(animation2)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            animation.setDuration(500)
            gameButtonMoon?.startAnimation(animation)
        }
        run {
            val animation: Animation =
                TranslateAnimation(0f, 0f, Owner.viewHeight * 0.2f, -height * 0.5f)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val animation2 = TranslateAnimation(0f, 0f, -height * 0.5f, 0f)
                    animation2.setDuration(200)
                    gameButtonFlower?.startAnimation(animation2)
                }
            })
            animation.setDuration(500)
            animation.startOffset = 200
            gameButtonFlower?.startAnimation(animation)
        }
        run {
            val animation: Animation =
                TranslateAnimation(0f, 0f, Owner.viewHeight * 0.2f, -height * 0.5f)
            animation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    val animation2: Animation = TranslateAnimation(0f, 0f, -height * 0.5f, 0f)
                    animation2.setAnimationListener(object : AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}
                        override fun onAnimationRepeat(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            //if (BlinkType)
                            run {
                                //setBlinkView(GameButtonMoon);
                                //setBlinkView(GameButtonFlower);
                                //setBlinkView(GameButtonSunshine);
                                run {
                                    val animation3 =
                                        TranslateAnimation(0f, (-height / 3).toFloat(), 0f, 0f)
                                    animation3.setDuration(1000)
                                    animation3.repeatMode = Animation.REVERSE
                                    animation3.setRepeatCount(Animation.INFINITE)
                                    gameButtonMoon?.startAnimation(animation3)
                                }
                                run {
                                    val animation3: Animation =
                                        RotateAnimation(-5f, 5f, height / 2f, height / 2f)
                                    animation3.setDuration(1000)
                                    animation3.repeatMode = Animation.REVERSE
                                    animation3.setRepeatCount(Animation.INFINITE)
                                    gameButtonFlower?.startAnimation(animation3)
                                }
                                run {
                                    val animation3: Animation =
                                        TranslateAnimation(0f, (height / 3).toFloat(), 0f, 0f)
                                    animation3.setDuration(1000)
                                    animation3.repeatMode = Animation.REVERSE
                                    animation3.setRepeatCount(Animation.INFINITE)
                                    gameButtonSunshine?.startAnimation(animation3)
                                }
                            }
                        }
                    })
                    animation2.setDuration(200)
                    gameButtonSunshine?.startAnimation(animation2)
                }
            })
            animation.setDuration(500)
            animation.startOffset = 400
            gameButtonSunshine?.startAnimation(animation)
        }
    }

    private fun setBlinkView(v: View) {
        val anim: Animation = AlphaAnimation(0.5f, 1f)
        anim.setDuration(1000)

        //anim.setStartOffset(500);
        anim.setRepeatMode(Animation.REVERSE)
        anim.setRepeatCount(Animation.INFINITE)
        v.startAnimation(anim)
    }

    private fun hideAnimation(v: View?) {
        if (v == null) return
        val anim: Animation = AlphaAnimation(1f, 0f)
        anim.setDuration(1000)

        //anim.setStartOffset(500);
        //anim.setRepeatMode(Animation.REVERSE);
        //anim.setRepeatCount(Animation.INFINITE);
        anim.setFillAfter(true)
        v.startAnimation(anim)
    }

    fun newImageView(resId: Int): ImageView {
        val image = ImageView(Owner)
        image.setImageResource(resId)
        this.addView(image)
        return image
    }

    fun newImageView(bmp: Bitmap?): ImageView {
        val image = ImageView(Owner)
        image.setImageBitmap(bmp)
        this.addView(image)
        return image
    }

    //@SuppressLint("NewApi")
    fun newShadowImageView(bmpName: String): ImageView {
        val image = ImageView(Owner)
        getImageFromObb(bmpName)?.apply {
            val bmp = makeShadowBitmap(this)
            image.setImageBitmap(bmp)
        }
        this.addView(image)
        return image
    }

    fun newShadowImageView2(bmpName: String?): ShadowImageView {
        val image = ShadowImageView(context)
        getImageFromObb(bmpName)?.apply {
            val bmp = makeShadowBitmap(this)
            image.setShadowBitmap(bmp, Color.BLACK)
        }
        this.addView(image)
        return image
    }

    /*fun newShadowImageView(resId: Int): ImageView {
        val image = ImageView(context)
        val bmp = MakeShadowBitmap(BitmapFactory.decodeResource(getResources(), resId))
        image.setImageBitmap(bmp)
        setAlpha(image, 0.5f)
        this.addView(image)
        return image
    }


    //@SuppressLint("NewApi")
    fun newShadow2ImageView(bmpName: String?): ImageView {
        val image = ImageView(context)
        val bmp = getImageFromObb(bmpName, ".png")
        image.setImageBitmap(bmp)
        setAlpha(image, 0.7f)
        this.addView(image)
        return image
    }

    fun newWhiteImageView(resId: Int): ImageView {
        val image = ImageView(context)
        val bmp = MakeWhiteBitmap(BitmapFactory.decodeResource(getResources(), resId))
        image.setImageBitmap(bmp)
        this.addView(image)
        return image
    }*/

    fun newLevelIcon(resId: Int, width: Float, height: Float, excludeRect: Rect): ImageView {
        val image = ImageView(context)
        val bmp = makeLevelIcon(resId, width, height, excludeRect)
        image.setImageBitmap(bmp)
        this.addView(image)
        return image
    }

    fun setBounds(image: View, left: Float, top: Float, right: Float, bottom: Float) {
        val lp = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        lp.leftMargin = left.toInt()
        lp.topMargin = top.toInt()
        lp.width = (right - left).toInt()
        lp.height = (bottom - top).toInt()
        if (right >= Owner.viewWidth) lp.rightMargin = (Owner.viewWidth - right - 1).toInt()
        if (bottom >= Owner.viewHeight) lp.bottomMargin =
            (Owner.viewHeight - bottom - 1).toInt()
        image.setLayoutParams(lp)
    }

    fun setBounds(image: View, rectF: RectF) {
        setBounds(image, rectF.left, rectF.top, rectF.right, rectF.bottom)
    }

    var babochkaImageView: ImageView? = null
    var shtoraImageView: ImageView? = null
    private fun initMainMenu() {
        this.setBackgroundResource(R.drawable.start_fone2)
        val cvetyImul: Float = Owner.viewHeight / 720f
        val starImul: Float = Owner.viewWidth / 1024f
        val starSize = 450f * cvetyImul
        startStar1Image = newImageView(R.drawable.menu_star2)
        setBounds(
            startStar1Image!!,
            867 * starImul - starSize / 2,
            126 * cvetyImul - starSize / 2,
            867 * starImul + starSize / 2,
            126 * cvetyImul + starSize / 2
        )
        run {
            val animation: Animation = AlphaAnimation(0.2f, 1f)
            animation.setDuration(1000)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startStar1Image!!.startAnimation(animation)
        }
        startStar2Image = newImageView(R.drawable.menu_star2)
        setBounds(
            startStar2Image!!,
            128 * starImul - starSize / 2,
            90 * cvetyImul - starSize / 2,
            128 * starImul + starSize / 2,
            90 * cvetyImul + starSize / 2
        )
        run {
            val animation: Animation = AlphaAnimation(0.2f, 1f)
            animation.setDuration(500)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startStar2Image!!.startAnimation(animation)
        }
        startStar3Image = newImageView(R.drawable.menu_star2)
        setBounds(
            startStar3Image!!,
            669 * starImul - starSize / 2,
            276 * cvetyImul - starSize / 2,
            669 * starImul + starSize / 2,
            276 * cvetyImul + starSize / 2
        )
        run {
            val animation: Animation = AlphaAnimation(0.2f, 1f)
            animation.setDuration(1200)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startStar3Image!!.startAnimation(animation)
        }
        startStar4Image = newImageView(R.drawable.menu_star)
        setBounds(
            startStar4Image!!,
            360 * starImul - starSize / 8,
            127 * cvetyImul - starSize / 8,
            360 * starImul + starSize / 8,
            127 * cvetyImul + starSize / 8
        )
        run {
            val animation: Animation = AlphaAnimation(0.2f, 1f)
            animation.setDuration(300)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startStar4Image!!.startAnimation(animation)
        }
        startStar5Image = newImageView(R.drawable.menu_star)
        setBounds(
            startStar5Image!!,
            818 * starImul - starSize / 8,
            309 * cvetyImul - starSize / 8,
            818 * starImul + starSize / 8,
            309 * cvetyImul + starSize / 8
        )
        run {
            val animation: Animation = AlphaAnimation(0.2f, 1f)
            animation.setDuration(400)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startStar5Image!!.startAnimation(animation)
        }
        startGameButton = ImageView(Owner)
        startGameButton!!.setBackgroundResource(R.drawable.start_button_anim)
        addView(startGameButton)
        val startAnimation = startGameButton!!.getBackground() as AnimationDrawable
        startAnimation.start()
        //LevelGameButton=newImageView(R.drawable.level);
        startListiyaImage = newImageView(R.drawable.start_listiya)
        setBounds(
            startListiyaImage!!,
            -10 * cvetyImul,
            Owner.viewHeight - 507 * cvetyImul + 10 * cvetyImul,
            556 * cvetyImul - 10 * cvetyImul,
            Owner.viewHeight + 10 * cvetyImul
        )
        run {
            val animation: Animation = RotateAnimation(0f, 7f, 0f, 507 * cvetyImul)
            animation.setDuration(3000)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startListiyaImage!!.startAnimation(animation)
        }
        startCvetyImage = newImageView(R.drawable.start_cvety2)
        setBounds(
            startCvetyImage!!,
            +10 * cvetyImul,
            Owner.viewHeight - 666 * cvetyImul + 10 * cvetyImul,
            491 * cvetyImul + 10 * cvetyImul,
            Owner.viewHeight + 10 * cvetyImul
        )
        run {
            val animation: Animation = RotateAnimation(0f, 5f, 0f, 666 * cvetyImul)
            animation.setDuration(2000)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startCvetyImage!!.startAnimation(animation)
        }
        startListikImage = newImageView(R.drawable.start_listik)
        setBounds(
            startListikImage!!,
            -10 * cvetyImul,
            Owner.viewHeight - 324 * cvetyImul + 10 * cvetyImul,
            218 * cvetyImul - 10 * cvetyImul,
            Owner.viewHeight + 10 * cvetyImul
        )
        run {
            val animation: Animation = RotateAnimation(0f, 3f, 0f, 324 * cvetyImul)
            animation.setDuration(2500)
            animation.setRepeatMode(Animation.REVERSE)
            animation.setRepeatCount(Animation.INFINITE)
            startListikImage!!.startAnimation(animation)
        }
        settingsButton = ImageView(Owner)
        settingsButton!!.setBackgroundResource(R.drawable.animaciya_setting)
        addView(settingsButton)
        val settingsAnimation = settingsButton!!.getBackground() as AnimationDrawable
        settingsAnimation.start()
        //SettingsButton=newImageView(R.drawable.settings);
        exitGameButton = ImageView(Owner)
        exitGameButton!!.setBackgroundResource(R.drawable.exit)
        addView(exitGameButton)
        val exitAnimation = exitGameButton!!.getBackground() as AnimationDrawable
        exitAnimation.start()
        //ExitGameButton=newImageView(R.drawable.exit);
        val center_h: Int =
            (Owner.viewHeight /*-ExitGameButton.getDrawable().getIntrinsicHeight()*/) / 2
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            getResources().getDisplayMetrics()
        )
        val empty_height: Int =
            Owner.viewHeight - ( /*MainMenuCaption.getHeight()+*/exitGameButton!!.getHeight())
        var imul = 1f
        val startButtonWidth = 540 * 1.25f
        val startButtonHeight = 458 * 1.25f
        if (empty_height < startButtonHeight) {
            imul = empty_height.toFloat() / (startButtonHeight)
        }
        //setBounds(MainMenuCaption,(int)((Owner.getViewWidth()-MainMenuCaption.getDrawable().getIntrinsicWidth()*imul)/2), 0, (int)(Owner.getViewWidth()+MainMenuCaption.getDrawable().getIntrinsicWidth()*imul)/2,  MainMenuCaption.getDrawable().getIntrinsicHeight()*imul);
        setBounds(
            startGameButton!!,
            ((Owner.viewWidth - startButtonWidth * imul) / 2).toInt().toFloat(),
            (center_h - startButtonHeight * imul / 2).toInt().toFloat(),
            ((Owner.viewWidth + startButtonWidth * imul).toInt() / 2).toFloat(),
            center_h + startButtonHeight * imul / 2
        )
        //setBounds(LevelGameButton,(int)((Owner.getViewWidth()-LevelGameButton.getDrawable().getIntrinsicWidth()*imul)/2), center_h, (int)(Owner.getViewWidth()+LevelGameButton.getDrawable().getIntrinsicWidth()*imul)/2,  (int)(center_h+LevelGameButton.getDrawable().getIntrinsicHeight()*imul));
        //int botton_buttons_width=/*ExitGameButton.getDrawable().getIntrinsicWidth()+*/SettingsButton.getDrawable().getIntrinsicWidth()+(int)px;
        setBounds(settingsButton!!, 0f, 0f, 187 * cvetyImul, 130 * cvetyImul)
        setBounds(
            exitGameButton!!,
            Owner.viewWidth - 118 * cvetyImul,
            0f,
            Owner.viewWidth.toFloat(),
            130 * cvetyImul
        )
        babochkaImageView = ImageView(Owner)
        babochkaImageView!!.setBackgroundResource(R.drawable.animaciya_babochka)
        addView(babochkaImageView)
        val babochkaAnimation = babochkaImageView!!.getBackground() as AnimationDrawable
        babochkaAnimation.start()
        setBounds(
            babochkaImageView!!,
            823 * starImul - 102 * cvetyImul,
            560 * cvetyImul - 64 * cvetyImul,
            823 * starImul + 102 * cvetyImul,
            560 * cvetyImul + 64 * cvetyImul
        )
        babochkaImageView!!.setRotation(37f)

        run {
            //if(shtoraImageView!=null)
            //	((ViewGroup)shtoraImageView.getParent()).removeView(shtoraImageView);
            shtoraImageView = null
            shtoraImageView = ImageView(Owner)
            //Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.animaciya_shtora11);
            shtoraImageView!!.setBackgroundResource(R.drawable.animaciya_shtora1)
            addView(shtoraImageView)
            val animationDrawable = shtoraImageView!!.getBackground() as AnimationDrawable
            /*animationDrawable.setAnimationFinishListener(new AnimationDrawable2.IAnimationFinishListener() {
					@Override
					public void onAnimationFinished() {
						removeView(shtoraImageView);
					}
				});*/
            animationDrawable.start()
            setBounds(
                shtoraImageView!!,
                0f,
                0f,
                Owner.viewWidth.toFloat(),
                Owner.viewHeight.toFloat()
            )
            var totalDuration: Long = 0
            for (i in 0..<animationDrawable.getNumberOfFrames()) {
                totalDuration += animationDrawable.getDuration(i).toLong()
            }
            val timer = Timer()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    shtoraImageView!!.post(object : Runnable {
                        override fun run() {
                            shtoraImageView!!.setBackgroundColor(Color.TRANSPARENT)
                        }
                    })
                }
            }
            timer.schedule(timerTask, totalDuration)
        }
    }

    private fun ClearMainMenu() {
        //if(Owner.buyButton!=null)
        //	Owner.buyButton.setVisibility(View.INVISIBLE);
        //if(Owner.btnSoobrazhalka2!=null)
        //	Owner.btnSoobrazhalka2.setVisibility(View.INVISIBLE);
        //if(Owner.btnMozaik!=null)
        //	Owner.btnMozaik.setVisibility(View.INVISIBLE);
        //if(Owner.btnAzbuka!=null)
        //    Owner.btnAzbuka.setVisibility(View.INVISIBLE);
        //RecycleBitmap(MainMenuCaption);
        recycleBitmap(startGameButton)
        //RecycleBitmap(LevelGameButton);
        recycleBitmap(settingsButton)
        recycleBitmap(exitGameButton)
        recycleBitmap(startCvetyImage)
        recycleBitmap(startListikImage)
        recycleBitmap(startListiyaImage)
        recycleBitmap(startStar1Image)
        recycleBitmap(startStar2Image)
        recycleBitmap(startStar3Image)
        recycleBitmap(startStar4Image)
        recycleBitmap(startStar5Image)
        recycleBitmap(babochkaImageView)
        recycleBitmap(shtoraImageView)
    }

    private fun InitSettings() {
        ClearMainMenu()
        symbolsTypeSettings = ImageView(Owner)
        symbolsTypeSettings!!.setScaleType(ImageView.ScaleType.FIT_CENTER)
        this.addView(symbolsTypeSettings)
        vibrationSettings = ImageView(Owner)
        symbolsTypeSettings!!.setScaleType(ImageView.ScaleType.FIT_CENTER)
        this.addView(vibrationSettings)
        starsSettings = ImageView(Owner)
        starsSettings!!.setScaleType(ImageView.ScaleType.FIT_CENTER)
        this.addView(starsSettings)
        animateBackgroundSettings = ImageView(Owner)
        animateBackgroundSettings!!.setScaleType(ImageView.ScaleType.FIT_CENTER)
        this.addView(animateBackgroundSettings)
        animateBackgroundSettings2 = ImageView(Owner)
        animateBackgroundSettings2!!.setScaleType(ImageView.ScaleType.FIT_CENTER)
        this.addView(animateBackgroundSettings2)

        //int px=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        val iMul: Float = Owner.viewWidth / 1200f
        setBounds(
            vibrationSettings!!,
            0f,
            (Owner.viewHeight / 2 - iMul * 259f) / 2f,
            Owner.viewWidth / 2f,
            (Owner.viewHeight / 2 + iMul * 259f) / 2f
        )
        setBounds(
            symbolsTypeSettings!!,
            Owner.viewWidth / 2f,
            (Owner.viewHeight / 2 - iMul * 259f) / 2f,
            Owner.viewWidth.toFloat(),
            (Owner.viewHeight / 2 + iMul * 259f) / 2f
        )
        setBounds(
            starsSettings!!,
            0f,
            (Owner.viewHeight * 3 / 2 - iMul * 259f) / 2f,
            Owner.viewWidth / 2f,
            (Owner.viewHeight * 3 / 2 + iMul * 259f) / 2f
        )
        setBounds(
            animateBackgroundSettings!!,
            Owner.viewWidth / 2f,
            (Owner.viewHeight * 3 / 2 - iMul * 259f) / 2f,
            Owner.viewWidth.toFloat(),
            (Owner.viewHeight * 3 / 2 + iMul * 259f) / 2f
        )
        setBounds(
            animateBackgroundSettings2!!,
            Owner.viewWidth / 2f + (Owner.viewWidth / 2f - iMul * 204f) / 2,
            (Owner.viewHeight * 3 / 2 - iMul * 158f) / 2f,
            Owner.viewWidth / 2f + (Owner.viewWidth / 2f + iMul * 204f) / 2,
            (Owner.viewHeight * 3 / 2 + iMul * 158f) / 2f
        )
        animateBackgroundSettings?.setImageResource(R.drawable.animaciya_stars00)
        vibrationType = vibrationType
        setSymbolsType(SymbolsType)
        isStars = isStars
        isAnimateBackground = isAnimateBackground
        run {
            shtoraImageView = ImageView(Owner)
            shtoraImageView!!.setBackgroundResource(R.drawable.animaciya_shtora2)
            addView(shtoraImageView)
            val animationDrawable = shtoraImageView?.background as? AnimationDrawable
            animationDrawable?.start()
            setBounds(
                shtoraImageView!!,
                0f,
                0f,
                Owner.viewWidth.toFloat(),
                Owner.viewHeight.toFloat()
            )
            var totalDuration: Long = 0
            for (i in 0..<animationDrawable?.numberOfFrames.orZero()) {
                totalDuration += animationDrawable?.getDuration(i).orZero().toLong()
            }
            val timer = Timer()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    shtoraImageView?.post {
                        shtoraImageView?.setBackgroundResource(R.drawable.animaciya_shtora1)
                        val animationDrawable = shtoraImageView?.background as? AnimationDrawable
                        animationDrawable?.start()
                        var totalDuration: Long = 0
                        for (i in 0..<animationDrawable?.numberOfFrames.orZero()) {
                            totalDuration += animationDrawable?.getDuration(i).orZero().toLong()
                        }
                        val timer = Timer()
                        val timerTask: TimerTask = object : TimerTask() {
                            override fun run() {
                                shtoraImageView?.post { shtoraImageView?.setBackgroundColor(Color.TRANSPARENT) }
                            }
                        }
                        timer.schedule(timerTask, totalDuration)
                    }
                }
            }
            timer.schedule(timerTask, totalDuration)
        }
    }

    /*private fun setStars(type: Boolean) {
        isStars = type
        starsSettings!!.setImageResource(R.drawable.animaciya_stars00)
        if (type) {
            starsSettings!!.setBackgroundResource(R.drawable.animaciya_stars)
            val animation = starsSettings!!.getBackground() as AnimationDrawable
            animation.start()
        } else starsSettings!!.setBackgroundColor(Color.TRANSPARENT)
    }*/

    /*private fun setAnimateBackground(type: Boolean) {
        isAnimateBackground = type
        if (type) {
            animateBackgroundSettings2!!.setBackgroundResource(R.drawable.animaciya_babochka_1)
            val animation = animateBackgroundSettings2!!.getBackground() as AnimationDrawable
            animation.start()
        } else animateBackgroundSettings2!!.setBackgroundColor(Color.TRANSPARENT)
    }*/

    /*private fun setVibrationType(type: Boolean) {
        vibrationType = type
        vibrationSettings!!.setImageResource(R.drawable.no_vibration)
        if (type) {
            vibrationSettings!!.setBackgroundResource(R.drawable.animaciya_vibraciya)
            val animation = vibrationSettings!!.getBackground() as AnimationDrawable
            animation.start()
        } else vibrationSettings!!.setBackgroundColor(Color.TRANSPARENT)
    }*/

    private fun setSymbolsType(type: Int) {
        SymbolsType = type
        if (type == 0) symbolsTypeSettings!!.setBackgroundResource(R.drawable.settings_zvuki)
        else symbolsTypeSettings!!.setBackgroundResource(R.drawable.settings_bukvy)
        //setAlpha(BlinkSettingsShadow, type ? 0f : 1f);
    }

    fun setCurrentScreenType(type: Int) {
        currentScreenType = type
    }

    fun smallClick() {
        vibrate(50)
    }

    fun exit() {
        currentScreenType = 1000
        Owner.startNextStage(0)
    }

    fun playItemName(num: Int) {
        if (levels[currentLevelNum].items[num].soundResource != null) {
            if (levels[currentLevelNum].items[num].isBukva) {
                if (SymbolsType == 1) soundFromResId(levels[currentLevelNum].items[num].soundResource?.get(1).orZero())
                else soundFromResId(levels[currentLevelNum].items[num].soundResource?.get(0).orZero())
            } else soundFromResId(levels[currentLevelNum].items[num].soundResource ?: arrayOf())
        } else soundFromResId(R.raw.start_game)
    }

    fun checkCompleteLevel(num: Int) {
        var allClear = true
        for (i in listObjects.indices) {
            if (listObjects[i]?.isSetOnPosition != true) {
                allClear = false
            }
        }
        run {
            if (levels[currentLevelNum].items[num].soundResource != null) {
                if (levels[currentLevelNum].items[num].isBukva) {
                    if (SymbolsType == 1) soundFromResId(levels[currentLevelNum].items[num].soundResource?.getOrNull(1).orZero())
                    else soundFromResId(levels[currentLevelNum].items[num].soundResource?.getOrNull(0).orZero())
                } else soundFromResId(levels[currentLevelNum].items[num].soundResource ?: arrayOf())
            } else {
                val resNum = mRnd.nextInt(goodStep.size)
                soundFromResId(goodStep[resNum])
            }
            if (allClear) {
                for (i in listObjects.indices) {
                    listObjects[i]?.apply {
                        if (isVisible) setBlinkView(this)
                    }
                }
                val handlerTimer = Handler()
                handlerTimer.postDelayed(object : Runnable {
                    override fun run() {
                        stageComplete()
                        currentScreenType = 2
                    }
                }, 3000)
                hideAnimation(glassView)
            } else {
                children
                    .filter { it.javaClass.simpleName == "MovingImageView" }
                    .forEach {
                        (it as? MovingImageView)?.let { miv ->
                            if (!miv.isSetOnPosition) {
                                miv.bringToFront()
                            }
                    }
                }
            }
        }
    }

    @SuppressLint("UseKtx")
    fun checkCompleteLevelWithGoodSound(num: Int) {
        var allClear = true
        for (i in listObjects.indices) {
            if (listObjects[i]?.isSetOnPosition != true) {
                allClear = false
            }
        }
        run {
            run {
                val resNum = mRnd.nextInt(goodStep.size)
                soundFromResId(goodStep[resNum])
            }
            if (allClear) {
                for (i in listObjects.indices) {
                    listObjects[i]?.apply {
                        if (isVisible)
                            setBlinkView(this)
                    }
                }
                val handlerTimer = Handler()
                handlerTimer.postDelayed({
                    stageComplete()
                    currentScreenType = 2
                }, 3000)
                hideAnimation(glassView)
            } else {
                children.forEach {
                    (it as? MovingImageView)?.let { miv ->
                        if (miv.isSetOnPosition) {
                            removeView(miv)
                            addView(miv)
                        }
                    }
                }
            }
        }
    }

    fun getImageFromObb(pathToFileInsideZip: String?): Bitmap? {
        try {
            val resId = resources.getIdentifier(
                pathToFileInsideZip,
                "drawable",
                Owner.packageName
            )
            if (resId > 0) {
                if (isScaledBitmaps) {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = false
                    options.inSampleSize = 2
                    return BitmapFactory.decodeResource(Owner.resources, resId, options)
                }
                return BitmapFactory.decodeResource(Owner.resources, resId)
            }
        } catch (e: IOException) {
            Log.v("Load from zip error: ", e.message!!)
        }
        return null
    }

    fun soundFromResId(resId: Int) {
        if (resId > 0) {
            try {
                stop()

                if (isSound) {
                    mp = MediaPlayer.create(Owner, resId)
                    mp?.start()
                }
            } catch (e: IOException) {
                Log.v("Load from zip error: ", e.message!!)
            }
        }
    }

    fun soundFromResId(resIds: Array<Int>, item: Int = 0) {
        try {
            stop()
            if (!isSound) return

            val resId = resIds[item]
            if (resId > 0) {
                mp = MediaPlayer.create(Owner, resId)
                mp?.setOnCompletionListener {
                    if (item + 1 < resIds.size)
                        soundFromResId(resIds, item + 1)
                }
                mp?.start()
            }
        } catch (e: IOException) {
            Log.v("Load from zip error: ", e.message!!)
        }
    }

    fun stop() {
        try {
            mp?.apply {
                stop()
                release()
            }
            mp = null
        } catch (ex: Exception) {
        }
    }

    /*Bitmap savedScreenBitmap=null;
	public Bitmap saveCurrentScreen()
	{
		if(savedScreenBitmap==null)
		{
			savedScreenBitmap=Bitmap.createBitmap(getWidth(),getHeight(),Config.ARGB_8888);
		}
		Canvas canvas=new Canvas(savedScreenBitmap);
		for()
	}*/
    fun isHasMovingObjectAtPoint(point: Point): Boolean {
        for (i in listObjects!!.indices) {
            if (listObjects[i]!!.isSetOnPosition == false) {
                val layoutParams = listObjects[i]!!.getLayoutParams() as LayoutParams
                if (listObjects[i]!!.isVisiblePoint(
                        point.x - layoutParams.leftMargin,
                        point.y - layoutParams.topMargin
                    )
                ) {
                    return true
                }
            }
        }
        return false
    }

    fun returnToMainMenu() {
        run {
            shtoraImageView = ImageView(Owner)
            shtoraImageView?.setBackgroundResource(R.drawable.animaciya_shtora2)
            addView(shtoraImageView)
            val animationDrawable = shtoraImageView?.background as? AnimationDrawable
            animationDrawable?.start()
            shtoraImageView?.let {
                setBounds(
                    it,
                    0f,
                    0f,
                    Owner.viewWidth.toFloat(),
                    Owner.viewHeight.toFloat()
                )
            }
            var totalDuration: Long = 0
            for (i in 0..<animationDrawable?.numberOfFrames.orZero()) {
                totalDuration += animationDrawable?.getDuration(i).orZero().toLong()
            }
            val timer = Timer()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    shtoraImageView?.post {
                        clearSettings()
                        clearLevel()
                        clearSelectLevels()
                        initMainMenu()
                    }
                }
            }
            timer.schedule(timerTask, totalDuration)
        }
    }

    init {
        mGameView = this
        try {
            mRnd.setSeed(System.currentTimeMillis())
            Owner = context as GameActivity
        } catch (ex: Exception) {
            //Log.d("MY_TEST_LOG", ex.getMessage());
        }
    }

    companion object {
        lateinit var mGameView: GameView
        lateinit var Owner: GameActivity
        var currentScreenType: Int = 100
        var currentLevelNum: Int = 0

        var start_time: Long = 0
        var max_alphavit_num: Int = 32
        var topPosition: Int = 0
        var selectLevelPart: Int = 0
        var mRnd: Random = Random()
        var SymbolsType: Int = 0
        fun getRandomIndexes(number: Int): IntArray {
            val result = IntArray(number)

            for (i in 0..<number) result[i] = -1

            for (i in 0..<number) {
                val index: Int = mRnd.nextInt(number - i)
                var realIndex = 0
                for (j in 0..<number) {
                    if (result[j] == -1) {
                        if (realIndex == index) {
                            result[j] = i
                            break
                        }
                        realIndex++
                    }
                }
            }
            return result
        }

        val isScaledBitmaps: Boolean
            get() = Owner.viewHeight <= 480
    }
}
