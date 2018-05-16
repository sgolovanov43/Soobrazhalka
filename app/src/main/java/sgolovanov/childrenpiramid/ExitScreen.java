package sgolovanov.childrenpiramid;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Sergey on 25.10.2015.
 */
public class ExitScreen extends GameLayout {
    public ExitScreen(Context context) {
        super(context);
    }

    ImageView exitButton=null;
    ImageView backButton=null;

    @Override
    void LoadLevel() {
        //Owner.btnBack.setVisibility(INVISIBLE);
        //Owner.btnSelectLevel.setVisibility(INVISIBLE);
        setBackgroundResource(R.drawable.sad_panda);

        exitButton=new ImageView(Owner);
        exitButton.setImageResource(R.drawable.exit_button);
        setBounds(exitButton,
                Owner.getViewWidth() / 2,
                Owner.getViewHeight() - 150 * bmp_factor,
                Owner.getViewWidth(),
                Owner.getViewHeight()
        );
        exitButton.setScaleType(ImageView.ScaleType.FIT_START);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Owner.Stop();
                int[] exitTracks={R.raw.ya_budu_skuchat,R.raw.vozvraschajsja_skoree};
                Owner.mp = MediaPlayer.create(Owner, exitTracks[mRnd.nextInt(exitTracks.length)]);
                Owner.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                Owner.mp.start();
            }
        });
        addView(exitButton);

        backButton=new ImageView(Owner);
        backButton.setImageResource(R.drawable.return_to_game_button);
        setBounds(backButton,
                0,
                Owner.getViewHeight() - 150 * bmp_factor,
                Owner.getViewWidth() / 2,
                Owner.getViewHeight()
        );
        backButton.setScaleType(ImageView.ScaleType.FIT_END);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Owner.btnBack.setVisibility(VISIBLE);
                //Owner.btnSelectLevel.setVisibility(VISIBLE);
                Owner.MyGameView.SetCurrentScreenType(100);
                Owner.returnToMenu(true);
            }
        });
        addView(backButton);

        //if (Owner.mInterstitialAd.isLoaded()) {
        //    Owner.mInterstitialAd.show();
        //}
    }

}
