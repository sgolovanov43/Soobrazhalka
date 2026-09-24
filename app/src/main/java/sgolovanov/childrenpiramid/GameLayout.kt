package sgolovanov.childrenpiramid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by Сергей Голованов on 03.08.2015.
 */
public abstract class GameLayout extends RelativeLayout {
    protected GameActivity Owner;
    //protected  AudioManager audio;
    protected Random mRnd = new Random();
    protected boolean VibrationType = true;
    protected boolean BlinkType = true;
    //protected  Vibrator vibrator;
    float Left = 0;
    float Top = 0;
    float screen_factor = 1f;
    float bmp_factor = 1f;
    boolean isComplete;

    public GameLayout(Context context) {
        super(context);
        Owner = (GameActivity) context;
        isComplete = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Owner.Stop();
    }

    public void setBounds(View image, float left, float top, float right, float bottom) {
        /*image.setLeft((int)left);
        image.setTop((int)top);
        image.setRight((int)right);
        image.setBottom((int)bottom);
        */
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = (int) left;
        lp.topMargin = (int) top;
        lp.width = (int) (right - left);
        lp.height = (int) (bottom - top);
        image.setLayoutParams(lp);
    }

    public void StartView() {
        try {
            //audio = (AudioManager) Owner.getSystemService(Context.AUDIO_SERVICE);
            //vibrator = (Vibrator)Owner.getSystemService(Context.VIBRATOR_SERVICE);
            StartLevel();
        } catch (Exception ex) {
            Log.d("MY_TEST_LOG", ex.getMessage());
        }
    }

    void StartLevel() {
        this.post(new Runnable() {
            @Override
            public void run() {
                //isStartingLevel = true;
                //ClearLevel();
                float py = (float) Owner.getViewHeight() / 620.0f;//(float) ListBackground.getDrawable().getIntrinsicHeight();
                float px = (float) Owner.getViewWidth() / 1200f;//(float) ListBackground.getDrawable().getIntrinsicWidth();
                if (px < py)
                    screen_factor = px;
                else
                    screen_factor = py;
                //float screen_factor=px;//((float)getWidth()/(float)getHeight())/(640f/310f);
                bmp_factor = screen_factor;//620f/ListBackground.getHeight();///(620f/screen_factor);
                //float grid_scale = (ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
                Left = (Owner.getViewWidth() - bmp_factor * 1200f) / 2f;
                Top = (Owner.getViewHeight() - bmp_factor * 620f) / 2f;

                LoadLevel();
            }
        });
    }

    abstract void LoadLevel();

    public void Play(int resource, boolean is_looping) {
        Owner.Play(resource);
    }

}