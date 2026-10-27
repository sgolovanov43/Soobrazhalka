package sgolovanov.childrenpiramid

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Process
import android.widget.ImageView

/**
 * Created by Sergey on 25.10.2015.
 */
class ExitScreen(context: Context?) : GameLayout(context) {
    var exitButton: ImageView? = null
    var backButton: ImageView? = null

    override fun LoadLevel() {
        //Owner.btnBack.setVisibility(INVISIBLE);
        //Owner.btnSelectLevel.setVisibility(INVISIBLE);
        setBackgroundResource(R.drawable.sad_panda)

        ImageView(Owner).let { exitButton ->
            this.exitButton = exitButton
            exitButton.setImageResource(R.drawable.exit_button)
            setBounds(
                exitButton,
                (Owner.viewWidth / 2).toFloat(),
                Owner.viewHeight - 150 * bmp_factor,
                Owner.viewWidth.toFloat(),
                Owner.viewHeight.toFloat()
            )
            exitButton.setScaleType(ImageView.ScaleType.FIT_START)
            exitButton.setOnClickListener { //Owner.Stop();
                val exitTracks = intArrayOf(R.raw.ya_budu_skuchat, R.raw.vozvraschajsja_skoree)
                Owner.mp = MediaPlayer.create(Owner, exitTracks[mRnd.nextInt(exitTracks.size)])
                Owner.mp?.setOnCompletionListener(object : OnCompletionListener {
                    override fun onCompletion(mp: MediaPlayer?) {
                        Process.killProcess(Process.myPid())
                    }
                })
                Owner.mp?.start()
            }
            addView(exitButton)
        }

        ImageView(Owner).let { backButton ->
            this.backButton = backButton
            backButton.setImageResource(R.drawable.return_to_game_button)
            setBounds(
                backButton,
                0f,
                Owner.viewHeight - 150 * bmp_factor,
                (Owner.viewWidth / 2).toFloat(),
                Owner.viewHeight.toFloat()
            )
            backButton.setScaleType(ImageView.ScaleType.FIT_END)
            backButton.setOnClickListener { //Owner.btnBack.setVisibility(VISIBLE);
                //Owner.btnSelectLevel.setVisibility(VISIBLE);
                Owner.myGameView().setCurrentScreenType(100)
                Owner.returnToMenu(true)
            }
            addView(backButton)
        }

        //if (Owner.mInterstitialAd.isLoaded()) {
        //    Owner.mInterstitialAd.show();
        //}
    }
}
