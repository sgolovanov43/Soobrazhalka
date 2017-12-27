package sgolovanov.childrenpiramid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

/*import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
*/
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity {
    public GameView MyGameView=null;
    static String SHARED_PREFS_NAME="SETTINGS";
	PowerManager pm=null;
    PowerManager.WakeLock wl=null;
    //private AdView adView=null;
    //ImageButton buyButton;
    boolean isPurchased=false;
	//public int adsHeight=0;
	MediaPlayer mp;
	public GameLayout gameLayout=null;
	InterstitialAd mInterstitialAd;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.FullscreenTheme);
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this);
        getSystemService(Context.VIBRATOR_SERVICE);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK , "My Tag"); 
        wl.acquire();

        try {
			setContentView(R.layout.activity_game);

			MyGameView = (GameView) findViewById(R.id.game_view);
			MyGameView.Owner = this;

			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			SharedPreferences GameSettings=getSharedPreferences(GameActivity.SHARED_PREFS_NAME, 0);
			isPurchased=GameSettings.getBoolean(getString(R.string.purchased_key), false);

			if(isPurchased==false){
				MobileAds.initialize(this, "ca-app-pub-4919341775273582~5315452852");
				AdView mAdView = findViewById(R.id.adView);
				AdRequest adRequest = new AdRequest.Builder().build();
				mAdView.loadAd(adRequest);

				mInterstitialAd = new InterstitialAd(this);
				mInterstitialAd.setAdUnitId("ca-app-pub-4919341775273582/8645434301");
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
				mInterstitialAd.setAdListener(new AdListener() {
					@Override
					public void onAdClosed() {
						// Load the next interstitial.
						mInterstitialAd.loadAd(new AdRequest.Builder().build());
					}

				});
			} else {
				HideAdmobPanel();
			}
		}
        catch(Exception ex)
        {
  		  Log.d("MY_TEST_LOG", ex.getMessage());
        }
    }

	public void setBounds(View image,float left,float top,float right,float bottom)
	{
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = (int)left;
		lp.topMargin = (int)top;
		lp.width=(int)(right-left);
		lp.height=(int)(bottom-top);
		image.setLayoutParams(lp);
	}

	public void Play(int resource) {
		try
		{
			mp = MediaPlayer.create(this, resource);
			mp.start();
		}
		catch(Exception ex)
		{

		}
	}

	/*public boolean isTablet()
	{
		Display display = this.getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);

		float width = displayMetrics.widthPixels / displayMetrics.xdpi;
		float height = displayMetrics.heightPixels / displayMetrics.ydpi;

		double screenDiagonal = Math.sqrt( width * width + height * height );
		int inch = (int) (screenDiagonal + 0.5);
		return (inch >= 7 );
	}*/

    public void HideAdmobPanel()
    {
    	findViewById(R.id.relativeLayoutAds).setVisibility(View.GONE);
    }

	@Override
	protected void onPause() {
		if(MyGameView!=null && MyGameView.showStarsThread!=null)
			MyGameView.showStarsThread.interrupt();
		super.onPause();
	}

	@Override
    protected void onStop() { 
        wl.release();
    	super.onStop(); 
    } 

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
       super.onPostCreate(savedInstanceState);
		MyGameView.post(new Runnable() {
			@Override
			public void run() {
				MyGameView.StartView();
			}
		});
    }
    
    @Override
    protected void onResume() {
       super.onResume();
       wl.acquire();
    }

    @Override
    protected void onDestroy() {
		/*if(adView!=null)
			adView.destroy();
  	    adView=null;*/
    	super.onDestroy();
    }

	public void StartNextStage(int levelType)
	{
		//btnSelectLevel.setImageResource(R.drawable.select_stage);
		final GameLayout prevGameLayout=gameLayout;
		//if(gameLayout!=null)
		//    MyGameView.removeView(gameLayout);
		gameLayout=null;

		switch (levelType)
		{
			case 0:
			{
				gameLayout=new ExitScreen(GameActivity.this);
				break;
			}
		}
		if(gameLayout!=null)
		{
			RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			gameLayout.setLayoutParams(layoutParams);
			GameActivity.this.MyGameView.addView(gameLayout);
			gameLayout.StartView();
			Animation animation= AnimationUtils.loadAnimation(this, R.anim.show);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if(prevGameLayout!=null)
						MyGameView.removeView(prevGameLayout);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
			gameLayout.startAnimation(animation);
		}
	}

	public void returnToMenu(boolean isStopSound)
	{
		//reloadSettings();
		//if(isStopSound)
		//	Stop();
		//btnSelectLevel.setVisibility(View.VISIBLE);
		//btnSelectLevel.setImageResource(R.drawable.settings);
		if(gameLayout!=null) {
			Animation animation=AnimationUtils.loadAnimation(this,R.anim.hide);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					MyGameView.removeView(gameLayout);
					gameLayout=null;
					//enableButtons(true);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
			gameLayout.startAnimation(animation);
		}
	}

	public int getViewHeight()
	{
		return MyGameView.getHeight();
	}

	public int getViewWidth()
	{
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

}
