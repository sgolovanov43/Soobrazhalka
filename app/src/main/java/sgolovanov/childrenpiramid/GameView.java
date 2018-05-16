package sgolovanov.childrenpiramid;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ACTIVITY_SERVICE;

public class GameView extends RelativeLayout {
	public static GameView mGameView;
    static GameActivity Owner;
    //GameThread game_thread=null;
	public static ZipResourceFile expansionFile=null;
	private MediaPlayer mp = null;
	boolean isVibrator=true;
	boolean isSound=true;
	boolean isStars=true;
	boolean isAnimateBackground=true;
	static int mainVersion=1;
	static int patchVersion=1;
	boolean isWasStoped=false;
    float PrevTouchPos1X=-1;
    float PrevTouchPos1Y=-1;
    float PrevTouchPos2X=-1;
    float PrevTouchPos2Y=-1;
    float PrevDistance=-1;
    public static int CurrentScreenType=100;
    static int current_level_num=0;
    static int last_opened_level=0;
    static String opened_locks="";
    static String current_level;
    static int level_pos_x=10;
    static int level_pos_y=10;
	static float zoom=0.15f;
	static boolean zoom_by_scroll=false;
	//static boolean use_sound=true;
	//static boolean use_vibration=true;
	static int help_screen_num=0;
	static int is_show_help=-1;
	static int level_nums[]=null;
	static int level_max_num=0;
	static int level_cur_num=0;
	static int level_cur_max_num=0;
	static int current_level_record=0;
	static String current_level_time;
	static long start_time;
	static String CompleteLevelText;
	static String level_characters;
	static int EnteredPosition=-1;
	static int level_max_alphavit_num=32;
	static char level_first_symbol='A';
	static int max_alphavit_num=32;
	static int alphavit[]=new int[max_alphavit_num];
	static int level_errors=0;
	static int level_left_errors=5;
	static String founded_symbols="";
	static int game_points=0;
	static int left_hints=3;
	static int game_difficult=2;
	static int current_level_cipher=1;
	Rect SymbolsKeysRect[]=new Rect[max_alphavit_num];
	Rect BackKeyRect=new Rect();
	static String level_author;
	static float cipher_height=40;
	static int top_position=0;
	static int bottom_position=0;
	static boolean is_moving_playground=false;
	boolean is_moving_object=false;
	int current_moving_object=-1;
	//static int background_res_num=R.drawable.backpiece_1_1;
	static int background_num=0;
	static int font_size=2;
	static long end_trial_period=0;
    SharedPreferences GameSettings;
    static String selected_word;
    static int max_result_number=10;
    static long next_background_time=0;
    static int select_level_part=0;
    //int maximum_objects=20;
    MovingImageView ListObjects[]=null;//new MovingImageView[maximum_objects];
    ShadowImageView ListClearObjects[]=null;//new ImageView[maximum_objects];
	AnimatedImageView ListAnimatedObjects[]=null;
	RelativeLayout AnimationLayout=null;
    //ImageView ListShineObjects[]=null;//new ImageView[maximum_objects];
    /*Bitmap ListObjects[]=new Bitmap[maximum_objects];
    Bitmap ListClearObjects[]=new Bitmap[maximum_objects];
    Bitmap ListShineObjects[]=new Bitmap[maximum_objects];
    RectF ListObjectsRect[]=new RectF[maximum_objects];
    RectF ListClearObjectsRect[]=new RectF[maximum_objects];*/
    ImageView Levels[]=new ImageView[6];
    //RectF LevelsRect[]=new RectF[6];
    int ObjectLevelPosition[]=null;
    AudioManager audio;
    boolean IsLoadingLevels=true;
	static ActivityManager.MemoryInfo memoryInfo=null;
    //boolean ListObjectsEmpty[]=new boolean[maximum_objects];

    //Handler handlerChangeBackground = new Handler();
    class showStarsRunnable implements Runnable {
		public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
					GameView.this.post(new Runnable() {
						@Override
						public void run() {
							int starSize = 45;
							if (ListObjects != null) {
								int objectNum = mRnd.nextInt(ListObjects.length);
								//for(int objectNum=0;objectNum<ListObjects.length;objectNum++)
								if (ListObjects[objectNum] != null && ListObjects[objectNum].isSetOnPosition == false) {
									final StarView starView = new StarView(Owner);
									int objectPoints = ListObjects[objectNum].mClearObject.getVisiblePoints();
									Point point = ListObjects[objectNum].mClearObject.getVisiblePoint(mRnd.nextInt(objectPoints));
									int left = ListObjects[objectNum].mClearObject.getLeft() + point.x - starSize / 2;//mRnd.nextInt(Owner.getViewWidth()-starSize)+starSize/2;
									int top = ListObjects[objectNum].mClearObject.getTop() + point.y - starSize / 2;//mRnd.nextInt(Owner.getViewHeight()-starSize)+starSize/2;
									//int left = mRnd.nextInt(Owner.getViewWidth()-starSize)+starSize/2;
									//int top = mRnd.nextInt(Owner.getViewHeight()-starSize)+starSize/2;
									setBounds(starView, left,
											top,
											left + screen_factor * starSize,
											top + screen_factor * starSize);
									GameView.this.addView(starView);
									Animation anim = new AlphaAnimation(0f, 1f);
									anim.setDuration(500);
									anim.setAnimationListener(new Animation.AnimationListener() {
										@Override
										public void onAnimationStart(Animation animation) {

										}

										@Override
										public void onAnimationEnd(Animation animation) {
											starView.clearAnimation();
											GameView.this.removeView(starView);
										}

										@Override
										public void onAnimationRepeat(Animation animation) {

										}
									});
									//anim.setStartOffset(500);
									anim.setRepeatMode(Animation.REVERSE);
									//anim.setRepeatCount(Animation.INFINITE);
									starView.startAnimation(anim);
								}
							}
						}
					});
                	long sleeptime=50;
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }catch(Exception e){
					Log.v("TEST",e.getMessage());
                }
            }
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context,attrs);
		mGameView=this;
        try
        {
			mRnd.setSeed(System.currentTimeMillis());
            Owner=(GameActivity) context;
			setMemoryInfo();
			if(expansionFile==null) {
				try {
					expansionFile = APKExpansionSupport.getAPKExpansionZipFile(Owner,
							mainVersion, patchVersion);
				}
				catch (IOException e)
				{
					Log.v("Open zip file error: ",e.toString());
				}
			}

			//setFocusable(true);
            //setFocusableInTouchMode(true);
            //onCreated();
    	}
    	catch(Exception ex)
    	{
    		  //Log.d("MY_TEST_LOG", ex.getMessage());
    	}
    }
    
    public void StartView() {
    	try
    	{
			soundFromObb("soobrazhalka");
      		//NewBackground=new ImageView(Owner);
      		//this.addView(NewBackground);
      		//setBounds(NewBackground,0,0,Owner.getViewWidth(),Owner.getViewHeight());
       		this.requestFocus();
    		this.setFocusableInTouchMode(true);
    		audio = (AudioManager) Owner.getSystemService(Context.AUDIO_SERVICE);
            vibrator = (Vibrator)Owner.getSystemService(Context.VIBRATOR_SERVICE);
            //game_thread=new GameThread(this);
    		//setRunning(true);
            GameSettings = Owner.getSharedPreferences(GameActivity.SHARED_PREFS_NAME, 0);
        	InitThread(GameSettings);
        	IsLoadingLevels=true;
        	//MakeAllLevelsIcons();
        	IsLoadingLevels=false;
        	
    		//if(Owner.isFirstStartAfterSoobrazhalka2!=1 && CurrentScreenType==100)
    	    //    soundFromObb("soobrazhalka");
    		if(CurrentScreenType==200)
    			MakeFourLevelsIcons();
    	    InitMainMenu();
    		
    		if(CurrentScreenType==5 || CurrentScreenType==6)
    		{
				top_position=0;
				//InitGameResults();
    		}

    		//setRunning(true);

    		if(CurrentScreenType!=100 && CurrentScreenType!=102 && CurrentScreenType!=201)
    		{
    			InitGameScreen();
    		}
    	}
    	catch(Exception ex)
    	{
			String msg=ex.getMessage();
  		  Log.d("MY_TEST_LOG", ex.getMessage());
    	}
    }

    /*public void surfaceDestroyed(SurfaceHolder arg0) {
    	if(game_thread!=null)
    	{
    		mTimerTask.cancel();
    		RecycleBitmaps();
    	}
    	isWasStoped=true;
    	//mTimerTask=null;
    	//onDestroy();
    }*/
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {     
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
    	{         
    		event.startTracking();         
    		return true;     
    	}     
    	else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
    	{
    		audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    		return true;
    	}
    	else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
    	{
    		audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    		return true;    	
    	}
    	/*else if (keyCode == KeyEvent.KEYCODE_MENU)
    	{
    		Intent intent = new Intent(Owner,SettingsActivity.class);
    		Owner.startActivityForResult(intent,1);
    	}*/
    	return super.onKeyDown(keyCode, event); 
	}
    
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{     
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) 
		{
	    	if(CurrentScreenType==100)
	    	{
	    		Exit();
	    	}

			if(CurrentScreenType==1000)
			{
				this.removeView(Owner.gameLayout);
				CurrentScreenType=100;
			}

			else if(CurrentScreenType==1 || CurrentScreenType==200 || CurrentScreenType==300)
	    	{
				SmallClick();
	    		ReturnToMainMenu();
	    		CurrentScreenType=100;
	    	}

			else if(CurrentScreenType==2)// || CurrentScreenType==1)
			{
				SmallClick();
				startSelectLevel(3);
				CurrentScreenType=200;
			}

			else if(CurrentScreenType==102 ||
	 	    	    CurrentScreenType==502 || 
	    			CurrentScreenType==3 || 
	    			CurrentScreenType==5 ||
	    			CurrentScreenType==6)
	    	{
				SmallClick();
	    		InitMainMenu();
	    		CurrentScreenType=100;
	    	}
			return true;     
		}     
		return super.onKeyUp(keyCode, event); 
	}
	
	private boolean isImageContains(ImageView image,int x,int y)
	{
		if(image!=null && image.getTop()<=y && image.getBottom()>=y &&
		   image.getLeft()<=x && image.getRight()>=x)
			return true;
		return false;
	}
	
    //@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(IsLoadingLevels)
    		return false;
		if(event.getAction()!=MotionEvent.ACTION_DOWN)
			return false;
    	if(CurrentScreenType==100)
    	{
    		if(isImageContains(StartGameButton,(int)event.getX(), (int)event.getY()))
    		{
				//SmallClick();
    			//StartLevel();
				soundFromObb("start_game");
				SmallClick();
				select_level_part=0;
				//ClearMainMenu();
				startSelectLevel(1);
				//MakeFourLevelsIcons();
				CurrentScreenType=200;
    		}
    		else if(isImageContains(ExitGameButton,(int)event.getX(), (int)event.getY()))
    		{
	    		Exit();
    		}
    		/*else if(isImageContains(LevelGameButton,(int)event.getX(), (int)event.getY()))
    		{
				SmallClick();
				select_level_part=0;
				ClearMainMenu();
				MakeFourLevelsIcons();
				CurrentScreenType=200;
    		}*/
    		else if(isImageContains(SettingsButton,(int)event.getX(), (int)event.getY()))
    		{
				soundFromObb("start_game");
				SmallClick();
				//InitSettings();
				startSelectLevel(2);
				CurrentScreenType=300;
    		}
    		return false;
    	}
    	if(CurrentScreenType==300)
    	{
    		int old_back_type=BackgroundType;
    		int old_bukvy_type=BukvyType;
    		boolean old_vibration_type=VibrationType;
			boolean is_animate_background=isAnimateBackground;
			boolean is_stars=isStars;
    		if(isImageContains(BukvyTypeSettings,(int)event.getX(), (int)event.getY()))
    		{
				soundFromObb("start_game");
				SetBukvyType(BukvyType==0?1:0);
				SmallClick();
    		}
    		if(isImageContains(VibrationSettings,(int)event.getX(), (int)event.getY()))
    		{
				soundFromObb("start_game");
				SetVibrationType(!VibrationType);
				SmallClick();
    		}
			if(isImageContains(AnimateBackgroundSettings,(int)event.getX(), (int)event.getY()))
			{
				soundFromObb("start_game");
				SetAnimateBackground(!isAnimateBackground);
				SmallClick();
			}
			if(isImageContains(StarsSettings,(int)event.getX(), (int)event.getY()))
			{
				soundFromObb("start_game");
				SetStars(!isStars);
				SmallClick();
			}
    		if(old_back_type!=BackgroundType)
    		{
    			//SetNextBackgroundTime();
    			Editor edit=GameSettings.edit();
    			edit.putInt(Owner.getString(R.string.background_key), BackgroundType);
    			edit.commit();
    		}
    		if(old_bukvy_type!=BukvyType)
    		{
    			Editor edit=GameSettings.edit();
    			edit.putInt(Owner.getString(R.string.bukvy_key), BukvyType);
    			edit.commit();
    		}
    		if(old_vibration_type!=VibrationType)
    		{
    			Editor edit=GameSettings.edit();
    			edit.putBoolean(Owner.getString(R.string.vibration_key), VibrationType);
    			edit.commit();
    		}
			if(is_stars!=isStars)
			{
				Editor edit=GameSettings.edit();
				edit.putBoolean(Owner.getString(R.string.stars_key), isStars);
				edit.commit();
			}
			if(is_animate_background!=isAnimateBackground)
			{
				Editor edit=GameSettings.edit();
				edit.putBoolean(Owner.getString(R.string.animate_background_key), isAnimateBackground);
				edit.commit();
			}
    		return false;
    	}
    	else if(CurrentScreenType==2)
    	{
    		if(isImageContains(GameButtonMoon,(int)event.getX(), (int)event.getY()))
    		{
				SmallClick();
    			PrevLevel();
				return true;
			}
    		else if(isImageContains(GameButtonFlower,(int)event.getX(), (int)event.getY()))
    		{
				SmallClick();
    			StartLevel();
				return true;
			}
    		else if(isImageContains(GameButtonSunshine,(int)event.getX(), (int)event.getY()))
    		{
				SmallClick();
    			NextLevel();
				return true;
			}
    	}
    	else if(CurrentScreenType==200)
    	{
    		if(Levels[4]!=null && isImageContains(Levels[4],(int)event.getX(), (int)event.getY()))
    		{
				//soundFromObb("start_game");
    			if(select_level_part>0)
    			{
    				select_level_part--;
    				MakeFourLevelsIcons();
    			}
    		}
    		else if(Levels[5]!=null && isImageContains(Levels[5],(int)event.getX(), (int)event.getY()))
    		{
				//soundFromObb("start_game");
    			if((select_level_part+1)*4<levels.length)
    			{
    				select_level_part++;
    				MakeFourLevelsIcons();
    			}
    		}
    		for(int i=0;i<4;i++)
    		{
    			if(Levels[i]!=null && isImageContains(Levels[i],(int)event.getX(), (int)event.getY()) && select_level_part*4+i<levels.length)
    			{
    				current_level_num=select_level_part*4+i;
					setLevelBackground();
    	        	ClearSelectLevels();
    				StartLevel();
    			}
    		}
			return false;
    	}
    	else if(CurrentScreenType==1)
    	{
    		return true;
    	}
    	
        return false;//super.onTouchEvent(event);
	}
    
    //class GameThread {

    	//GameView OwnerView;
        public static Random mRnd=new Random();
    	boolean mRun=false;
    	boolean yet_draw=false;
    	
    	String PlayerName="Player";
    	int settings_cipher_num=1;
    	int anim_step=0;
    	int anim_step2=0;
        int screen_width=0;
        int screen_height=0;
    	public boolean GameStarted=false;
    	public boolean isPaused=false;
    	public int StartLoop=0;
    	//private MediaPlayer mp = null;

    	Vibrator vibrator;
        long stage_complete_time=0;
        
        int BackgroundType=3;
        static int BukvyType=0;
        boolean VibrationType=true;
        ImageView StartGameButton=null;
        ImageView ExitGameButton=null;
        ImageView LevelGameButton=null;
		ImageView StartCvetyImage=null;
		ImageView StartListiyaImage=null;
		ImageView StartListikImage=null;
		ImageView StartStar1Image=null;
		ImageView StartStar2Image=null;
		ImageView StartStar3Image=null;
		ImageView StartStar4Image=null;
		ImageView StartStar5Image=null;

        /*ImageView BackgroundSettings=null;
        ImageView Background1Button=null;
        ImageView Background1ButtonShadow=null;
        ImageView Background1ButtonWhite=null;
        ImageView Background2Button=null;
        ImageView Background2ButtonShadow=null;
        ImageView Background2ButtonWhite=null;
        ImageView Background3Button=null;
        ImageView Background3ButtonShadow=null;
        ImageView Background3ButtonWhite=null;
        ImageView Background4Button=null;
        ImageView Background4ButtonShadow=null;
        ImageView Background4ButtonWhite=null;*/
        //ImageView BlinkSettings=null;
        //ImageView BlinkSettingsShadow=null;
        //ImageView BlinkSettingsWhite=null;
		ImageView BukvyTypeSettings=null;
        ImageView VibrationSettings=null;
		ImageView AnimateBackgroundSettings=null;
		ImageView AnimateBackgroundSettings2=null;
		ImageView StarsSettings=null;
        //ImageView VibrationSettingsShadow=null;
        //ImageView VibrationSettingsWhite=null;

        //ImageView MainBackground=null;
        //ImageView NewBackground=null;
        //ImageView MainMenuCaption=null;
        ImageView GlassView;
 
    	//boolean IsVibrator=true;
    	//boolean IsSound=true;

	ImageView ListBackground=null;

	LevelItem levels[]={
			new LevelItem(1,"raduga_background",-1,"list_4","soberem_raketu",new PictureItem[]{
					new PictureItem(150-450,404.5f, "raketa1",null),
					new PictureItem(209.5f-450,186.5f, "raketa2",null),
					new PictureItem(380.5f-450,519.5f, "raketa3",null),
					new PictureItem(401.5f-450,269.5f, "raketa4",null),
					new PictureItem(604.5f-450,205, "raketa5",null),
					new PictureItem(774.5f-450,133.5f, "raketa6",null)}),

			new LevelItem(1,"den_rozhdenia_back",-1,"den_rozhdenia_list","den_rozhdeniya",new PictureItem[]{
					new PictureItem(654-515,408f, "den_rozhdenia1",null),
					new PictureItem(220.5f-515,423f, "den_rozhdenia2",null),
					new PictureItem(420-515,430, "den_rozhdenia3",null),
					new PictureItem(833.5f-515,418f, "den_rozhdenia4",null),
			}),

			new LevelItem(1,"back1_full",-3,"list_1","slogi_piramidky",new PictureItem[]{
					new PictureItem(0,575.5f,"picture_1_1","krasnyi"),
					new PictureItem(0,495,"picture_1_2","zelenyi"),
					new PictureItem(0,415,"picture_1_3","sinii"),
					new PictureItem(0,335,"picture_1_4","zheltyi"),
					new PictureItem(0,255,"picture_1_5","salatoavyi"),
					new PictureItem(0,175,"picture_1_6","fioletovyi"),
					new PictureItem(0,70f,"picture_1_7","oranzhevyi")}),//piramidkaItems),

			new LevelItem(1,"back13_full",0,"list_4","rasstav_griby",new PictureItem[]{
					new PictureItem(-250,320, "grib1", "bolshoi"),
					new PictureItem(100,375, "grib2", "srednii"),
					new PictureItem(360,430, "grib3", "malenkii")}),//gribyItems), ///-----------------------------------------------

		new WordLevelItem("back16_full",0,"list_12","mama","МАМА"),

			new LevelItem(1,"africa_background",16,"list_4","giraf",new PictureItem[]{
					new PictureItem(19,95, "jiraf1", null),
					new PictureItem(10,298, "jiraf2", null),
					new PictureItem(-92,431, "jiraf3", null),
					new PictureItem(-40,509, "jiraf4", null),
					new PictureItem(55,511, "jiraf5", null)}),//zhirafikItems),

		new LevelItem(1,"leto_oblaka",0,"ezhik_list2","chto_neset_ezik",new PictureItem[]{
				new PictureItem(-340,120, "listik", "list"),
				new PictureItem(-30,170, "morkovka2", "morkovka"),
				new PictureItem(180,360, "yabloko2", "yabloko"),
				new PictureItem(400,160, "gribok", "grib")}),//ezhikItems),

			new LevelItem(1,"back34_full",-1,"tort_list","level_tort",new PictureItem[]{
					new PictureItem(324.5f-325,441f, "tort1",null),
					new PictureItem(313.5f-325,332.5f, "tort2",null),
					new PictureItem(316f-325,231f, "tort3",null),
					new PictureItem(318.5f-325,166.5f, "tort4",null),
					new PictureItem(320f-325,109f, "tort5",null),
			}),

			new LevelItem(1,"back10_full",11,"list_4","soberi_busy",new PictureItem[]{
					new PictureItem(450,195, "picture_4_4", null),
					new PictureItem(341,316, "picture_4_1", null),
					new PictureItem(165,376, "picture_4_3", null),
					new PictureItem(0,400, "picture_4_5", null),
					new PictureItem(-165,376, "picture_4_8", null),
					new PictureItem(-341,316, "picture_4_6", null),
					new PictureItem(-450,196, "picture_4_9", null)}),//busyItems),
		new WordLevelItem("bottles_background",-3,"list_12","papa","ПАПА"),
			new LevelItem(1,"bottles_background"/*"back11_full"*/,0,"list_5a","posadim_cvetok",new PictureItem[]{
					new PictureItem(-335,191, "picture_5_1", null),
					new PictureItem(-162,290, "picture_5_2", null),
					new PictureItem(-2,195, "picture_5_3", null),
					new PictureItem(165,269, "picture_5_4", null),
					new PictureItem(330,191, "picture_5_5", null)}),//cvetyVGorshkahItems),
		/*new MoveToSidesLevelItem("doska",0,"list_12","pravo_levo",new PictureItem[]{
				new PictureItem(0,0,"morkovka2",new String[]{"morkovka","nalevo"},3),
				new PictureItem(0,0,"curl_pomidor",new String[]{"pomidor","napravo"},3),
		}),*/
			new LevelItem(1,"start_fone2",-3,"maski_list","maski",new PictureItem[]{
					new PictureItem(390-570,139, "maski1",null),
					new PictureItem(768.5f-570,181.5f, "maski2",null),
					new PictureItem(964-570,145.5f, "maski3",null),
					new PictureItem(159.5f-570,162, "maski4",null),
					new PictureItem(583.5f-570,164, "maski5",null)}),
			new LevelItem(1,"back1_full",0,"list_1","soberi_domik",new PictureItem[]{
					new PictureItem(290.5f-300,67.5f, "domik1",null),
					new PictureItem(292-300,219.5f, "domik2",null),
					new PictureItem(302.5f-300,463, "domik3",null),
					new PictureItem(170-300,444, "domik4",null),
					new PictureItem(432.5f-300,442.5f, "domik5",null)}),
		new LevelItem(1,"inoplanetyane_background",-1,"inoplanetyane_list","smogeh_otpravit_kosmicheskie_apparaty_v_polet",new PictureItem[]{
				new PictureItem(870-500,101,"inoplanetyane_1",null),
				new PictureItem(154-500,369,"inoplanetyane_2",null),
				new PictureItem(282-500,69,"inoplanetyane_3",null),
				new PictureItem(526-500,208,"inoplanetyane_4",null),
				new PictureItem(842-500,322,"inoplanetyane_5",null)}),
			new LevelItem(1,"back4_full",-3,"list_4","kukolki",new PictureItem[]{
					new PictureItem(-365,190, "picture_6_1", null),
					new PictureItem(-220,430, "picture_6_3", null),
					new PictureItem(-80,190, "picture_6_2", null),
					new PictureItem(100,430, "picture_6_4", null),
					new PictureItem(265,190, "picture_6_6", null),
					new PictureItem(410,430, "picture_6_5", null)}),//kuklyItems),
			new LevelItem(1,"savana",24,"list_650x620","leopard",new PictureItem[]{
					new PictureItem(119,130, "leopard1",null),
					new PictureItem(121,345, "leopard2",null),
					new PictureItem(224,434, "leopard3",null),
					new PictureItem(-193,321, "leopard4",null),
					new PictureItem(-19,337, "leopard5",null),
					new PictureItem(-165,457, "leopard6",null),
					new PictureItem(21,450, "leopard7",null),
					new PictureItem(-30,466, "leopard8",null)}),//leopardItems),
		new LevelItem(3,"fixiki_background",8,"svetofor_list","svetofor",new PictureItem[]{
				new PictureItem(213-212,119,"svetofor_krasnyj","krasnyi",R.drawable.kraska_krasnaya),
				new PictureItem(213-212,269,"svetofor_zheltyj","zheltyi",R.drawable.kraska_zheltaya),
				new PictureItem(213-212,418,"svetofor_zelenyj","zelenyi",R.drawable.kraska_zelenaya),
		}),
			new LevelItem(1,"back8_full",0,"list_8","ptichki",new PictureItem[]{
					new PictureItem(-325,263, "picture_8_1",null),
					new PictureItem(-130,320, "picture_8_2",null),
					new PictureItem(70,347, "picture_8_3",null),
					new PictureItem(236,318, "picture_8_4",null),
					new PictureItem(440,370, "picture_8_5",null),
					new PictureItem(350,120, "picture_8_6",null)}),//ptichkiNaVetkeItems),
		new WordLevelItem("back35_full",0,"list_12","mir","МИР"),
			new LevelItem(1,"back1_full",13,"parovozik","poravozik",new PictureItem[]{
					new PictureItem(-446,355, "picture_9_2",null),
					new PictureItem(-296,355, "picture_9_4",null),
					new PictureItem(-134,360, "picture_9_5",null),
					new PictureItem(20,360, "picture_9_1",null),
					new PictureItem(192,299, "picture_9_3",null)}),//parovozikItems),
		new LevelItem(1,"back19_full",-2,"list_4","slozhi_deda",new PictureItem[]{
					new PictureItem(-106,153, "ded_moroz1",null),
					new PictureItem(-142,377, "ded_moroz2",null),
					new PictureItem(0,163, "ded_moroz3",null),
					new PictureItem(68,194, "ded_moroz4",null),
					new PictureItem(-64,407, "ded_moroz5",null),
					new PictureItem(141,558, "ded_moroz6",null),
					new PictureItem(97,415, "ded_moroz7",null)}
					),
			new LevelItem(1,"kolobok_background",0,"list_12","kolobok",new PictureItem[]{
					new PictureItem(1025-660,424.5f, "kolobok_babka","baba"),
					new PictureItem(296-660,390, "kolobok_lisa","lisa"),
					new PictureItem(487-660,457, "kolobok_medved","medved"),
					new PictureItem(655-660,248, "kolobok_volk","volk"),
					new PictureItem(756-660,470, "kolobok_zayac","zayac"),
					new PictureItem(864-660,354, "kolobok_kolobok","kolobok2"),
					new PictureItem(1113-660,265, "kolobok_ded","ded")}),

			new LevelItem(1,"ovoschi_background",0,"clear_1200x620","pologi_ovoshi_na_svou_ten",new PictureItem[]{
				new PictureItem(191-600,158,"curl_tykva","tykva"),
				new PictureItem(177-600,460,"curl_pomidor","pomidor"),
				new PictureItem(443-600,275,"curl_perchiki","ostry_perec"),
				new PictureItem(565-600,99,"curl_ogurcy","ogyrci"),
				new PictureItem(545-600,507,"curl_perec","perec"),
				new PictureItem(788-600,324,"curl_kapusta","kapysta"),
				new PictureItem(1031-600,164,"curl_brokkoli","brokkoli"),
				new PictureItem(1031-600,479,"curl_baklazhan","baklazhan")}),
			new LevelItem(1,"repka_background",-1,"list_4","znaech_skazky",new PictureItem[]{
					new PictureItem(306-575,330,"repka1","repka"),
					new PictureItem(532-575,342,"repka2","dedka"),
					new PictureItem(663-575,361,"repka3","babka"),
					new PictureItem(784-575,394,"repka4","vnychka"),
					new PictureItem(875-575,411,"repka5","zuchka"),
					new PictureItem(974-575,427,"repka6","koshka"),
					new PictureItem(1067-575,445,"repka7","myshka")}),//piramidkaItems),
			new LevelItem(1,"morozhenye_background",-2,"list_4","slozhi_snegurochku",new PictureItem[]{
					new PictureItem(0,90, "snegurochka1",null),
					new PictureItem(57,109, "snegurochka2",null),
					new PictureItem(-50,198, "snegurochka3",null),
					new PictureItem(90,315, "snegurochka4",null),
					new PictureItem(17,193, "snegurochka5",null),
					new PictureItem(-68,430, "snegurochka6",null),
					new PictureItem(71,423, "snegurochka7",null)}),
		new LevelItem(1,"stars_texture",-1,"list_4","rakety",new PictureItem[]{
				new PictureItem(-360,400, "rakety1",null),
				new PictureItem(-180,200, "rakety5",null),
				new PictureItem(0,350, "rakety2",null),
				new PictureItem(200,200, "rakety3",null),
				new PictureItem(400,400, "rakety4",null)}),//raketyItems),
			new LevelItem(1,"back34_full",0,"list_3","soberi_podarki",new PictureItem[]{
					new PictureItem(-55,104, "picture_10_1",null),
					new PictureItem(55,243, "picture_10_2",null),
					new PictureItem(-45,382, "picture_10_3",null),
					new PictureItem(-5,548, "picture_10_4",null)}),//podarkiItems),
		new WordLevelItem("back21_full",0,"list_12","dryg","ДРУГ"),
			new LevelItem(1,"obezyana_background",0,"list_520x620","obezyana",new PictureItem[]{
					new PictureItem(-82,142, "obezyana1",null),
					new PictureItem(-36,334, "obezyana2",null),
					new PictureItem(-53,389, "obezyana3",null),
					new PictureItem(-166,433, "obezyana4",null),
					new PictureItem(89,351, "obezyana5",null),
					new PictureItem(82,426, "obezyana6",null),
					new PictureItem(23,520, "obezyana7",null),
					new PictureItem(204,473, "obezyana8",null)}),//obezyanaItems),
			new LevelItem(1,"back26_full",0,"odezhda_back","odezhda",new PictureItem[]{
					new PictureItem(-226,384, "odezhda_part1","platie"),
					new PictureItem(-222,70, "odezhda_part2","shljapa"),
					new PictureItem(223,60, "odezhda_part3","beret"),
					new PictureItem(241,338, "odezhda_part4","palto"),
					new PictureItem(262,560, "odezhda_part5","sapogi")}),//odezhdaNaKuklahItems),
			new LevelItem(1,"menu_background",-3,"list_4","zaiki",new PictureItem[]{
					new PictureItem(-365,180, "picture_11_1",null),
					new PictureItem(-241,440, "picture_11_2",null),
					new PictureItem(-14,180, "picture_11_5",null),
					new PictureItem(98,440, "picture_11_3",null),
					new PictureItem(255,175, "picture_11_4",null),
					new PictureItem(410,440, "picture_11_6",null)}),//zaikiItems),
		/*new MoveToSidesLevelItem("doska",0,"list_12","pravo_levo_verx",new PictureItem[]{
				new PictureItem(0,0,"morkovka2",new String[]{"morkovka","nalevo"},2),
				new PictureItem(0,0,"grib3",new String[]{"grib","napravo"},3),
				new PictureItem(0,0,"curl_pomidor",new String[]{"pomidor","verx"},3),
		}),*/
			new LevelItem(1,"back7_full",-3,"list_4","mashinki",new PictureItem[]{
					new PictureItem(-365,150, "picture_12_1",null),
					new PictureItem(-340,500, "picture_12_2",null),
					new PictureItem(-80,130, "picture_12_3",null),
					new PictureItem(45,510, "picture_12_4",null),
					new PictureItem(320,120, "picture_12_5",null),
					new PictureItem(390,500, "picture_12_6",null),
					new PictureItem(-80,285, "picture_12_7",null)}),
			new LevelItem(1,"morozhenye_background",-2,"korablik_list","morogenoe",new PictureItem[]{
					new PictureItem(125-400,174, "morozhenoe1",null),
					new PictureItem(314-400,178, "morozhenoe2",null),
					new PictureItem(514-400,170, "morozhenoe3",null),
					new PictureItem(691-400,183, "morozhenoe4",null),
					new PictureItem(123-400,479, "morozhenoe5",null),
					new PictureItem(323-400,467, "morozhenoe6",null),
					new PictureItem(508-400,471, "morozhenoe7",null),
					new PictureItem(693-400,473, "morozhenoe8",null)}),

			new LevelItem(1,"back10_full",0,"list_4","cartiny",new PictureItem[]{
					new PictureItem(-365,160, "picture_15_1",null),
					new PictureItem(-365,450, "picture_15_5",null),
					new PictureItem(45,180, "picture_15_2",null),
					new PictureItem(45,460, "picture_15_3",null),
					new PictureItem(365,160, "picture_15_4",null),
					new PictureItem(365,450, "picture_15_6",null)}),
		/*new LevelItem(5,"back21_full","applicaciya_gribok_list","appl_grib",new PictureItem[]{
				new ApplicationPictureItem(0,357f,"applicaciya_gribok2",null,362,409),
				new ApplicationPictureItem(0,448.5f,"applicaciya_gribok1",null,248,114.5f),
				new ApplicationPictureItem(0,169f,"applicaciya_gribok3",null,113,418.5f,90),
		}),*/
		new LevelItem(1,"christmas_texture",-2,"list_4","soberi",new PictureItem[]{
					new PictureItem(-320,415, "ded_moroz","ded_moroz"),
					new PictureItem(-20,425, "snegovik","snegovik"),
					new PictureItem(400,413, "snegurochka","snegurochka"),
					new PictureItem(220,280, "elochnaya_girlyanda","girlyanda"),
					new PictureItem(60,103, "elochnye_igrushki","elochnye_igrushki"),
					new PictureItem(-238,122, "svechka","svechka"),
					new PictureItem(160,519, "snezhnyi_shap","snezhnyi_shap")}),
		new LevelItem("back4_full","polki","banki_na_polkah",new PictureItem[]{
					new PictureItem(-100,110, "banka1",null),
					new PictureItem(100,105, "banka2",null),
					new PictureItem(-150,315, "banka3",null),
					new PictureItem(10,315, "banka4",null),
					new PictureItem(160,315, "banka5",null),
					new PictureItem(-100,520, "banka6",null),
					new PictureItem(100,520, "banka7",null)}),

			new LevelItem(1,"stars_texture",-1,"list_860x620","kosmos",new PictureItem[]{
					new PictureItem(748-430,477, "kosmos_kosmonavt","kosmonavt"),
					new PictureItem(532-430,481, "kosmos_lunohod","lynoxod"),
					new PictureItem(143-430,460, "kosmos_luna","lyna"),
					new PictureItem(162-430,109, "kosmos_sputnik","spytnik"),
					new PictureItem(373-430,272, "kosmos_raketa","raketa"),
					new PictureItem(181-430,253, "kosmos_teleskop","teleskop"),
					new PictureItem(653-430,200, "kosmos_solnce","solnce")}),//raketyItems),
		new LevelItem(1,"vannaya_komnata_background",14,"vannaya_komnata_list","pomogi_rasstavit_veshi_v_vannoi",new PictureItem[]{
				new PictureItem(91-463,266,"vannaya_komnata_1","zerkalo"),
				new PictureItem(832-463,474,"vannaya_komnata_2","korzina_dlya_belya"),
				new PictureItem(205-463,106,"vannaya_komnata_3","kyvshin"),
				new PictureItem(308-463,103,"vannaya_komnata_4","shampoo"),
				new PictureItem(429-463,88,"vannaya_komnata_5","zybnye_chetki"),
				new PictureItem(824-463,245,"vannaya_komnata_6","polotence"),
				new PictureItem(651-463,475,"vannaya_komnata_7","stiralnya_mashina")}),
			new LevelItem(1,"menu_background",-3,"list_4","kloun",new PictureItem[]{
					new PictureItem(-112,96, "picture_26_1",null),
					new PictureItem(-75,187, "picture_26_2",null),
					new PictureItem(-48,274, "picture_26_3",null),
					new PictureItem(103,214, "picture_26_4",null),
					new PictureItem(-155,355, "picture_26_5",null),
					new PictureItem(0,390, "picture_26_6",null),
					new PictureItem(-58,554, "picture_26_7",null),
					new PictureItem(70,534, "picture_26_8",null)
			}),
			new LevelItem("back28_full","list_4","cifry",new PictureItem[]{
					new PictureItem(-450,160, "cifra_1","c1"),
					new PictureItem(-225,160, "cifra_2","c2"),
					new PictureItem(0,160, "cifra_3","c3"),
					new PictureItem(225,160, "cifra_4","c4"),
					new PictureItem(450,160,  "cifra_5","c5"),
					new PictureItem(-337,450, "cifra_6","c6"),
					new PictureItem(-112,450, "cifra_7","c7"),
					new PictureItem(112,450, "cifra_8","c8"),
					new PictureItem(337,450, "cifra_9","c9")
			}),

			new LevelItem(1,"back34_full",-1,"zoo_magazin_list","zoo_magazin",new PictureItem[]{
					new PictureItem(913f-517,157f, "zoo_magazin1",null),
					new PictureItem(752.5f-517,470.5f, "zoo_magazin2",null),
					new PictureItem(497f-517,159.5f, "zoo_magazin3",null),
					new PictureItem(231.5f-517,210f, "zoo_magazin4",null),
					new PictureItem(370.5f-517,530f, "zoo_magazin5",null),
			}),

			new LevelItem("back2_full","list_4","agody",new PictureItem[]{
					new PictureItem(-410,160, "picture_7_1","vishnya"),
					new PictureItem(-140,160, "picture_7_2","klubnika"),
					new PictureItem(170,160, "picture_7_3","grusha"),
					new PictureItem(430,160, "picture_7_4","sliva"),
					new PictureItem(-410,450, "picture_7_5","yabloko"),
					new PictureItem(-130,450, "picture_7_8","abrikos"),
					new PictureItem(170,450, "picture_7_7","arbuz"),
					new PictureItem(430,450, "picture_7_6","smorodina")
			}),
		/*new LevelItem(5,"back21_full","applicaciya_elka_list","appl_elka",new PictureItem[]{
				new ApplicationPictureItem(0,345.5f,"applicaciya_elka2",null,97.5f,425,-90),
				new ApplicationPictureItem(0,247,"applicaciya_elka3",null,146.5f,68),
				new ApplicationPictureItem(0,451.5f,"applicaciya_elka4",null,147.5f,204f,90),
				new ApplicationPictureItem(0,487.5f,"applicaciya_elka5",null,150.5f,152.5f),
				new ApplicationPictureItem(0,169.5f,"applicaciya_elka1",null,150,546.5f),
		}),*/
			new LevelItem(1,"back6_full",-3,"list_4","matreshki",new PictureItem[]{
					new PictureItem(-436,523, "picture_16_1",null),
					new PictureItem(-337,470, "picture_16_2",null),
					new PictureItem(-170,422, "picture_16_3",null),
					new PictureItem(65,360, "picture_16_4",null),
					new PictureItem(358,310, "picture_16_5",null)
			}),
			new LevelItem(1,"back1_full",-3,"list_4","figury",new PictureItem[]{
					new PictureItem(-365,160, "picture_17_1","kvadrat"),
					new PictureItem(-365,450, "picture_17_2","krug"),
					new PictureItem(45,180, "picture_17_3","oval"),
					new PictureItem(45,460, "picture_17_4","treugolnik"),
					new PictureItem(365,160, "picture_17_5","pryamougolnik"),
					new PictureItem(365,450, "picture_17_6","romb")
			}),
		new LevelItem(1,"mama_nas_kuhne_background",15,"mama_nas_kuhne_list","pomogi_mame_rasstavit_na_kyxne",new PictureItem[]{
				new PictureItem(882-470,197,"mama_nas_kuhne_1","terka"),
				new PictureItem(383-470,340,"mama_nas_kuhne_2","tort"),
				new PictureItem(561-470,93,"mama_nas_kuhne_3","lopatka2"),
				new PictureItem(612-470,89,"mama_nas_kuhne_4","doska"),
				new PictureItem(671-470,94,"mama_nas_kuhne_5","polovnik"),
				new PictureItem(199-470,368,"mama_nas_kuhne_6","chaynik"),
				new PictureItem(272-470,169,"mama_nas_kuhne_7","mikrovolnovka"),
				new PictureItem(164-470,47,"mama_nas_kuhne_8","chaynik"),
				new PictureItem(558-470,382,"mama_nas_kuhne_9","konfety")}),
			new LevelItem("back5_full","list_4","ovoschi",new PictureItem[]{
					new PictureItem(-400,150, "picture_18_1","morkov"),
					new PictureItem(-134,120, "picture_18_2","pomidor"),
					new PictureItem(134,150, "picture_18_3","redis"),
					new PictureItem(400,150, "picture_18_4","goroh"),
					new PictureItem(-266,310, "picture_18_5","perec"),
					new PictureItem(0,310, "picture_18_6","ogurec"),
					new PictureItem(267,310, "picture_18_7","tykva"),
					new PictureItem(-350,500, "picture_18_9","kartofel"),
					new PictureItem(-120,470, "picture_18_10","baklazhan"),
					new PictureItem(130,470, "picture_18_11","chesnok"),
					new PictureItem(400,470, "picture_18_8","luk")
			}),
			new LevelItem("back4_full","list_4","a_ti_znaesh",new PictureItem[]{
					new PictureItem(-365,100, "dub","dub"),
					new PictureItem(-380,320, "ryabina","ryabina"),
					new PictureItem(-380, 520, "sosna","sosna"),
					new PictureItem(-120,120, "bereza","bereza"),
					new PictureItem(-70,285, "el","el"),
					new PictureItem(0,483, "klen","klen"),
					new PictureItem(380,115, "olha","olxa"),
					new PictureItem(350,430, "lipa","lipa"),
					new PictureItem(160,190, "iva","iva")
			}),
			new LevelItem(1,"back33_full",9,"list_4","domashnie_zhivotnye",new PictureItem[]{
					new PictureItem(-430,150, "picture_19_6","kurica"),
					new PictureItem(-140,150, "picture_19_2","korova"),
					new PictureItem(160,150, "picture_19_3","ovca"),
					new PictureItem(430,150, "picture_19_4","svanya"),
					new PictureItem(-430,450, "picture_19_5","osel"),
					new PictureItem(-140,450, "picture_19_1","loshad"),
					new PictureItem(160,450, "picture_19_7","byk"),
					new PictureItem(430,450, "picture_19_8","utka")
			}),
		/*new LevelItem(5,"back21_full","applicaciya_domik_list","appl_dom",new PictureItem[]{
				new ApplicationPictureItem(108,147,"applicaciya_domik1",null,414.5f,458),
				new ApplicationPictureItem(0,180.5f,"applicaciya_domik2",null,232,112,180),
				new ApplicationPictureItem(0,394,"applicaciya_domik3",null,188.5f,470),
				new ApplicationPictureItem(0,389,"applicaciya_domik4",null,117.5f,278.5f),
				new ApplicationPictureItem(0,389,"applicaciya_domik5",null,343,217,38),
		}),*/
			new LevelItem(1,"back32_full",0,"list_4","dikie_zhivotnye",new PictureItem[]{
					new PictureItem(-365,160, "medved","medved"),
					new PictureItem(-365,450, "lisa","lisa"),
					new PictureItem(0,180, "belka2","belka"),
					new PictureItem(0,460, "zayats1","zayac"),
					new PictureItem(365,160, "ezh","ezh"),
					new PictureItem(350,420, "olen","olen")
			}),
			new LevelItem(1,"back24_full",11,"list_10","zvezdochki",new PictureItem[]{
					new PictureItem(-10,230, "picture_23_1",null),
					new PictureItem(395,375, "picture_23_2",null),
					new PictureItem(-380,180, "picture_23_3",null),
					new PictureItem(290,145, "picture_23_4",null),
					new PictureItem(160,507, "picture_23_5",null),
					new PictureItem(-345,460, "picture_23_6",null)
			}),
			new LevelItem(1,"back5_full",-3,"list_4","mashinki2",new PictureItem[]{
					new PictureItem(-340,180, "picture_24_1","autobus"),
					new PictureItem(-200,420, "picture_24_2","vertolet"),
					new PictureItem(0,180, "picture_24_3","mashina"),
					new PictureItem(180,420, "picture_24_4","poezd"),
					new PictureItem(340,180, "picture_24_5","samoletik")
			}),
			new LevelItem("back13_full","list_4","cvety",new PictureItem[]{
					new PictureItem(-410,400, "picture_25_2","mak"),
					new PictureItem(-215,400,"picture_25_1" ,"kolokolchik"),
					new PictureItem(-10,400, "picture_25_3","vasilek"),
					new PictureItem(200,400, "picture_25_4","oduvanchik"),
					new PictureItem(410,400, "picture_25_5","romashka")
			}),
			new LevelItem("back1_full","handwashing_list","pomogi_mame_povesit_belie",new PictureItem[]{
					new PictureItem(-211,143, "handwashing1",null),
					new PictureItem(0,191, "handwashing2",null),
					new PictureItem(206,136, "handwashing3",null),
					new PictureItem(-193,441, "handwashing4",null),
					new PictureItem(2,475, "handwashing5",null),
					new PictureItem(197,462, "handwashing6",null)
			}),
			new LevelItem("savana","list_4","kto_givet_v_garkix_stranax",new PictureItem[]{
					new PictureItem(359,141, "exotic1","lev2"),
					new PictureItem(58,154, "exotic2","verblyd"),
					new PictureItem(-219,162, "exotic3","kengyry"),
					new PictureItem(-386,380, "exotic4","giraf2"),
					new PictureItem(-59,455, "exotic5","begemot2"),
					new PictureItem(340,441, "exotic6","slon")
			}),
			new LevelItem(1,"bamboo_list","list_600x620","panda",new PictureItem[]{
					new PictureItem(-82, 64, "panda1",null),
					new PictureItem(148,58, "panda2",null),
					new PictureItem(30,143, "panda3",null),
					new PictureItem(-157,307, "panda4",null),
					new PictureItem(19,375, "panda5",null),
					new PictureItem(190,371, "panda6",null),
					new PictureItem(-178,428, "panda7",null),
					new PictureItem(66,511, "panda8",null)
			}),
		new LevelItem(3,"raduga_background",2,"raduga_list","davai_vmeste_soberem_radygy",new PictureItem[]{
				new PictureItem(600-600,72.5f,"raduga_krasnyj","krasnyi",R.drawable.kraska_krasnaya),
				new PictureItem(600-600,120.5f,"raduga_oranzhevyj","oranzhevyi",R.drawable.kraska_oranzhevaya),
				new PictureItem(600-600,165,"raduga_zheltyj","zheltyi",R.drawable.kraska_zheltaya),
				new PictureItem(600-600,210,"raduga_zelenyj","zelenyi",R.drawable.kraska_zelenaya),
				new PictureItem(600-600,256.5f,"raduga_goluboj","goluboi",R.drawable.kraska_golubaya),
				new PictureItem(600-600,304,"raduga_sinij","sinii",R.drawable.kraska_sinyaya),
				new PictureItem(600-600,354.5f,"raduga_fioletovyj","fioletovyi",R.drawable.kraska_malinovaya),
		}),
			/*new LevelItem("back13_full","rainbow_2","raduga",new PictureItem[]{
					new PictureItem(-308,477, "raduga_part1","krasnyi"),
					new PictureItem(-244,477, "raduga_part2","oranzhevyi"),
					new PictureItem(-186,477, "raduga_part3","zheltyi"),
					new PictureItem(-126,477, "raduga_part4","zelenyi"),
					new PictureItem(-64,477, "raduga_part5","goluboi"),
					new PictureItem(-3,477, "raduga_part6","sinii"),
					new PictureItem(53,477, "raduga_part7","fioletovyi")
			}),*/
		/*new LevelItem(5,"back21_full","applicacyya_cvetok_list","appl_cvetok",new PictureItem[]{
			
				new ApplicationPictureItem(95-95,531.5f,"applikaciya_cvetok_1",null,425,140),
				new ApplicationPictureItem(95-95,91.5f,"applikaciya_cvetok_3",null,130,133),
				new ApplicationPictureItem(93-95,93,"applikaciya_cvetok_2",null,425,460),
				new ApplicationPictureItem(89.5f-95,313,"applikaciya_cvetok_4",null,130,460),
				new ApplicationPictureItem(121.5f-95,335.5f,"applikaciya_cvetok_5",null,212,424),
				new ApplicationPictureItem(57.5f-95,252,"applikaciya_cvetok_6",null,49,424),
		}),*/
			new LevelItem("menu_background","list_4","mama_i_malysh",new PictureItem[]{
					new PictureItem(-450,160, "sobaka2","sobaka"),
					new PictureItem(-274,200, "cenok3","cenok"),
					new PictureItem(-100,160, "koshka","koshka"),
					new PictureItem(50,200, "kotenok","kotenok"),
					new PictureItem(250,160,"utka","utka2"),
					new PictureItem(450,210, "utenok","utenok"),
					new PictureItem(-450,470, "kurica2","kurica3"),
					new PictureItem(-260,510, "cyplenok","cyplenok"),
					new PictureItem(90,500, "svinja4","svanya"),
					new PictureItem(410,520, "porosenok","porosenok")
			}),
			new LevelItem(1,"back25_full",6,"list_4","sdelaem_risunok",new PictureItem[]{
					new PictureItem(-280,470, "malchik","malchik"),
					new PictureItem(-420,470, "devochka","devochka"),
					new PictureItem(50,350, "dom","dom"),
					new PictureItem(-310,100, "oblako","oblako"),
					new PictureItem(350,150, "solnyshko","solnyshko"),
					new PictureItem(380,490, "zabor","zabor"),
					new PictureItem(-350,265, "bozhja_korovka","bozhja_korovka")
			}),
			new LevelItem("back31_full","list_4","mebel_v_komnate",new PictureItem[]{
					new PictureItem(-370,180, "zerkalo","zerkalo"),
					new PictureItem(-305,450, "divan","divan"),
					new PictureItem(-135,180, "televizor","televizor"),
					new PictureItem(50,450, "krovat","krovat"),
					new PictureItem(135,180, "stol","stol"),
					new PictureItem(380,450, "kreslo","kreslo"),
					new PictureItem(380,180, "shkaf","shkaf")
			}),
			new LevelItem(1,"back6_full",-3,"list_4","razlozhi_odezhdu",new PictureItem[]{
					new PictureItem(-380,150, "futbolka","futbolka"),
					new PictureItem(-110,170, "kolgoty","kolgoty"),
					new PictureItem(170,150, "kurtka","kurtka"),
					new PictureItem(430,150, "nosok","nosok"),
					new PictureItem(-380,470, "sapog","sapog"),
					new PictureItem(-110,480, "shapka","shapka"),
					new PictureItem(170,470, "sharf","sharf"),
					new PictureItem(410,440, "shtany","shtany")
			}),
			new LevelItem("back2_full","list_4","profeccii",new PictureItem[]{
					new PictureItem(-380,160, "shveya","shveya"),
					new PictureItem(-110,160, "stolyar","stolyar"),
					new PictureItem(160,150, "moryak","moryak"),
					new PictureItem(410,160, "provadec","prodavec"),
					new PictureItem(-380,460, "vrach","vrach"),
					new PictureItem(-170,450, "povar","povar"),
					new PictureItem(20,450, "milicioner","milicioner"),
					new PictureItem(220,450, "pochtalon","pochtalon"),
					new PictureItem(430,450, "doyarka","doyarka")
			}),
		new LevelItem(1,"back18_full",-4,"list_4","ryby",new PictureItem[]{
					new PictureItem(-400,100, "kambala","kambala"),
					new PictureItem(-280,240, "karas","karas"),
					new PictureItem(-60,100, "karp","karp"),
					new PictureItem(50,240, "ryba_mech","ryba_mech"),
					new PictureItem(300,100, "som","som"),
					new PictureItem(400,220, "okun","okun"),
					new PictureItem(-420,420, "skat","skat"),
					new PictureItem(-210,540, "schuka","schuka"),
					new PictureItem(110,420, "reef_shark","reef_shark"),
					new PictureItem(450,460, "morskoi_konek","morskoi_konek")
			}),
			/*new LevelItem(5,"back21_full","applicaciya_korablik_list","vyrezhem_korablik",new PictureItem[]{
					new ApplicationPictureItem(324.5f-267,509.5f,"applicaciya_korablik1",null,100.5f,331,90),
					new ApplicationPictureItem(181-267,217.5f,"applicaciya_korablik2",null,87,49,26),
					new ApplicationPictureItem(531.5f-267,491.5f,"applicaciya_korablik3",null,438.5f,381,90),
					new ApplicationPictureItem(262-267,342,"applicaciya_korablik4",null,289,453.5f,90),
					new ApplicationPictureItem(176.5f-267,213.5f,"applicaciya_korablik5",null,425.5f,558.5f,90),
					new ApplicationPictureItem(189-267,334.5f,"applicaciya_korablik6",null,425.5f,185.5f),
					new ApplicationPictureItem(324-267,334.5f,"applicaciya_korablik6",null,426.5f,65.5f),
					new ApplicationPictureItem(84.5f-267,121.5f,"applicaciya_korablik8",null,267,193.5f),
					new ApplicationPictureItem(138.5f-267,151.5f,"applicaciya_korablik9",null,346,267.5f),
					new ApplicationPictureItem(0-267,79,"applicaciya_korablik10",null,279.5f,60),
			}),*/
		/*new MoveToSidesLevelItem("doska",18,"list_12","pravo_levo_verx_niz",new PictureItem[]{
				new PictureItem(0,0,"picture_18_8",new String[]{"luk","nalevo"},2),
				new PictureItem(0,0,"picture_18_5",new String[]{"perec","napravo"},2),
				new PictureItem(0,0,"picture_18_10",new String[]{"baklazhan","verx"},2),
				new PictureItem(0,0,"picture_18_3",new String[]{"redis","vniz"},2),
		}),*/
		new LevelItem(1,"back23_full",-4,"list_4","morskie_zhivotnye",new PictureItem[]{
					new PictureItem(-420,160, "dolphin","delphin"),
					new PictureItem(-190,150, "kit","kit"),
					new PictureItem(-130,280, "kasatka","kasatka"),
					new PictureItem(80,140, "korall","korall"),
					new PictureItem(350,130, "krab","crab"),
					new PictureItem(190,320, "krevetka","krevetka"),
					new PictureItem(-450,470, "meduza","meduza"),
					new PictureItem(-210,480, "morskaya_cherepaha","cherepaha"),
					new PictureItem(90,500, "osminog","osminog"),
					new PictureItem(400,500, "rak","rak")
			}),
			new LevelItem("back34_full","list_4","ekzot_frukty",new PictureItem[]{
					new PictureItem(-420,180, "ananas","ananas"),
					new PictureItem(-340,470, "banany","banan"),
					new PictureItem(110,140, "lime","lime"),
					new PictureItem(45,410, "limon","limon"),
					new PictureItem(365,150, "mango","mango"),
					new PictureItem(365,470, "persik","persik"),
					new PictureItem(-160,160, "vinograd","vinograd")
			}),
			new LevelItem("back6_full","list_4","posuda",new PictureItem[]{
					new PictureItem(-350,140, "chainik","chainik"),
					new PictureItem(0,140, "chashka","chashka"),
					new PictureItem(350,140, "kastrula","kastrulja"),
					new PictureItem(-337,450, "skovorodka","skovoroda"),
					new PictureItem(0,450, "tarelka","tarelka"),
					new PictureItem(350,450, "kuvshin","kuvshyn")
			}),
			new LevelItem("back5_full","list_12","sladosti",new PictureItem[]{
					new PictureItem(-420,140, "ekler","ekler"),
					new PictureItem(-190,280, "kolechko","kolechko"),
					new PictureItem(-30,100, "konfety","konfety"),
					new PictureItem(250,130, "korzinka","korzinochka"),
					new PictureItem(70,400, "ledenec","ledenec"),
					new PictureItem(480,140, "morozhenoe","morozhenoe"),
					new PictureItem(-120,520, "pechenie","pechenie"),
					new PictureItem(-420,480, "shokolad","shokolad"),
					new PictureItem(350,430, "tort","tort")
			}),
			new LevelItem("back6_full","list_12","soberem_igrushki",new PictureItem[]{
					new PictureItem(-420,140, "grabli","grabli"),
					new PictureItem(-244,440, "kukla","kukla"),
					new PictureItem(-260,140, "lopatka","lopatka"),
					new PictureItem(30,100, "mashinka","mashinka"),
					new PictureItem(280,160, "matryoshka","matreshka"),
					new PictureItem(470,140, "mjachik","mjachik"),
					new PictureItem(-420,440, "piramidka2","piramidka"),
					new PictureItem(0,300, "pogremushka","pogremushka"),
					new PictureItem(430,450, "ula","ula"),
					new PictureItem(150,470, "vederko","vederko")
			}),
			new LevelItem("back6_full","list_4","muz_instrumenty",new PictureItem[]{
					new PictureItem(-420,140, "skripka","skripka"),
					new PictureItem(370,450, "baraban","baraban"), ///------------------------------------------
					new PictureItem(100,280, "muz_treugolnik","mus_treugolnik"),
					new PictureItem(45,120, "fleita2","fleita"),
					new PictureItem(365,150, "garmon","garmon"),
					new PictureItem(-350,430, "gitara","gitara"),
					new PictureItem(-100,120, "balalaika","balalaika"),
					new PictureItem(-80,500, "truba","truba")
			}),
			new LevelItem(1,"begemot_background","list_860x620","begemot",new PictureItem[]{
					new PictureItem(200,89, "begemot1",null),
					new PictureItem(199,281, "begemot2",null),
					new PictureItem(-86,278, "begemot3",null),
					new PictureItem(-164,382, "begemot4",null),
					new PictureItem(-332,275, "begemot5",null),
					new PictureItem(-44,500, "begemot6",null),
					new PictureItem(61,506, "begemot7",null),
					new PictureItem(226,432, "begemot8",null),
					new PictureItem(329,502, "begemot9",null)
			}),
		new LevelItem("back21_full","list_4","bukvy",new PictureItem[]{
					new PictureItem(-450,125, 'А', 3),
					new PictureItem(-150,125, 'Б', 3),
					new PictureItem(150,125, 'В', 3),
					new PictureItem(450,125, 'Г', 3),
					new PictureItem(-310,307, 'Д', 3),
					new PictureItem(0,310, 'Е', 3),
					new PictureItem(300,310, 'Ё', 3),
					new PictureItem(-450,517, 'Ж', 3),
					new PictureItem(-150,517, 'З', 3),
					new PictureItem(150,495, 'И', 3),
					new PictureItem(450,495, 'Й', 3)
			}),
		new LevelItem(1,"back20_full",18,"list_4","bukvy",new PictureItem[]{
					new PictureItem(-450,110, 'К', 3),
					new PictureItem(-150,110, 'Л', 3),
					new PictureItem(150,110, 'М', 3),
					new PictureItem(440,110, 'Н', 3),
					new PictureItem(-300,310, 'О', 3),
					new PictureItem(-14,310, 'П', 3),
					new PictureItem(300,310, 'Р', 3),
					new PictureItem(-450,512, 'С', 3),
					new PictureItem(-150,512, 'Т', 3),
					new PictureItem(150,505, 'У', 3),
					new PictureItem(425,505, 'Ф', 3)
			}),
			new LevelItem(1,"back17_full",5,"list_4","bukvy",new PictureItem[]{
					new PictureItem(-450,110, 'Х',3),
					new PictureItem(-150,110, 'Ц', 3),
					new PictureItem(150,110, 'Ч', 3),
					new PictureItem(430,110, 'Ш', 3),
					new PictureItem(-300,310, 'Щ', 3),
					new PictureItem(0,310, 'Ъ', 3),
					new PictureItem(300,310, 'Ы', 3),
					new PictureItem(-450,512, 'Ь', 3),
					new PictureItem(-150,512, 'Э', 3),
					new PictureItem(150,512, 'Ю', 3),
					new PictureItem(450,512, 'Я', 3)
			}),
			new LevelItem("back6_full","list_4","mishki",new PictureItem[]{
					new PictureItem(-450,120, "mishka1",null),
					new PictureItem(-150,110, "mishka2",null),
					new PictureItem(170,100, "mishka3",null),
					new PictureItem(420,165, "mishka4",null),
					new PictureItem(-260,310, "mishka5",null),
					new PictureItem(-50,330, "mishka6",null),
					new PictureItem(230,290, "mishka11",null),
					new PictureItem(-450,460, "mishka7",null),
					new PictureItem(-150,530, "mishka9",null),
					new PictureItem(150,505, "mishka8",null),
					new PictureItem(390,460, "mishka10",null)
			}),
			new LevelItem("back34_full","list_4","pirozhnye",new PictureItem[]{
					new PictureItem(-290,160, "pirozhnoe1",null),
					new PictureItem(-290,470, "pirozhnoe2",null),
					new PictureItem(30,160, "pirozhnoe3",null),
					new PictureItem(30,470, "pirozhnoe4",null),
					new PictureItem(350,160, "pirozhnoe5",null),
					new PictureItem(350,470, "pirozhnoe6",null)
			}),
		new WordLevelItem("back20_full",0,"list_12","babyshka","БАБУШКА"),
			new LevelItem(1,"back4_full",-3,"list_4","etiketki",new PictureItem[]{
					new PictureItem(-30,350, "price1",null),
					new PictureItem(330,310, "price3",null),
					new PictureItem(-150,150, "price2",null),
					new PictureItem(210,180, "price9",null),
					new PictureItem(-260,310, "price5",null),
					new PictureItem(460,310, "price6",null),
					new PictureItem(-400,310, "price7",null),
					new PictureItem(90,310, "price8",null),
					new PictureItem(-150,450, "price4",null),
					new PictureItem(210,490, "price10",null)
			}),
			new LevelItem(1,"back8_full",-3,"list_4","instrumenty",new PictureItem[]{
					new PictureItem(40,260, "drel","drel"),
					new PictureItem(-360,230, "molotok","molotok"),
					new PictureItem(290,220, "napilnik","napilnik"),
					new PictureItem(420,210, "pasanizhy","pasanizhy"),
					new PictureItem(-220,290, "nozhnicy","nozhnicy"),
					new PictureItem(-50,240, "ruletka","ruletka"),
					new PictureItem(-460,270, "razvodnoi_kluch","razvodnoi_kluch"),
					new PictureItem(420,410, "otvertka","otvertka"),
					new PictureItem(-220,540, "shtangelcirkul","shtangelcirkul"),
					new PictureItem(180,550, "pila","pila")
			}),
			new LevelItem(1,"back35_full",-2,"list_4","naidi_snezhinki",new PictureItem[]{
					new PictureItem(-410,218, "snezhinka8",null),
					new PictureItem(-120,160, "snezhinka1",null),
					new PictureItem(170,120, "snezhinka5",null),
					new PictureItem(430,115, "snezhinka6",null),
					new PictureItem(70,315, "snezhinka9",null),
					new PictureItem(400,360, "snezhinka4",null),
					new PictureItem(-380,500, "snezhinka3",null),
					new PictureItem(-130,464, "snezhinka7",null),
					new PictureItem(160,500, "snezhinka2",null)
			}),
			new LevelItem(1,"back34_full",-1,"prihozhaya_list","prihozhaya",new PictureItem[]{
					new PictureItem(134.5f-418,62.5f, "prihozhaya1",null),
					new PictureItem(316.5f-418,62f, "prihozhaya2",null),
					new PictureItem(682.5f-418,311.5f, "prihozhaya3",null),
					new PictureItem(490.5f-418,268.5f, "prihozhaya4",null),
					new PictureItem(533f-418,416.5f, "prihozhaya5",null),
					new PictureItem(140f-418,401.5f, "prihozhaya6",null),
					new PictureItem(141f-418,266f, "prihozhaya7",null),
					new PictureItem(372.5f-418,418.5f, "prihozhaya8",null),
					new PictureItem(304.5f-418,295f, "prihozhaya9",null),
					new PictureItem(686f-418,70.5f, "prihozhaya10",null),
					new PictureItem(498.5f-418,68f, "prihozhaya11",null),
			}),

			new LevelItem(1,"smeshariki",10,"list_4","smeshariki",new PictureItem[]{
					new PictureItem(-400,176, "krosh","krosh"),
					new PictureItem(-200,214, "karkarych","karkarych"),
					new PictureItem( 0,283, "pin","pin"),
					new PictureItem(200,173, "njusha","nusha"),
					new PictureItem(200,352, "bibi","bibi"),
					new PictureItem(400,252, "losjash","losjash"),
					new PictureItem(-390,455, "ezhik","ezhik"),
					new PictureItem( -150,490, "sovunia","sovunia"),
					new PictureItem( 100,500, "kopatych","kopatych"),
					new PictureItem( 400,500, "barash","barash")

			}),
		new WordLevelItem("back26_full",0,"list_12","dedyshka","ДЕДУШКА"),
			new LevelItem(1,"olympic_rings",18,"list_4","kakim_sportom_zanimautsa",new PictureItem[]{
					new PictureItem(-365,150, "football","fooball"),
					new PictureItem(-220,460, "basketball","basketball"),
					new PictureItem(-60,150, "box","box"),
					new PictureItem(100,490, "plavanie","plavanje"),
					new PictureItem(265,150, "hockey","hockey"),
					new PictureItem(400,460, "lyzhi","lyzhi")
			}),
			new LevelItem(1,"back15_full",-2,"list_2","ukras_elochku",new PictureItem[]{
					new PictureItem(100,300, "picture_2_1",null),
					new PictureItem(-100,240, "picture_2_2",null),
					new PictureItem(-20,400, "picture_2_3",null),
					new PictureItem(10,145, "picture_2_4",null),
					new PictureItem(120, 470, "picture_2_5",null),
					new PictureItem(-150,490, "picture_2_6",null),
					new PictureItem(300,475, "picture_2_7",null)
			}),
			new LevelItem(1,"zima_luna",3,"list_13","slozhi_snegovika",new PictureItem[]{
					new PictureItem(-37,82, "picture_3_1",null),
					new PictureItem(-23,180, "picture_3_2",null),
					new PictureItem(-19,362, "picture_3_3",null),
					new PictureItem(197,254, "picture_3_4",null),
					new PictureItem(-209,326, "picture_3_5",null),
					new PictureItem(-1,350, "picture_3_6",null),
					new PictureItem(-1,470, "picture_3_7",null),
					new PictureItem(-157,562, "picture_3_8",null),
					new PictureItem(123,554, "picture_3_9",null)
			}),
			new LevelItem(1,"fixiki_background",19,"children_list","fixiki_kak_zovyt",new PictureItem[]{
					new PictureItem(-375,162, "fixiki_igrek", "fixiki_igrek"),
					new PictureItem(-230,168, "fixiki_fire", "fixiki_fier"),
					new PictureItem(3,230, "fixiki_nolik", "fixiki_nolik"),
					new PictureItem(170,185, "fixiki_simka", "fixiki_simka"),
					new PictureItem(380,170, "fixiki_shpulya", "fixiki_shpulya"),
					new PictureItem(-315,467, "fixiki_papus", "fixiki_papus"),
					new PictureItem( -90,460, "fixiki_masya", "fixiki_masya"),
					new PictureItem( 102,456, "fixiki_verta", "fixiki_verta"),
					new PictureItem(340,462, "fixiki_dedus", "fixiki_dedus")

			}),
			new LevelItem(1,"barboskiny_background",7,"children_list","nazovi_barboskinyh",new PictureItem[]{
					new PictureItem(-425,381, "barboskiny1", "dryzgok"),
					new PictureItem(-267,370, "barboskiny5", "dedushka"),
					new PictureItem(-157,420, "barboskiny3", "malish"),
					new PictureItem( -18,400, "barboskiny_liza", "liza"),
					new PictureItem(78,388, "barboskiny_papa", "papa_2"),
					new PictureItem( 192,386, "barboskiny_mama", "mama_2"),
					new PictureItem(317,388, "barboskiny4", "roza"),
					new PictureItem(450,404, "barboskiny2", "gena")
			}),
			/*new LevelItem("domik_background","domik_list","soberi_domik",new PictureItem[]{
					new PictureItem(-173, 439, "domik1",null),
					new PictureItem(-157,431, "domik2",null),
					new PictureItem(31,427, "domik3",null),
					new PictureItem(32,444, "domik4",null),
					new PictureItem(-188,236, "domik5",null),
					new PictureItem(-97,96, "domik6",null),
					new PictureItem(42,225, "domik7",null)
			}),*/
			new LevelItem(1,"korablik_background",17,"list_4","korablik",new PictureItem[]{
					new PictureItem(117,517, "korablik1","korma"),
					new PictureItem(97,293, "korablik2","machta"),
					new PictureItem(241,502, "korablik3","spasatelnyi_kryg"),
					new PictureItem(220,283, "korablik4","parus"),
					new PictureItem(-21,320, "korablik5","parus"),
					new PictureItem(45,59, "korablik6","flag")
			}),
			new LevelItem("children_background","children_list","otkuda_eti_deti",new PictureItem[]{
					new PictureItem(-338,140, "child_australia","child_australia"),
					new PictureItem(-113,140, "child_canada","child_canada"),
					new PictureItem(108,140, "child_china","child_china"),
					new PictureItem(338,140, "child_france","child_france"),
					new PictureItem(-450,310, "child_greenland","child_greenland"),
					new PictureItem(-225,310, "child_hawaii","child_hawaii"),
					new PictureItem(-10,310, "child_mexico","child_mexico"),
					new PictureItem(225,310, "child_japan","child_japan"),
					new PictureItem(450,310, "child_kenia","child_keniya"),
					new PictureItem(-338,480, "child_indie","child_india"),
					new PictureItem(-113,480, "child_peru","child_peru"),
					new PictureItem(113,480, "child_russia","child_russia"),
					new PictureItem(338,480, "child_usa","child_usa")
			}),

			new LevelItem(1,"bottles_background",0,"bottles_list","bytilka",new PictureItem[]{
					new PictureItem(-425,443, "bottle1",null),
					new PictureItem(-320,246, "bottle2",null),
					new PictureItem(-250,489, "bottle3",null),
					new PictureItem(-130,323, "bottle4",null),
					new PictureItem(-13,478, "bottle5",null),
					new PictureItem(62,280, "bottle6",null),
					new PictureItem(156,474, "bottle7",null),
					new PictureItem(256,242, "bottle8",null),
					new PictureItem(354,496, "bottle9",null),
					new PictureItem(434,288, "bottle10",null)
			}),
			new LevelItem(1,"luntik_background",0,"list_4","vspomni_geroe_vmyltfilma",new PictureItem[]{
					new PictureItem(-425,140, "pchelenok","pchelenok"),
					new PictureItem(-250,180, "general_sher","general_sher"),
					new PictureItem(-50,140, "mila","mila"),
					new PictureItem(180,160, "kapa","kapa"),
					new PictureItem(415,170, "pauk_shnuk","pauk_shnuk"),
					new PictureItem(-380,420, "tetya_motya","tetya_motya"),
					new PictureItem(-230,510, "kornei_ivanovich","kornei_ivanovich"),
					new PictureItem(-100,400, "kuzya","kuzya"),
					new PictureItem(70,460, "luntik","luntik"),
					new PictureItem(240,450, "piyavka","piyavka"),
					new PictureItem(390,460, "gusenicy","gusenicy")
			}),
		/*new LevelItem(5,"back21_full","applicaciya_bozhiya_korovka_list","appl_bogyakorovka",new PictureItem[]{
				new ApplicationPictureItem(-40,295,"applicaciya_bozhiya_korovka1",null,195f,138,107),
				new ApplicationPictureItem(-103,336,"applicaciya_bozhiya_korovka3",null,103,438,4),
				new ApplicationPictureItem(104,334,"applicaciya_bozhiya_korovka2",null,290,436,-5),
				new ApplicationPictureItem(-60,259,"applicaciya_bozhiya_korovka4",null,244,227),
				new ApplicationPictureItem(-122,332,"applicaciya_bozhiya_korovka4",null,404,226),
				new ApplicationPictureItem(60,259,"applicaciya_bozhiya_korovka4",null,324,173),
				new ApplicationPictureItem(122,332,"applicaciya_bozhiya_korovka4",null,405,112),
				new ApplicationPictureItem(-74,424,"applicaciya_bozhiya_korovka4",null,476,173),
				new ApplicationPictureItem(74,424,"applicaciya_bozhiya_korovka4",null,476,43),
				new ApplicationPictureItem(-44,162,"applicaciya_bozhiya_korovka5",null,455,372),
				new ApplicationPictureItem(44,162,"applicaciya_bozhiya_korovka5",null,455,522),
		}),*/
			new LevelItem("back2_full","platie0","platiya",new PictureItem[]{
					new PictureItem(-398,185, "platie12",null),
					new PictureItem(-227,150, "platie9",null),
					new PictureItem(-50,187, "platie11",null),
					new PictureItem(258,192, "platie10",null),
					new PictureItem(440,188, "platie6",null),
					new PictureItem(117,328, "platie8",null),
					new PictureItem(-425,490, "platie7",null),
					new PictureItem(-222,478, "platie3",null),
					new PictureItem(-56,478, "platie5",null),
					new PictureItem(264,494, "platie4",null),
					new PictureItem(440,490, "platie2",null)
			}),
			new LevelItem(1,"savana","list_600x620","lev",new PictureItem[]{
					new PictureItem(-38,99, "lev1",null),
					new PictureItem(-40,269, "lev2",null),
					new PictureItem(-204,276, "lev3",null),
					new PictureItem(121,291, "lev4",null),
					new PictureItem(-60,426, "lev5",null),
					new PictureItem(-184,521, "lev6",null),
					new PictureItem(-61,516, "lev7",null),
					new PictureItem(62,539, "lev8",null),
					new PictureItem(162,431, "lev9",null),
					new PictureItem(256,319, "lev10",null)
			}),
			new LevelItem(1,"ocean_background",-4,"ocean_list","rasstav_korabliki",new PictureItem[]{
					new PictureItem(115,400, "boat1",null),
					new PictureItem(-368,130, "boat2",null),
					new PictureItem(-85,244, "boat3",null),
					new PictureItem(373,472, "boat4",null),
					new PictureItem(164,141, "boat5",null),
					new PictureItem(-392,377, "boat6",null),
					new PictureItem(-175,501, "boat7",null),
					new PictureItem(383,265, "boat8",null)
			}),
			new LevelItem("trava_background","list_4","nazvania_ptihc",new PictureItem[]{
					new PictureItem(-326,441, "pticy1","pavlin"),
					new PictureItem(444,473, "pticy2","popygai"),
					new PictureItem(238,471, "pticy3","ydot"),
					new PictureItem(367,178, "pticy4","lebed"),
					new PictureItem(22,493, "pticy5","sinichka"),
					new PictureItem(-168,159, "pticy6","indyk"),
					new PictureItem(119,203, "pticy7","starays"),
					new PictureItem(-433,134, "pticy8","petyx")
			}),
		new LevelItem(3,"back21_full",-3,"karandashi_list","kakoi_cvet_y_karandasha",new PictureItem[]{
				new PictureItem(104-600,152,"karandash_goluboi","goluboi",R.drawable.kraska_golubaya),
				new PictureItem(100-600,354,"karandash_seryj","seryi",R.drawable.kraska_seraya),
				new PictureItem(303-600,152,"karandash_krasnyj","krasnyi",R.drawable.kraska_krasnaya),
				new PictureItem(293-600,354,"karandash_chernyj","chernyi",R.drawable.kraska_chernaya),
				new PictureItem(503-600,152,"karandash_oranzhevyj","oranzhevyi",R.drawable.kraska_oranzhevaya),
				new PictureItem(491-600,354,"karandash_belyj","belyi",R.drawable.kraska_belaya),
				new PictureItem(702-600,152,"karandash_korichnevyj","korichnevyi",R.drawable.kraska_korichnevaya),
				new PictureItem(690-600,354,"karandash_malinovyj","malinovyi",R.drawable.kraska_malinovaya),
				new PictureItem(901-600,152,"karandash_zelenyj","zelenyi",R.drawable.kraska_zelenaya),
				new PictureItem(890-600,354,"karandash_sinij","sinii",R.drawable.kraska_sinyaya),
				new PictureItem(1099-600,152,"karandash_salatovyj","salatoavyi",R.drawable.kraska_salatovaya),
				new PictureItem(1088-600,354,"karandash_zheltyj","zheltyi",R.drawable.kraska_zheltaya),
		}),

	};

        float CenterX=0;
        float Top=0;
		float Left=0;
    	float screen_factor=1f;
    	float bmp_factor=1f;
        
	String GoodBoy[]={"molodec",
        		"otlichno",
        		"velikolepno",
        		"ya_toboj_gorzhus",
        		"prevoshodno",
        		"blestjasche",
        		"chudesno",
        };

	String GoodStep[]={"da",
        		"pravilno",
        		"umnica",
        		"horosho",
        		"prekrasno",
        };

        ImageView GameButtonMoon=null;
        ImageView GameButtonFlower=null;
        ImageView GameButtonSunshine=null;
        ImageView GameButtonMoonWhite=null;
        ImageView GameButtonSunshineWhite=null;

        ImageView SettingsButton=null;

        boolean isStartingLevel=true;

		private void InitGameScreen()
		{
			StartLevel();
		}
		
        public void InitThread(SharedPreferences settings)
        {
            BackgroundType=GameSettings.getInt(Owner.getString(R.string.background_key), 3);
            BukvyType=GameSettings.getInt(Owner.getString(R.string.bukvy_key), 0);
            VibrationType=GameSettings.getBoolean(Owner.getString(R.string.vibration_key), true);
			isStars=GameSettings.getBoolean(Owner.getString(R.string.stars_key), Owner.getViewHeight()>600);
			isAnimateBackground=GameSettings.getBoolean(Owner.getString(R.string.animate_background_key), Owner.getViewHeight()>600);
        }

        public void onDestroy() {
        	mRun=false;
        	RecycleBitmaps();
        }
        
        private void RecycleBitmap(View image)
        {
        	if(image!=null)
        	{
        		image.clearAnimation();
        		this.removeView(image);
        	}
	        image=null;
        }
        
        private void ClearSettings()
        {
        	RecycleBitmap(BukvyTypeSettings);
        	RecycleBitmap(VibrationSettings);
			RecycleBitmap(shtoraImageView);
			RecycleBitmap(AnimateBackgroundSettings);
			RecycleBitmap(AnimateBackgroundSettings2);
			RecycleBitmap(StarsSettings);
        }

        private void RecycleBitmaps()
        {
			this.post(new Runnable() {
				@Override
				public void run() {
					RecycleBitmap(ExitGameButton);
					RecycleBitmap(StartGameButton);
					RecycleBitmap(LevelGameButton);

					RecycleBitmap(GameButtonMoon);
					RecycleBitmap(GameButtonFlower);
					RecycleBitmap(GameButtonSunshine);
					RecycleBitmap(GameButtonMoonWhite);
					RecycleBitmap(GameButtonSunshineWhite);
					RecycleBitmap(SettingsButton);

					ClearSettings();

					RecycleBitmap(ListBackground);

					for(int i=0;i<Levels.length;i++)
					{
						RecycleBitmap(Levels[i]);
					}
					if(ListObjects!=null) {
						for (int i = 0; i < ListObjects.length; i++) {
							RecycleBitmap(ListObjects[i]);
							RecycleBitmap(ListClearObjects[i]);
						}
					}

				}
			});
        }

         private void NextLevel()
        {
			current_level_num++;
			if(current_level_num>=levels.length)
				current_level_num=0;
			StartLevel();
        }
        
        private void PrevLevel()
        {
			current_level_num--;
			if(current_level_num<0)
				current_level_num=levels.length-1;
			StartLevel();
        }

        private MovingImageView newDecodeResourceWithSize(String bmpName,float factor)
        {
        	MovingImageView image=new MovingImageView(Owner,this);
        	this.addView(image);
        	Bitmap tmp=getImageFromObb(bmpName, ".png");
        	if(factor>=1f)
        	{
        		image.setImageBitmap(tmp,factor);
        		return image;
        	}
        	Bitmap res=Bitmap.createScaledBitmap(tmp, (int)(tmp.getWidth()*(isScaledBitmaps()?2:1)*factor), (int)(tmp.getHeight()*(isScaledBitmaps()?2:1)*factor), false);
        	tmp.recycle();
        	image.setImageBitmap(res,1);
        	return image;
        }

	private RotateImageView newDecodeResourceWithSize2(String bmpName,float factor)
	{
		RotateImageView image=new RotateImageView(Owner,this);
		this.addView(image);
		Bitmap tmp=getImageFromObb(bmpName, ".png");
		if(factor>=1f)
		{
			image.setImageBitmap(tmp,factor);
			return image;
		}
		Bitmap res=Bitmap.createScaledBitmap(tmp, (int)(tmp.getWidth()*factor), (int)(tmp.getHeight()*factor), false);
		tmp.recycle();
		image.setImageBitmap(res,1);
		return image;
	}

	private DrawImageView newDecodeResourceWithSize3(String bmpName,float factor)
	{
		DrawImageView image=new DrawImageView(Owner,this);
		this.addView(image);
		Bitmap tmp=getImageFromObb(bmpName, ".png");
		if(factor==1f)
		{
			image.setImageBitmap(tmp);
			return image;
		}
		Bitmap res=Bitmap.createScaledBitmap(tmp, (int)(tmp.getWidth()*factor), (int)(tmp.getHeight()*factor), false);
		tmp.recycle();
		image.setImageBitmap(res,Owner.getViewHeight()/10f);
		return image;
	}

	private void ClearLevel()
        {
			if(showStarsThread!=null)
				showStarsThread.interrupt();
			showStarsThread=null;
        	ClearMainMenu();
        	RecycleBitmap(GlassView);
        	//RecycleBitmap(GameFrame);
        	RecycleBitmap(GameButtonMoon);
        	RecycleBitmap(GameButtonFlower);
        	RecycleBitmap(GameButtonSunshine);
			if(ListObjects!=null)
        		for(int i=0;i<ListObjects.length;i++) {
					RecycleBitmap(ListObjects[i]);
					ListObjects[i] = null;
					//RecycleBitmap(ListShineObjects[i]);
					//ListObjectsEmpty[i]=false;
				}
			if(ListClearObjects!=null)
				for(int i=0;i<ListClearObjects.length;i++) {
					RecycleBitmap(ListClearObjects[i]);
				}
			ListClearObjects=null;
			if(ListAnimatedObjects!=null)
				for(int i=0;i<ListAnimatedObjects.length;i++) {
					if(ListAnimatedObjects[i]!=null) {
						ListAnimatedObjects[i].clearAnimation();
						RecycleBitmap(ListAnimatedObjects[i]);
						AnimationLayout.removeView(ListAnimatedObjects[i]);
					}
				}
			ListAnimatedObjects=null;
       		RecycleBitmap(ListBackground);
			if(AnimationLayout!=null)
				removeView(AnimationLayout);
			AnimationLayout=null;
        }

	int[] flakes=null;
	int[] flakes2=null;
	Point maxSize;
	int sleepTimes=1000;
	public void setSnowflake(final AnimatedImageView imageView, int startOffset)
	{
		float iMul = mRnd.nextFloat()/2f+0.5f;
		int objectNum = mRnd.nextInt(flakes.length);
		int posX=mRnd.nextInt(Owner.getViewWidth());
		//final ImageView imageView=new ImageView(Owner);
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		setBounds(imageView,
				posX-maxSize.x*iMul/2,
				-maxSize.y*iMul,
				posX+maxSize.x*iMul/2,
				0);
		imageView.setImageResource(flakes[objectNum]);
		Animation animationRotate=new RotateAnimation(0,360,maxSize.x*iMul/2,maxSize.y*iMul/2);
		Animation animationTranslate=new TranslateAnimation(0,0,0,Owner.getViewHeight()+maxSize.y*iMul*2);
		animationTranslate.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//AnimationLayout.removeView(imageView);
				if(imageView.getAnimation()!=null)
					setSnowflake(imageView,mRnd.nextInt(2000));
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(animationRotate);
		animationSet.addAnimation(animationTranslate);
		animationSet.setDuration(mRnd.nextInt(6000)+2000);
		animationSet.setStartOffset(startOffset);
		imageView.startAnimation(animationSet);
	}

	public void setSnowflake3D(final AnimatedImageView imageView, int startOffset)
	{
		float iMul = mRnd.nextFloat()/3f+0.67f;
		int objectNum = mRnd.nextInt(flakes.length);
		int posX=mRnd.nextInt(Owner.getViewWidth());
		//final ImageView imageView=new ImageView(Owner);
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		setBounds(imageView,
				posX-maxSize.x*iMul/2,
				-maxSize.y*iMul,
				posX+maxSize.x*iMul/2,
				0);
		imageView.setImageResource(flakes[objectNum]);
		TypedValue outValue = new TypedValue();
		Resources.Theme currentTheme = Owner.getTheme();
		currentTheme.resolveAttribute(R.attr.actionBarWidgetTheme,
				outValue, true);
		final int targetThemeRes = outValue.resourceId;

		Animation animation3DRotate=new Rotate3DAnimation(new ContextThemeWrapper(Owner, targetThemeRes),0f,360f,maxSize.x*iMul/2,maxSize.y*iMul/2,mRnd.nextInt(360),false);
		Animation animationRotate=new RotateAnimation(0f,360f,maxSize.x*iMul/2,maxSize.y*iMul/2);
		Animation animationTranslate=new TranslateAnimation(0,0,0,Owner.getViewHeight()+maxSize.y*iMul*2);
		animationTranslate.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//AnimationLayout.removeView(imageView);
				if(imageView.getAnimation()!=null)
					setSnowflake3D(imageView,mRnd.nextInt(2000));
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(animation3DRotate);
		animationSet.addAnimation(animationRotate);
		animationSet.addAnimation(animationTranslate);
		animationSet.setDuration(mRnd.nextInt(2000)+6000);
		animationSet.setStartOffset(startOffset);
		imageView.startAnimation(animationSet);
	}

	public void setPyatno(final AnimatedImageView imageView)
	{
		final RelativeLayout.LayoutParams layoutParams= (LayoutParams) imageView.getLayoutParams();
		final int imageLeft=layoutParams.leftMargin;//imageView.getLeft();
		final int imageTop=layoutParams.topMargin;//imageView.getTop();
		final int posX=mRnd.nextInt((int) (Owner.getViewWidth()-layoutParams.width));
		final int posY=mRnd.nextInt((int) (Owner.getViewHeight()-layoutParams.height));
		//final ImageView imageView=new ImageView(Owner);
		Animation animationTranslate=new TranslateAnimation(0,posX-imageLeft,0,posY-imageTop);
		animationTranslate.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//AnimationLayout.removeView(imageView);
				if(imageView.getAnimation()!=null) {
					setBounds(imageView, posX, posY, posX + layoutParams.width, posY + layoutParams.height);
					setPyatno(imageView);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		float size= (float) Math.sqrt((imageLeft-posX)*(imageLeft-posX)+(imageTop-posY)*(imageTop-posY));
		animationTranslate.setDuration((long) (1200*(size/620)));
		imageView.startAnimation(animationTranslate);
	}

	public void setOblako(final AnimatedImageView imageView)
	{
		int left=imageView.getCentralPoint().x;
		int top=imageView.getCentralPoint().y;
		final int posX=(int) (left+(mRnd.nextInt(80)-40)*bmp_factor);
		final int posY=(int) (top+(mRnd.nextInt(20)-10)*bmp_factor);
		RelativeLayout.LayoutParams layoutParams= (LayoutParams) imageView.getLayoutParams();
		final int imageLeft=layoutParams.leftMargin;//imageView.getLeft();
		final int imageTop=layoutParams.topMargin;//imageView.getTop();
		//final ImageView imageView=new ImageView(Owner);
		Animation animationTranslate=new TranslateAnimation(0,posX-imageLeft,0,posY-imageTop);
		animationTranslate.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//AnimationLayout.removeView(imageView);
				if(imageView.getAnimation()!=null) {
					setBounds(imageView, posX, posY, posX + imageView.getWidth(), posY + imageView.getHeight());
					setOblako(imageView);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		float size= (float) Math.sqrt((imageLeft-posX)*(imageLeft-posX)+(imageTop-posY)*(imageTop-posY));
		animationTranslate.setDuration(mRnd.nextInt(2000)+2000);
		imageView.startAnimation(animationTranslate);
	}

	public void setPerekatiPole(final AnimatedImageView imageView, int startOffset, final int top, final int bottom)
	{
		float iMul = mRnd.nextFloat()/2f+0.5f;
		int objectNum = mRnd.nextInt(flakes.length);
		int posY=top+mRnd.nextInt(bottom-top);
		//final ImageView imageView=new ImageView(Owner);
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		setBounds(imageView,
				-maxSize.x*iMul,
				posY,
				0,
				posY+maxSize.y*iMul);
		imageView.setImageResource(flakes[objectNum]);
		Animation animationRotate=new RotateAnimation(0,360,maxSize.x*iMul/2,maxSize.y*iMul/2);
		Animation animationTranslate=new TranslateAnimation(0,Owner.getViewWidth()+maxSize.x*iMul*2,0,0);
		animationTranslate.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//AnimationLayout.removeView(imageView);
				if(imageView.getAnimation()!=null)
					setPerekatiPole(imageView,mRnd.nextInt(2000),top,bottom);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(animationRotate);
		animationSet.addAnimation(animationTranslate);
		animationSet.setDuration(mRnd.nextInt(6000)+2000);
		animationSet.setStartOffset(startOffset);
		imageView.startAnimation(animationSet);
	}

	PointF getPointByAroundLength(int startPoint)
	{
		if(startPoint<Owner.getViewWidth())
		{
			return new PointF(startPoint,-maxSize.y/2f);
		}
		else if(startPoint<Owner.getViewWidth()+Owner.getViewHeight())
		{
			return new PointF(Owner.getViewWidth()+maxSize.x/2f,startPoint-Owner.getViewWidth());
		}
		else if(startPoint<Owner.getViewWidth()+Owner.getViewHeight()+Owner.getViewWidth())
		{
			return new PointF(startPoint-Owner.getViewWidth()-Owner.getViewHeight(),Owner.getViewHeight()+maxSize.y/2f);
		}
		return new PointF(-maxSize.x/2f,startPoint-Owner.getViewWidth()-Owner.getViewHeight()-Owner.getViewWidth());
	}
	public void setBabochkaPath(final AnimatedImageView imageView)
	{
		/*imageView.post(new Runnable() {
			@Override
			public void run() {*/
				//imageView.clearAnimation();
				imageView.setBackgroundResource(flakes[mRnd.nextInt(flakes.length)]);
				AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
				babochkaAnimation.start();
				int summaryAroundLength=Owner.getViewHeight()*2+Owner.getViewWidth()*2;
				PointF startPoint=getPointByAroundLength(mRnd.nextInt(summaryAroundLength));
				PointF endPoint;
				do {
					endPoint=getPointByAroundLength(mRnd.nextInt(summaryAroundLength));
				} while(Math.abs(endPoint.x-startPoint.x)<maxSize.x*2 || Math.abs(endPoint.y-startPoint.y)<maxSize.y*2);
				setBounds(imageView,
						startPoint.x-maxSize.x/2f,
						startPoint.y-maxSize.y/2f,
						startPoint.x+maxSize.x/2f,
						startPoint.y+maxSize.y/2f);
				float angle= (float) (Math.atan2(endPoint.y-startPoint.y,endPoint.x-startPoint.x)/ Math.PI * 180)+90;
				imageView.setRotation(angle);
				Animation animation=new TranslateAnimation(0,endPoint.x-startPoint.x,0,endPoint.y-startPoint.y);
				animation.setDuration((long) (5000f/Owner.getViewWidth()*Math.sqrt((endPoint.x-startPoint.x)*(endPoint.x-startPoint.x)+(endPoint.y-startPoint.y)*(endPoint.y-startPoint.y))));
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override public void onAnimationStart(Animation animation) {}
					@Override public void onAnimationRepeat(Animation animation) {}

					@Override
					public void onAnimationEnd(Animation animation) {
						if(imageView.getAnimation()!=null)
							setBabochkaPath(imageView);
					}
				});
				imageView.startAnimation(animation);
	}

	public void setRomashkaPath(final AnimatedImageView imageView)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageResource(flakes[mRnd.nextInt(flakes.length)]);
		int summaryAroundLength=Owner.getViewHeight()*2+Owner.getViewWidth()*2;
		PointF startPoint=getPointByAroundLength(mRnd.nextInt(summaryAroundLength));
		PointF endPoint;
		do {
			endPoint=getPointByAroundLength(mRnd.nextInt(summaryAroundLength));
		} while(Math.abs(endPoint.x-startPoint.x)<maxSize.x*2 || Math.abs(endPoint.y-startPoint.y)<maxSize.y*2);
		setBounds(imageView,
				startPoint.x-maxSize.x/2f,
				startPoint.y-maxSize.y/2f,
				startPoint.x+maxSize.x/2f,
				startPoint.y+maxSize.y/2f);
		Animation animation=new TranslateAnimation(0,endPoint.x-startPoint.x,0,endPoint.y-startPoint.y);
		Animation animationRotate=new RotateAnimation(0,360,maxSize.x/2f,maxSize.y/2f);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setRomashkaPath(imageView);
			}
		});
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(animationRotate);
		animationSet.addAnimation(animation);
		animationSet.setDuration((long) (5000f/Owner.getViewWidth()*Math.sqrt((endPoint.x-startPoint.x)*(endPoint.x-startPoint.x)+(endPoint.y-startPoint.y)*(endPoint.y-startPoint.y))));
		imageView.startAnimation(animationSet);
	}

	public void setRomashkaCurl(final AnimatedImageView imageView)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageResource(flakes[mRnd.nextInt(flakes.length)]);
		int posX=mRnd.nextInt(Owner.getViewWidth());
		int posY=mRnd.nextInt(Owner.getViewHeight());
		setBounds(imageView,
				posX-maxSize.x/2f,
				posY-maxSize.y/2f,
				posX+maxSize.x/2f,
				posY+maxSize.y/2f);
		Animation animationScale=new ScaleAnimation(0,1.2f,0,1.2f,maxSize.x/2f,maxSize.y/2f);
		Animation animationRotate=new RotateAnimation(0,359,maxSize.x/2f,maxSize.y/2f);
		Animation animationAlpha=new AlphaAnimation(1,0);
		animationRotate.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setRomashkaCurl(imageView);
			}
		});
		AnimationSet animationSet=new AnimationSet(true);
		animationSet.addAnimation(animationRotate);
		animationSet.addAnimation(animationScale);
		animationSet.addAnimation(animationAlpha);
		animationSet.setDuration(1500+mRnd.nextInt(1000));
		imageView.startAnimation(animationSet);
	}

	public void setRybkaPath(final AnimatedImageView imageView)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setBackgroundResource(flakes2[resNum]);
		imageView.setImageResource(flakes[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
		babochkaAnimation.start();
		int posY=mRnd.nextInt(Owner.getViewHeight()-maxSize.y);
		int direction=mRnd.nextInt(2);
		int startX=direction==1?-maxSize.x:Owner.getViewWidth()+maxSize.x;
		int endX=direction==1?Owner.getViewWidth()+maxSize.x:-maxSize.x;
		imageView.setHorizontalReverse(direction!=1);
		setBounds(imageView,
				startX-maxSize.x/2f,
				posY-maxSize.y/2f,
				startX+maxSize.x/2f,
				posY+maxSize.y/2f);
		Animation animation=new TranslateAnimation(0,endX-startX,0,0);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setRybkaPath(imageView);
			}
		});
		animation.setDuration(10000+mRnd.nextInt(6000));
		imageView.startAnimation(animation);
	}

	public void setSalut()
	{
		if(ListAnimatedObjects==null)
			return;
		int objectNum = mRnd.nextInt(flakes.length);
		int startOffset=mRnd.nextInt(2000)+1000;
		int posX=mRnd.nextInt(Owner.getViewWidth());
		int posY=mRnd.nextInt(Owner.getViewHeight());
		PointF[] points=new PointF[]{
				new PointF(67,66.5f),
				new PointF(149.5f,32.5f),
				new PointF(233,66.5f),
				new PointF(267.5f,149.5f),
				new PointF(233,233.5f),
				new PointF(149.5f,267.5f),
				new PointF(67,233.5f),
				new PointF(32.5f,149.5f),
		};

		final AnimatedImageView imageView=ListAnimatedObjects[0];
		for(int i=0;i<ListAnimatedObjects.length;i++) {
			//ListAnimatedObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
			setBounds(ListAnimatedObjects[i], posX - maxSize.x / 2, posY - maxSize.y / 2, posX + maxSize.x / 2, posY + maxSize.y / 2);
			ListAnimatedObjects[i].setImageResource(flakes[objectNum]);
			Animation animationTranslate = new TranslateAnimation(0, (points[i].x*4/3 - 200) * bmp_factor, 0, (points[i].y*4/3 - 200) * bmp_factor);
			if (i == ListAnimatedObjects.length - 1) {
				final int finalI = i;
				animationTranslate.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						//AnimationLayout.removeView(imageView);
						if(imageView!=null && imageView.getAnimation()!=null)
							setSalut();
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
			}
			animationTranslate.setDuration(2000);
			//animationTranslate.setStartOffset(startOffset);
			ListAnimatedObjects[i].startAnimation(animationTranslate);
		}
	}

	public void setSharikUp(final AnimatedImageView imageView,int startOffset)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setImageResource(flakes[resNum]);
		int posX1=mRnd.nextInt(Owner.getViewWidth());
		int posX2=posX1+mRnd.nextInt(Owner.getViewWidth()/5)-Owner.getViewWidth()/10;
		setBounds(imageView,
				posX1-maxSize.x/2f,
				Owner.getViewHeight(),
				posX1+maxSize.x/2f,
				Owner.getViewHeight()+maxSize.y);
		Animation animation=new TranslateAnimation(0,posX2-posX1,0,-Owner.getViewHeight()-maxSize.y*2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setSharikUp(imageView,0);
			}
		});
		animation.setDuration(3000+mRnd.nextInt(3000));
		animation.setStartOffset(startOffset);
		imageView.startAnimation(animation);
	}

	public void setVozdushnyjSharUp(final AnimatedImageView imageView,int startOffset)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setImageResource(flakes[resNum]);
		int posX1=mRnd.nextInt(Owner.getViewWidth());
		int posX2=posX1+mRnd.nextInt(Owner.getViewWidth()/3)-Owner.getViewWidth()/6;
		setBounds(imageView,
				posX1-maxSize.x/2f,
				Owner.getViewHeight(),
				posX1+maxSize.x/2f,
				Owner.getViewHeight()+maxSize.y);
		Animation animation=new TranslateAnimation(0,posX2-posX1,0,-Owner.getViewHeight()-maxSize.y*2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setVozdushnyjSharUp(imageView,0);
			}
		});
		animation.setDuration(6000+mRnd.nextInt(3000));
		animation.setStartOffset(startOffset);
		imageView.startAnimation(animation);
	}

	public void setVozdushnyjShar2Up(final AnimatedImageView imageView,int startOffset)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setBackgroundResource(flakes[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
		babochkaAnimation.start();
		int posX1=mRnd.nextInt(Owner.getViewWidth());
		int posX2=posX1+mRnd.nextInt(Owner.getViewWidth()/3)-Owner.getViewWidth()/6;
		setBounds(imageView,
				posX1-maxSize.x/2f,
				Owner.getViewHeight(),
				posX1+maxSize.x/2f,
				Owner.getViewHeight()+maxSize.y);
		Animation animation=new TranslateAnimation(0,posX2-posX1,0,-Owner.getViewHeight()-maxSize.y*2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setVozdushnyjShar2Up(imageView,0);
			}
		});
		animation.setDuration(6000+mRnd.nextInt(3000));
		animation.setStartOffset(startOffset);
		imageView.startAnimation(animation);
	}

	public void setMeduzaPath(final AnimatedImageView imageView)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setImageResource(flakes[resNum]);
		imageView.setBackgroundResource(flakes2[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
		babochkaAnimation.start();
		int posX1=mRnd.nextInt(Owner.getViewWidth());
		int posX2=posX1+mRnd.nextInt(Owner.getViewWidth()/3)-Owner.getViewWidth()/6;
		int direction=mRnd.nextInt(2);
		imageView.setHorizontalReverse(direction!=1);
		setBounds(imageView,
				posX1-maxSize.x/2f,
				Owner.getViewHeight(),
				posX1+maxSize.x/2f,
				Owner.getViewHeight()+maxSize.y);
		Animation animation=new TranslateAnimation(0,posX2-posX1,0,-Owner.getViewHeight()-maxSize.y*2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setMeduzaPath(imageView);
			}
		});
		animation.setDuration(10000+mRnd.nextInt(3000));
		imageView.startAnimation(animation);
	}

	public void setRaketaUp(final AnimatedImageView imageView,int startOffset)
	{
		//imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setImageResource(flakes[resNum]);
		int posX1=mRnd.nextInt(Owner.getViewWidth());
		int posX2=posX1+mRnd.nextInt(Owner.getViewWidth()/5)-Owner.getViewWidth()/10;
		setBounds(imageView,
				posX1-maxSize.x/2f,
				Owner.getViewHeight(),
				posX1+maxSize.x/2f,
				Owner.getViewHeight()+maxSize.y);
		Animation animation=new TranslateAnimation(0,posX2-posX1,0,-Owner.getViewHeight()-maxSize.y*2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setRaketaUp(imageView,0);
			}
		});
		animation.setDuration(3000+mRnd.nextInt(3000));
		animation.setStartOffset(startOffset);
		imageView.startAnimation(animation);
	}

	public void setParovozikPath(final AnimatedImageView imageView1,final AnimatedImageView imageView2)
	{
		int resNum=mRnd.nextInt(flakes.length);
		imageView1.setImageResource(flakes[resNum]);
		//imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView2.setBackgroundResource(flakes2[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView2.getBackground();
		babochkaAnimation.start();
		int posY=mRnd.nextInt(Owner.getViewHeight()-maxSize.y)+maxSize.y/2;
		int direction=mRnd.nextInt(2);
		int startX=direction==1?-maxSize.x:Owner.getViewWidth()+maxSize.x;
		int endX=direction==1?Owner.getViewWidth()+maxSize.x:-maxSize.x;
		imageView1.setHorizontalReverse(direction!=1);
		imageView2.setHorizontalReverse(direction!=1);
		setBounds(imageView1,
				startX-maxSize.x/2f,
				posY-maxSize.y/2f,
				startX+maxSize.x/2f,
				posY+maxSize.y/2f);
		setBounds(imageView2,
				startX-maxSize.x/2f,
				posY-maxSize.y/2f,
				startX+maxSize.x/2f,
				posY+maxSize.y/2f);
		Animation animation=new TranslateAnimation(0,endX-startX,0,0);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView1.getAnimation()!=null)
					setParovozikPath(imageView1,imageView2);
			}
		});
		animation.setDuration(6000+mRnd.nextInt(2000));
		imageView1.startAnimation(animation);
		imageView2.startAnimation(animation);
	}

	public void setKometaPath(final AnimatedImageView imageView)
	{
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setBackgroundResource(flakes[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
		babochkaAnimation.start();
		int posY1=mRnd.nextInt(Owner.getViewHeight());
		int posY2=mRnd.nextInt(Owner.getViewHeight());
		int direction=mRnd.nextInt(2);
		int startX=direction==1?-maxSize.x:Owner.getViewWidth()+maxSize.x;
		int endX=direction==1?Owner.getViewWidth()+maxSize.x:-maxSize.x;
		setBounds(imageView,
				startX-maxSize.x/2,
				posY1-maxSize.y/2f,
				startX+maxSize.x/2,
				posY1+maxSize.y/2f);
		float angle= (float) (Math.atan2(posY2-posY1,endX-startX)/ Math.PI * 180)+180;
		imageView.setRotation(angle);
		Animation animation=new TranslateAnimation(0,endX-startX,0,posY2-posY1);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setKometaPath(imageView);
			}
		});
		animation.setDuration(8000+mRnd.nextInt(4000));
		imageView.startAnimation(animation);
	}

	public void setSamoletikPath(final AnimatedImageView imageView)
	{
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setBackgroundResource(flakes[resNum]);
		AnimationDrawable babochkaAnimation=(AnimationDrawable)imageView.getBackground();
		babochkaAnimation.start();
		int posY1=mRnd.nextInt(Owner.getViewHeight()-maxSize.y);
		int posY2=mRnd.nextInt(Owner.getViewHeight()-maxSize.y);
		int direction=mRnd.nextInt(2);
		int startX=direction==1?-maxSize.x:Owner.getViewWidth()+maxSize.x;
		int endX=direction==1?Owner.getViewWidth()+maxSize.x:-maxSize.x;
		setBounds(imageView,
				startX-maxSize.x/2,
				posY1-maxSize.y/2f,
				startX+maxSize.x/2,
				posY1+maxSize.y/2f);
		imageView.setHorizontalReverse(direction!=1);
		float angle= (float) (Math.atan2(posY2-posY1,Math.abs(endX-startX))/ Math.PI * 180);
		imageView.setRotation(angle);
		Animation animation=new TranslateAnimation(0,endX-startX,0,posY2-posY1);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setSamoletikPath(imageView);
			}
		});
		animation.setDuration(8000+mRnd.nextInt(4000));
		imageView.startAnimation(animation);
	}

	public void setSamoletPath(final AnimatedImageView imageView)
	{
		int resNum=mRnd.nextInt(flakes.length);
		imageView.setImageResource(flakes[resNum]);
		int posY1=mRnd.nextInt(Owner.getViewHeight()-maxSize.y);
		int posY2=mRnd.nextInt(Owner.getViewHeight()-maxSize.y);
		int direction=mRnd.nextInt(2);
		int startX=direction==1?-maxSize.x:Owner.getViewWidth()+maxSize.x;
		int endX=direction==1?Owner.getViewWidth()+maxSize.x:-maxSize.x;
		setBounds(imageView,
				startX-maxSize.x/2,
				posY1-maxSize.y/2f,
				startX+maxSize.x/2,
				posY1+maxSize.y/2f);
		imageView.setHorizontalReverse(direction!=1);
		float angle= (float) (Math.atan2(posY2-posY1,Math.abs(endX-startX))/ Math.PI * 180);
		imageView.setRotation(angle);
		Animation animation=new TranslateAnimation(0,endX-startX,0,posY2-posY1);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}
			@Override public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(imageView.getAnimation()!=null)
					setSamoletPath(imageView);
			}
		});
		animation.setDuration(8000+mRnd.nextInt(4000));
		imageView.startAnimation(animation);
	}

	public void makeAnimatedBackground(int backgroundType)
	{
		AnimationLayout=new RelativeLayout(Owner);
		AnimationLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.addView(AnimationLayout);
		switch (backgroundType)
		{
			case 2: {
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animaciya_solnyshko);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(CenterX + (-150 - 600) * bmp_factor,
							Top + (-150) * bmp_factor,
							CenterX + (150 - 600) * bmp_factor,
							Top + 150 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, 180, rectF.width()/2, rectF.height()/2);
					animation.setDuration(10000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 3: {
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.luna);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(Owner.getViewWidth() - 260 * bmp_factor,
							0,
							Owner.getViewWidth(),
							260 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, 22, rectF.width()/2, rectF.height()/2);
					animation.setDuration(2500);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 4: {
				flakes=new int[]{R.drawable.animatiya_snezhinka1,R.drawable.animatiya_snezhinka2,R.drawable.animatiya_snezhinka3,R.drawable.animatiya_snezhinka4,R.drawable.animatiya_snezhinka5};
				maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[10];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setSnowflake(ListAnimatedObjects[i],mRnd.nextInt(8000));
				}
			}
			break;
			case 29: {
				flakes=new int[]{R.drawable.animatiya_snezhinki1,R.drawable.animatiya_snezhinki2,R.drawable.animatiya_snezhinki3,
						R.drawable.animatiya_snezhinki4,R.drawable.animatiya_snezhinki5,R.drawable.animatiya_snezhinki6,
						R.drawable.animatiya_snezhinki7,R.drawable.animatiya_snezhinki8,R.drawable.animatiya_snezhinki9,
						R.drawable.animatiya_snezhinki10,R.drawable.animatiya_snezhinki11,R.drawable.animatiya_snezhinki12,
						R.drawable.animatiya_snezhinki13,R.drawable.animatiya_snezhinki14,R.drawable.animatiya_snezhinki15,
						R.drawable.animatiya_snezhinki16,R.drawable.animatiya_snezhinki17,R.drawable.animatiya_snezhinki18,
						R.drawable.animatiya_snezhinki19,R.drawable.animatiya_snezhinki20};
				maxSize=new Point((int)(150*bmp_factor),(int)(150*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[10];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setSnowflake3D(ListAnimatedObjects[i],mRnd.nextInt(8000));
				}
			}
			break;
			case 30: {
				flakes=new int[]{R.drawable.animatiya_snezhinki1,R.drawable.animatiya_snezhinki2,R.drawable.animatiya_snezhinki3,
						R.drawable.animatiya_snezhinki4,R.drawable.animatiya_snezhinki5,R.drawable.animatiya_snezhinki6,
						R.drawable.animatiya_snezhinki7,R.drawable.animatiya_snezhinki8,R.drawable.animatiya_snezhinki9,
						R.drawable.animatiya_snezhinki10,R.drawable.animatiya_snezhinki11,R.drawable.animatiya_snezhinki12,
						R.drawable.animatiya_snezhinki13,R.drawable.animatiya_snezhinki14,R.drawable.animatiya_snezhinki15,
						R.drawable.animatiya_snezhinki16,R.drawable.animatiya_snezhinki17,R.drawable.animatiya_snezhinki18,
						R.drawable.animatiya_snezhinki19,R.drawable.animatiya_snezhinki20};
				maxSize=new Point((int)(200*bmp_factor),(int)(200*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[3/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setRomashkaCurl(ListAnimatedObjects[i]);
				}
			}
			break;
			case 31: {
				flakes=new int[]{R.drawable.animaciya_raketa01,R.drawable.animaciya_raketa02,R.drawable.animaciya_raketa03,R.drawable.animaciya_raketa04};
				maxSize=new Point((int)(127*bmp_factor),(int)(300*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[6/*flakes.length*/];
				//int i=0;
				for(int i=0;i<2;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setBackgroundResource(R.drawable.animaciya_raketa1);
					AnimationDrawable animationDrawable=(AnimationDrawable)ListAnimatedObjects[i].getBackground();
					animationDrawable.start();
					ListAnimatedObjects[i].setAlpha(0.4f);
					setRaketaUp(ListAnimatedObjects[i],mRnd.nextInt(3000));
				}
				{
					ListAnimatedObjects[2] = new AnimatedImageView(Owner);
					ListAnimatedObjects[2].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[2].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (390 - 600) * bmp_factor,
							Top + 165 * bmp_factor,
							CenterX + (490 - 600) * bmp_factor,
							Top + 265 * bmp_factor);
					setBounds(ListAnimatedObjects[2],rectF);
					AnimationLayout.addView(ListAnimatedObjects[2]);
					Animation animation = new AlphaAnimation(0.5f, 1);
					animation.setDuration(1000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[2].startAnimation(animation);
				}
				{
					ListAnimatedObjects[3] = new AnimatedImageView(Owner);
					ListAnimatedObjects[3].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[3].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (900 - 600) * bmp_factor,
							Top + 420 * bmp_factor,
							CenterX + (970 - 600) * bmp_factor,
							Top + 490 * bmp_factor);
					setBounds(ListAnimatedObjects[3],rectF);
					AnimationLayout.addView(ListAnimatedObjects[3]);
					Animation animation = new AlphaAnimation(0.2f, 1);
					animation.setDuration(1300);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[3].startAnimation(animation);
				}
				{
					ListAnimatedObjects[4] = new AnimatedImageView(Owner);
					ListAnimatedObjects[4].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[4].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (680 - 600) * bmp_factor,
							Top + 80 * bmp_factor,
							CenterX + (760 - 600) * bmp_factor,
							Top + 160 * bmp_factor);
					setBounds(ListAnimatedObjects[4],rectF);
					AnimationLayout.addView(ListAnimatedObjects[4]);
					Animation animation = new AlphaAnimation(0.3f, 1);
					animation.setDuration(1600);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[4].startAnimation(animation);
				}
				{
					ListAnimatedObjects[5] = new AnimatedImageView(Owner);
					ListAnimatedObjects[5].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[5].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (980 - 600) * bmp_factor,
							Top + 130 * bmp_factor,
							CenterX + (1080 - 600) * bmp_factor,
							Top + 230 * bmp_factor);
					setBounds(ListAnimatedObjects[5],rectF);
					AnimationLayout.addView(ListAnimatedObjects[5]);
					Animation animation = new AlphaAnimation(0.1f, 1);
					animation.setDuration(1400);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[5].startAnimation(animation);
				}
			}
			break;
			case 32: {
				flakes=new int[]{R.drawable.animaciya_vozd_shar1,R.drawable.animaciya_vozd_shar2,R.drawable.animaciya_vozd_shar3};
				maxSize=new Point((int)(213*bmp_factor),(int)(300*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[1/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setVozdushnyjShar2Up(ListAnimatedObjects[i],mRnd.nextInt(3000));
				}
			}
			break;
			case 33: {
				flakes=new int[]{R.drawable.animaciya_samoletik1};
				maxSize=new Point((int)(300*bmp_factor),(int)(72*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[2];
				//int i=0;
				for(int i=0;i<2;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					/*Matrix matrix = new Matrix();
					matrix.setTranslate(-maxSize.x,0);
					ListAnimatedObjects[i].setScaleType(ImageView.ScaleType.MATRIX);
					ListAnimatedObjects[i].setImageMatrix(matrix);*/
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setSamoletikPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 34: {
				flakes=new int[]{R.drawable.animaciya_samolet01,R.drawable.animaciya_samolet02,R.drawable.animaciya_samolet03,
						R.drawable.animaciya_samolet04,R.drawable.animaciya_samolet05,R.drawable.animaciya_samolet06,
						R.drawable.animaciya_samolet07,R.drawable.animaciya_samolet08};
				maxSize=new Point((int)(600*bmp_factor),(int)(148*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[1];
				//int i=0;
				for(int i=0;i<1;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setBackgroundResource(R.drawable.animaciya_samolet);
					AnimationDrawable babochkaAnimation=(AnimationDrawable)ListAnimatedObjects[i].getBackground();
					babochkaAnimation.start();
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setSamoletPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 37: {
				flakes=new int[]{R.drawable.animaciya_nlo01,R.drawable.animaciya_nlo02,R.drawable.animaciya_nlo03,R.drawable.animaciya_nlo04,R.drawable.animaciya_nlo05};
				maxSize=new Point((int)(300*bmp_factor),(int)(159*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[1];
				//int i=0;
				for(int i=0;i<1;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setBackgroundResource(R.drawable.animaciya_nlo1);
					AnimationDrawable babochkaAnimation=(AnimationDrawable)ListAnimatedObjects[i].getBackground();
					babochkaAnimation.start();
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setSamoletPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 16: {
				flakes=new int[]{R.drawable.animaciya_perekati_pole};
				maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[5];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setPerekatiPole(ListAnimatedObjects[i],mRnd.nextInt(8000),(int)(330*bmp_factor),(int)(554*bmp_factor));
				}
			}
			break;
			case 24: {
				flakes=new int[]{R.drawable.animaciya_perekati_pole};
				maxSize=new Point((int)(150*bmp_factor),(int)(150*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[2];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setPerekatiPole(ListAnimatedObjects[i],mRnd.nextInt(8000),(int)(554*bmp_factor),(int)(615*bmp_factor));
				}
			}
			break;
			case 17: {
				flakes=new int[]{R.drawable.animaciya_volny1,R.drawable.animaciya_volny2,R.drawable.animaciya_volny3,R.drawable.animaciya_volny4,R.drawable.animaciya_volny5};
				RectF[] rectF= {
						new RectF(-10, 330, Owner.getViewWidth() + 20, 450),
						new RectF(-10, 383, Owner.getViewWidth() + 20, 533),
						new RectF(-10, 436, Owner.getViewWidth() + 20, 556),
						new RectF(-10, 489, Owner.getViewWidth() + 20, 609),
						new RectF(-10, 542, Owner.getViewWidth() + 20, 620),
				};
				maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[rectF.length];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(flakes[i]);
					//ListAnimatedObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
					setBounds(ListAnimatedObjects[i],
							rectF[i].left*bmp_factor,
							rectF[i].top*bmp_factor,
							rectF[i].right*bmp_factor,
							rectF[i].bottom*bmp_factor);
					Animation animation=new TranslateAnimation(0,10*bmp_factor,0,0);
					animation.setDuration(1000+i*200);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].startAnimation(animation);
				}
			}
			break;
			case 18: {
				flakes=new int[]{R.drawable.animatiya_pyanto1,R.drawable.animatiya_pyanto2,R.drawable.animatiya_pyanto3,R.drawable.animatiya_pyanto4,
						R.drawable.animatiya_pyanto5,R.drawable.animatiya_pyanto6,R.drawable.animatiya_pyanto7,R.drawable.animatiya_pyanto8};
				maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[8];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
				}
				setSalut();
			}
			break;
			case 5: {
				flakes=new int[]{R.drawable.animatiya_pyanto1,R.drawable.animatiya_pyanto2,R.drawable.animatiya_pyanto3,R.drawable.animatiya_pyanto4};
				maxSize=new Point((int)(200*bmp_factor),(int)(200*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[flakes.length];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(flakes[i]);
					final int posX=mRnd.nextInt((int) (Owner.getViewWidth()-200*bmp_factor));
					final int posY=mRnd.nextInt((int) (Owner.getViewHeight()-200*bmp_factor));
					setBounds(ListAnimatedObjects[i],posX,posY,posX+200*bmp_factor,posY+200*bmp_factor);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setPyatno(ListAnimatedObjects[i]);
				}
			}
			break;
			case 20: {
				flakes=new int[]{R.drawable.animaciya_babochka_1,R.drawable.animaciya_babochka_2,R.drawable.animaciya_babochka_3,R.drawable.animaciya_babochka_4,R.drawable.animaciya_babochka_5,
						R.drawable.animaciya_strekoza_1,R.drawable.animaciya_strekoza_2,R.drawable.animaciya_strekoza_3,R.drawable.animaciya_strekoza_4,R.drawable.animaciya_strekoza_5};
				maxSize=new Point((int)(204*bmp_factor),(int)(158*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[1/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					/*ListAnimatedObjects[i].setBackgroundResource(flakes[mRnd.nextInt(flakes.length)]);
					AnimationDrawable babochkaAnimation=(AnimationDrawable)ListAnimatedObjects[i].getBackground();
					babochkaAnimation.start();*/
					setBabochkaPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 21: {
				flakes=new int[]{R.drawable.animaciya_romashka1,R.drawable.animaciya_romashka2,R.drawable.animaciya_romashka3,R.drawable.animaciya_romashka4,R.drawable.animaciya_romashka5,
						R.drawable.animaciya_romashka6,R.drawable.animaciya_romashka7,R.drawable.animaciya_romashka8,R.drawable.animaciya_romashka9};
				maxSize=new Point((int)(200*bmp_factor),(int)(200*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[3/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					/*ListAnimatedObjects[i].setBackgroundResource(flakes[mRnd.nextInt(flakes.length)]);
					AnimationDrawable babochkaAnimation=(AnimationDrawable)ListAnimatedObjects[i].getBackground();
					babochkaAnimation.start();*/
					//setSnowflake(ListAnimatedObjects[i],mRnd.nextInt(8000));
					ListAnimatedObjects[i].setAlpha(0.6f);
					setRomashkaPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 22: {
				flakes=new int[]{R.drawable.animaciya_rybka01,R.drawable.animaciya_rybka02,R.drawable.animaciya_rybka03};
				flakes2=new int[]{R.drawable.animaciya_rybka1,R.drawable.animaciya_rybka2,R.drawable.animaciya_rybka3};
				maxSize=new Point((int)(160*bmp_factor),(int)(115*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[4/*flakes.length*/];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setRybkaPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 36: {
				flakes=new int[]{R.drawable.animaciya_podlodka01,R.drawable.animaciya_podlodka02,R.drawable.animaciya_podlodka03,R.drawable.animaciya_podlodka04};
				flakes2=new int[]{R.drawable.animaciya_podlodka1,R.drawable.animaciya_podlodka1,R.drawable.animaciya_podlodka1,R.drawable.animaciya_podlodka1};
				maxSize=new Point((int)(585*bmp_factor),(int)(297*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[1/*flakes.length*/];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setRybkaPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 35: {
				flakes=new int[]{R.drawable.animaciya_meduza01,R.drawable.animaciya_meduza02,R.drawable.animaciya_meduza03};
				flakes2=new int[]{R.drawable.animaciya_meduza1,R.drawable.animaciya_meduza2,R.drawable.animaciya_meduza3};
				maxSize=new Point((int)(112*bmp_factor),(int)(183*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[3/*flakes.length*/];
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setMeduzaPath(ListAnimatedObjects[i]);
				}
			}
			break;
			case 23: {
				flakes=new int[]{R.drawable.animaciya_romashka1,R.drawable.animaciya_romashka2,R.drawable.animaciya_romashka3,R.drawable.animaciya_romashka4,R.drawable.animaciya_romashka5,
						R.drawable.animaciya_romashka6,R.drawable.animaciya_romashka7,R.drawable.animaciya_romashka8,R.drawable.animaciya_romashka9};
				maxSize=new Point((int)(200*bmp_factor),(int)(200*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[3/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setRomashkaCurl(ListAnimatedObjects[i]);
				}
			}
			break;
			case 25: {
				flakes=new int[]{R.drawable.animaciya_sharik1,R.drawable.animaciya_sharik2,R.drawable.animaciya_sharik3,R.drawable.animaciya_sharik4,R.drawable.animaciya_sharik5,
						R.drawable.animaciya_sharik6,R.drawable.animaciya_sharik7};
				maxSize=new Point((int)(141*bmp_factor),(int)(250*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[5/*flakes.length*/];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setSharikUp(ListAnimatedObjects[i],mRnd.nextInt(3000));
				}
			}
			break;
			case 26: {
				flakes=new int[]{R.drawable.animaciya_parovozik01,R.drawable.animaciya_parovozik02,
						R.drawable.animaciya_avtobus01,R.drawable.animaciya_avtobus02,R.drawable.animaciya_gruzovik01/*,R.drawable.animaciya_parovozik03,
						R.drawable.animaciya_parovozik04,R.drawable.animaciya_avtobus03,R.drawable.animaciya_avtobus04,R.drawable.animaciya_gruzovik02*/};
				flakes2=new int[]{R.drawable.animaciya_parovozik1,R.drawable.animaciya_parovozik1,
						R.drawable.animaciya_avtobus1,R.drawable.animaciya_avtobus1,R.drawable.animaciya_gruzovik1/*,R.drawable.animaciya_parovozik1,
						R.drawable.animaciya_parovozik1,R.drawable.animaciya_avtobus2,R.drawable.animaciya_avtobus2,R.drawable.animaciya_gruzovik2*/};
				maxSize=new Point((int)(527*bmp_factor),(int)(234*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[/*flakes.length**/2];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length/2;i++)
				{
					ListAnimatedObjects[i*2]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i*2]);
					//ListAnimatedObjects[i*2].setScaleType(ImageView.ScaleType.FIT_XY);
					ListAnimatedObjects[i*2].setImageResource(flakes[i]);
					ListAnimatedObjects[i*2+1]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i*2+1]);
					setParovozikPath(ListAnimatedObjects[i*2],ListAnimatedObjects[i*2+1]);
				}
			}
			break;
			/*case 27: {
				flakes=new int[]{R.drawable.animaciya_vozdushnyj_shar1,R.drawable.animaciya_vozdushnyj_shar2,R.drawable.animaciya_vozdushnyj_shar3,
						R.drawable.animaciya_vozdushnyj_shar4,R.drawable.animaciya_vozdushnyj_shar5,
						R.drawable.animaciya_vozdushnyj_shar6,R.drawable.animaciya_vozdushnyj_shar7};
				maxSize=new Point((int)(222*bmp_factor),(int)(300*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[2];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setVozdushnyjSharUp(ListAnimatedObjects[i],mRnd.nextInt(3000));
				}
			}
			break;*/
			case 28: {
				flakes=new int[]{R.drawable.animaciya_kometa,R.drawable.animaciya_kometa02,R.drawable.animaciya_kometa03};
				maxSize=new Point((int)(403*bmp_factor),(int)(128*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[6];
				//int i=0;
				for(int i=0;i<2;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setAlpha(0.6f);
					setKometaPath(ListAnimatedObjects[i]);
				}
				{
					ListAnimatedObjects[2] = new AnimatedImageView(Owner);
					ListAnimatedObjects[2].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[2].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (390 - 600) * bmp_factor,
							Top + 165 * bmp_factor,
							CenterX + (490 - 600) * bmp_factor,
							Top + 265 * bmp_factor);
					setBounds(ListAnimatedObjects[2],rectF);
					AnimationLayout.addView(ListAnimatedObjects[2]);
					Animation animation = new AlphaAnimation(0.5f, 1);
					animation.setDuration(1000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[2].startAnimation(animation);
				}
				{
					ListAnimatedObjects[3] = new AnimatedImageView(Owner);
					ListAnimatedObjects[3].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[3].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (900 - 600) * bmp_factor,
							Top + 420 * bmp_factor,
							CenterX + (970 - 600) * bmp_factor,
							Top + 490 * bmp_factor);
					setBounds(ListAnimatedObjects[3],rectF);
					AnimationLayout.addView(ListAnimatedObjects[3]);
					Animation animation = new AlphaAnimation(0.2f, 1);
					animation.setDuration(1300);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[3].startAnimation(animation);
				}
				{
					ListAnimatedObjects[4] = new AnimatedImageView(Owner);
					ListAnimatedObjects[4].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[4].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (680 - 600) * bmp_factor,
							Top + 80 * bmp_factor,
							CenterX + (760 - 600) * bmp_factor,
							Top + 160 * bmp_factor);
					setBounds(ListAnimatedObjects[4],rectF);
					AnimationLayout.addView(ListAnimatedObjects[4]);
					Animation animation = new AlphaAnimation(0.3f, 1);
					animation.setDuration(1600);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[4].startAnimation(animation);
				}
				{
					ListAnimatedObjects[5] = new AnimatedImageView(Owner);
					ListAnimatedObjects[5].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[5].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (980 - 600) * bmp_factor,
							Top + 130 * bmp_factor,
							CenterX + (1080 - 600) * bmp_factor,
							Top + 230 * bmp_factor);
					setBounds(ListAnimatedObjects[5],rectF);
					AnimationLayout.addView(ListAnimatedObjects[5]);
					Animation animation = new AlphaAnimation(0.1f, 1);
					animation.setDuration(1400);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[5].startAnimation(animation);
				}
			}
			break;
			case 12: {
				flakes=new int[]{R.drawable.animatiya_pyanto5,R.drawable.animatiya_pyanto5,R.drawable.animatiya_pyanto5};
				maxSize=new Point((int)(400*bmp_factor),(int)(400*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[flakes.length];
				//int i=0;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(flakes[i]);
					final int posX=mRnd.nextInt((int) (Owner.getViewWidth()-400*bmp_factor));
					final int posY=mRnd.nextInt((int) (Owner.getViewHeight()-400*bmp_factor));
					setBounds(ListAnimatedObjects[i],posX,posY,posX+400*bmp_factor,posY+400*bmp_factor);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					setPyatno(ListAnimatedObjects[i]);
				}
			}
			break;
			case 13: {
				//flakes=new int[]{R.drawable.animacya_koleso1,R.drawable.animatiya_pyanto5,R.drawable.animatiya_pyanto5};
				PointF[] points=new PointF[]{
						new PointF(427,498),
						new PointF(351,498),
						new PointF(273,498),
						new PointF(197,498),
						new PointF(122,498),
						new PointF(46,498),
						new PointF(503,498),
						new PointF(579,498),
						new PointF(881,499),
						new PointF(954,499),
						new PointF(789,477),
						new PointF(673,477),				};
				ListAnimatedObjects = new AnimatedImageView[points.length];
				//int i=0;
				for(int i=0;i<points.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(R.drawable.animacya_koleso4);
					//ListAnimatedObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
					float radius=41.5f*(i<points.length-2?1:1.5f);
					setBounds(ListAnimatedObjects[i],Left+(points[i].x-radius)*bmp_factor,Top+(points[i].y-radius)*bmp_factor,Left+(points[i].x+radius)*bmp_factor,(points[i].y+radius)*bmp_factor);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					Animation animation = new RotateAnimation(0, 359, radius*bmp_factor, radius*bmp_factor);
					animation.setDuration(2500);
					//animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[i].startAnimation(animation);
				}
			}
			break;
			case 6: {
				flakes=new int[]{R.drawable.animatiya_oblako1,R.drawable.animatiya_oblako2,R.drawable.animatiya_oblako3,R.drawable.animatiya_oblako4,R.drawable.animatiya_oblako1};
				maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				ListAnimatedObjects = new AnimatedImageView[flakes.length];
				final Rect[] startPoints={
						new Rect(884,18, 1126,166),
						new Rect(693,18, 865,122),
						new Rect(606,34, 736,138),
						new Rect(64,27, 342,157),
						new Rect(388,14, 560,118),
				};
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(flakes[i]);
					setBounds(ListAnimatedObjects[i],
							startPoints[i].left*bmp_factor,
							startPoints[i].top*bmp_factor,
							startPoints[i].right*bmp_factor,
							startPoints[i].bottom*bmp_factor);
					AnimationLayout.addView(ListAnimatedObjects[i]);
					ListAnimatedObjects[i].setCentralPoint(new Point((int)(startPoints[i].left*bmp_factor),(int)(startPoints[i].top*bmp_factor)));
					setOblako(ListAnimatedObjects[i]);
				}
			}
			break;
			case 7: {
				ListAnimatedObjects = new AnimatedImageView[2];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animacya_barboskiny1);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(CenterX - 205 * bmp_factor,
							0,
							CenterX + 205 * bmp_factor,
							173 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, -2*4.17f, rectF.width()/2, 24*bmp_factor);
					animation.setDuration(1500);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
				{
					ListAnimatedObjects[1] = new AnimatedImageView(Owner);
					ListAnimatedObjects[1].setImageResource(R.drawable.animacya_barboskiny2);
					//ListAnimatedObjects[1].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(CenterX - 10.5f * bmp_factor,
							0,
							CenterX + 10.5f * bmp_factor,
							28 * bmp_factor);
					setBounds(ListAnimatedObjects[1], rectF);
					AnimationLayout.addView(ListAnimatedObjects[1]);
				}
			}
			break;
			case 8: {
				//flakes=new int[]{R.drawable.animatiya_oblako1,R.drawable.animatiya_oblako2,R.drawable.animatiya_oblako3,R.drawable.animatiya_oblako4,R.drawable.animatiya_oblako1};
				//maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				final Rect[] startPoints={
						new Rect(189,269, 289,365),
						new Rect(360,95, 460,195),
						new Rect(416,442, 516,542),
						new Rect(923,255, 1023,355),
						new Rect(927,154, 1027,254),
						new Rect(752,38, 852,138),
						new Rect(799,493, 899,593),
						new Rect(181,470, 281,570),
						new Rect(181,470, 281,570),
						new Rect(144,334, 244,434),
						new Rect(144,192, 244,292),
						new Rect(266,180, 366,280),
						new Rect(466,109, 566,209),
						new Rect(480,280, 580,380),
						new Rect(507,384, 607,484),
						new Rect(522,453, 622,553),
						new Rect(829,142, 929,242),
						new Rect(741,134, 841,234),
						new Rect(701,80, 801,180),
						new Rect(690,279, 790,379),
				};
				ListAnimatedObjects = new AnimatedImageView[startPoints.length];
				float iMulY=Owner.getViewHeight()/620f;
				float iMulX=Owner.getViewWidth()/1013f;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(R.drawable.animatiya_pyanto1);
					setBounds(ListAnimatedObjects[i],
							startPoints[i].left*iMulX,
							startPoints[i].top*iMulY,
							startPoints[i].right*iMulX,
							startPoints[i].bottom*iMulY);
					AnimationLayout.addView(ListAnimatedObjects[i]);

					Animation animation = new AlphaAnimation(0, 1);
					animation.setDuration(1000+mRnd.nextInt(1000));
					animation.setStartOffset(mRnd.nextInt(10000));
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					final int finalI = i;
					animation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							/*Animation animation2 = new AlphaAnimation(0, 1);
							animation2.setDuration(1000+mRnd.nextInt(1000));
							animation2.setStartOffset(mRnd.nextInt(10000));
							animation2.setRepeatMode(Animation.REVERSE);
							ListAnimatedObjects[finalI].startAnimation(animation2);*/
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					ListAnimatedObjects[i].startAnimation(animation);
				}
			}
			break;
			case 9: {
				flakes=new int[]{R.drawable.animacya_dom1,R.drawable.animacya_dom2,R.drawable.animacya_dom3,R.drawable.animacya_dom4,R.drawable.animacya_dom5,R.drawable.animacya_dom6,R.drawable.animacya_dom7,R.drawable.animacya_dom8,R.drawable.animacya_dom9};
				//maxSize=new Point((int)(100*bmp_factor),(int)(100*bmp_factor));
				final Rect[] startPoints={
						new Rect(484,366, 583,433),
						new Rect(450,473, 493,535),
						new Rect(862,488, 996,556),
						new Rect(310,592, 373,620),
						new Rect(219,502, 400,549),
						new Rect(489,538, 578,620),
						new Rect(2,409, 106,498),
						new Rect(173,473, 195,553),
						new Rect(210,350, 265,403),
				};
				ListAnimatedObjects = new AnimatedImageView[startPoints.length];
				float iMulY=Owner.getViewHeight()/620f;
				float iMulX=Owner.getViewWidth()/1200f;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(flakes[i]);
					setBounds(ListAnimatedObjects[i],
							startPoints[i].left*iMulX,
							startPoints[i].top*iMulY,
							startPoints[i].right*iMulX,
							startPoints[i].bottom*iMulY);
					AnimationLayout.addView(ListAnimatedObjects[i]);

					Animation animation = new AlphaAnimation(0, 1);
					animation.setDuration(2000+mRnd.nextInt(2000));
					//animation.setStartOffset(mRnd.nextInt(10000));
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[i].startAnimation(animation);
				}
			}
			break;
			case 10: {
				//float iMul=Owner.getViewWidth()/1200f;
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animacya_smeshariki);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(Owner.getViewWidth()/2 - 175 * bmp_factor,
							0,
							Owner.getViewWidth()/2 + 175 * bmp_factor,
							166 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, -2*10f, rectF.width()/2, rectF.height()/2);
					animation.setDuration(1500);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 19: {
				//float iMul=Owner.getViewWidth()/1200f;
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animaciya_fixiki);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(Owner.getViewWidth()/2 - 169 * bmp_factor,
							0,
							Owner.getViewWidth()/2 + 169 * bmp_factor,
							104 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, -2*7f, rectF.width()/2, 48*bmp_factor);
					animation.setDuration(1500);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 14: {
				//float iMul=Owner.getViewWidth()/1200f;
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animaciya_chasy2);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(Left + 837 * bmp_factor,
							31 * bmp_factor,
							Left + 846 * bmp_factor,
							94 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new RotateAnimation(0, 359, rectF.width()/2, 50.5f*bmp_factor);
					animation.setDuration(10000);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 15: {
				//float iMul=Owner.getViewWidth()/1200f;
				ListAnimatedObjects = new AnimatedImageView[1];
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.animaciya_mama_na_kuhne);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF = new RectF(Left + 546 * bmp_factor,
							328 * bmp_factor,
							Left + 690 * bmp_factor,
							416 * bmp_factor);
					setBounds(ListAnimatedObjects[0], rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);

					Animation animation = new AlphaAnimation(0, 1);
					animation.setDuration(5000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
			}
			break;
			case 1:
			{
				ListAnimatedObjects = new AnimatedImageView[10];
				{
					ListAnimatedObjects[1] = new AnimatedImageView(Owner);
					ListAnimatedObjects[1].setImageResource(R.drawable.kometa_tail);
					//ListAnimatedObjects[1].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (81 - 600) * bmp_factor,
							Top + 51 * bmp_factor,
							CenterX + (188 - 600) * bmp_factor,
							Top + 133 * bmp_factor);
					setBounds(ListAnimatedObjects[1],rectF);
					AnimationLayout.addView(ListAnimatedObjects[1]);

					Animation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f, rectF.width(), rectF.height());
					animation.setDuration(2000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[1].startAnimation(animation);
				}
				{
					ListAnimatedObjects[0] = new AnimatedImageView(Owner);
					ListAnimatedObjects[0].setImageResource(R.drawable.kometa_head);
					//ListAnimatedObjects[0].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (168 - 600) * bmp_factor,
							Top + 109 * bmp_factor,
							CenterX + (244 - 600) * bmp_factor,
							Top + 182 * bmp_factor);
					setBounds(ListAnimatedObjects[0],rectF);
					AnimationLayout.addView(ListAnimatedObjects[0]);
					Animation animation = new RotateAnimation(0, 359, rectF.width() / 2, rectF.height() / 2);
					animation.setDuration(10000);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[0].startAnimation(animation);
				}
				{
					ListAnimatedObjects[2] = new AnimatedImageView(Owner);
					ListAnimatedObjects[2].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[2].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (390 - 600) * bmp_factor,
							Top + 165 * bmp_factor,
							CenterX + (490 - 600) * bmp_factor,
							Top + 265 * bmp_factor);
					setBounds(ListAnimatedObjects[2],rectF);
					AnimationLayout.addView(ListAnimatedObjects[2]);
					Animation animation = new AlphaAnimation(0.5f, 1);
					animation.setDuration(1000);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[2].startAnimation(animation);
				}
				{
					ListAnimatedObjects[3] = new AnimatedImageView(Owner);
					ListAnimatedObjects[3].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[3].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (900 - 600) * bmp_factor,
							Top + 420 * bmp_factor,
							CenterX + (970 - 600) * bmp_factor,
							Top + 490 * bmp_factor);
					setBounds(ListAnimatedObjects[3],rectF);
					AnimationLayout.addView(ListAnimatedObjects[3]);
					Animation animation = new AlphaAnimation(0.2f, 1);
					animation.setDuration(1300);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[3].startAnimation(animation);
				}
				{
					ListAnimatedObjects[4] = new AnimatedImageView(Owner);
					ListAnimatedObjects[4].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[4].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (680 - 600) * bmp_factor,
							Top + 80 * bmp_factor,
							CenterX + (760 - 600) * bmp_factor,
							Top + 160 * bmp_factor);
					setBounds(ListAnimatedObjects[4],rectF);
					AnimationLayout.addView(ListAnimatedObjects[4]);
					Animation animation = new AlphaAnimation(0.3f, 1);
					animation.setDuration(1600);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[4].startAnimation(animation);
				}
				{
					ListAnimatedObjects[5] = new AnimatedImageView(Owner);
					ListAnimatedObjects[5].setImageResource(R.drawable.menu_star);
					//ListAnimatedObjects[5].setScaleType(ImageView.ScaleType.FIT_XY);
					RectF rectF=new RectF(CenterX + (980 - 600) * bmp_factor,
							Top + 130 * bmp_factor,
							CenterX + (1080 - 600) * bmp_factor,
							Top + 230 * bmp_factor);
					setBounds(ListAnimatedObjects[5],rectF);
					AnimationLayout.addView(ListAnimatedObjects[5]);
					Animation animation = new AlphaAnimation(0.1f, 1);
					animation.setDuration(1400);
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					ListAnimatedObjects[5].startAnimation(animation);
				}
			}
			break;
			case 11:
			{
				final Rect[] startPoints={
						new Rect(189,269, 289,365),
						//new Rect(360,95, 460,195),
						new Rect(416,442, 516,542),
						//new Rect(923,255, 1023,355),
						new Rect(927,154, 1027,254),
						//new Rect(752,38, 852,138),
						new Rect(799,493, 899,593),
						//new Rect(181,470, 281,570),
						new Rect(181,470, 281,570),
						//new Rect(144,334, 244,434),
						new Rect(144,192, 244,292),
						//new Rect(266,180, 366,280),
						new Rect(466,109, 566,209),
						//new Rect(480,280, 580,380),
						new Rect(507,384, 607,484),
						//new Rect(522,453, 622,553),
						new Rect(829,142, 929,242),
						//new Rect(741,134, 841,234),
						new Rect(701,80, 801,180),
						//new Rect(690,279, 790,379),
				};
				ListAnimatedObjects = new AnimatedImageView[startPoints.length];
				float iMulY=Owner.getViewHeight()/620f;
				float iMulX=Owner.getViewWidth()/1013f;
				for(int i=0;i<ListAnimatedObjects.length;i++)
				{
					ListAnimatedObjects[i]=new AnimatedImageView(Owner);
					ListAnimatedObjects[i].setImageResource(R.drawable.star7);
					setBounds(ListAnimatedObjects[i],
							startPoints[i].left*iMulX,
							startPoints[i].top*iMulY,
							startPoints[i].right*iMulX,
							startPoints[i].bottom*iMulY);
					AnimationLayout.addView(ListAnimatedObjects[i]);

					Animation animation = new AlphaAnimation(0, 1);
					animation.setDuration(1000+mRnd.nextInt(1000));
					animation.setStartOffset(mRnd.nextInt(10000));
					animation.setRepeatMode(Animation.REVERSE);
					animation.setRepeatCount(Animation.INFINITE);
					final int finalI = i;
					animation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							/*ListAnimatedObjects[finalI].clearAnimation();
							Animation animation2 = new AlphaAnimation(0, 1);
							animation2.setDuration(1000+mRnd.nextInt(1000));
							animation2.setStartOffset(mRnd.nextInt(10000));
							animation2.setRepeatMode(Animation.REVERSE);
							ListAnimatedObjects[finalI].startAnimation(animation2);*/
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					ListAnimatedObjects[i].startAnimation(animation);
				}
			}
			break;
			case 0: {
				int animationResources[]={18,5,20,21,12,23,25};
				int randomAnimation = mRnd.nextInt(animationResources.length);
				makeAnimatedBackground(animationResources[randomAnimation]);
			}
			break;
			case -1: {
				int animationResources[]={28,31,33,34,37};
				int randomAnimation = mRnd.nextInt(animationResources.length);
				makeAnimatedBackground(animationResources[randomAnimation]);
			}
			break;
			case -2: {
				int animationResources[]={4,29,30,12};
				int randomAnimation = mRnd.nextInt(animationResources.length);
				makeAnimatedBackground(animationResources[randomAnimation]);
			}
			break;
			case -3: {
				int animationResources[]={33, 31, 25, 26, 27, 33, 34};
				int randomAnimation = mRnd.nextInt(animationResources.length);
				makeAnimatedBackground(animationResources[randomAnimation]);
			}
			break;
			case -4: {
				int animationResources[]={22,35,36};
				int randomAnimation = mRnd.nextInt(animationResources.length);
				makeAnimatedBackground(animationResources[randomAnimation]);
			}
			break;
		}
	}
	//Thread showStarsThread=null;
	Thread showStarsThread=null;

	public void setLevelBackground()
	{
		GameView.this.setBackground(new BitmapDrawable(Owner.getResources(), getImageFromObb(levels[current_level_num].background, ".jpg")));
	}

	public void startShowStarsTread()
	{
		showStarsThread = new Thread(new showStarsRunnable());
		showStarsThread.start();
	}
        private void StartLevel()
        {
        	showInterstatial();
			this.post(new Runnable() {
				@Override
				public void run() {
					isStartingLevel = true;
					ClearLevel();
					ListClearObjects=new ShadowImageView[levels[current_level_num].items.size()];
					/*if(isStars) {
						showStarsThread = new Thread(new showStarsRunnable());
						showStarsThread.start();
					}*/
					//showStarsThread = new showStarsRunnable();
					//showStarsThread.run();
					//LoadBackground();
					if (current_level_num >= levels.length)
						current_level_num = 0;

					ListObjects=new MovingImageView[levels[current_level_num].items.size()];
					GameView.this.setBackground(new BitmapDrawable(Owner.getResources(), getImageFromObb(levels[current_level_num].background, ".jpg")));
					if(levels[current_level_num].animatedBackground==0 && levels[current_level_num].levelType!=7) {
						GlassView = new ImageView(Owner);
						GlassView.setBackgroundColor(0x80ffffff);//Color.TRANSPARENT);
						GameView.this.setBounds(GlassView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
						GameView.this.addView(GlassView);
					}
					else
						GlassView=null;
					int levelType=levels[current_level_num].levelType;
					if(levelType==0) {
						levelType = mRnd.nextInt(3) + 1;
						if(levelType==3)
							levelType=4;
					}
					AnimationLayout=null;
					ListBackground = new ImageView(Owner);
					ListBackground.setImageBitmap(getImageFromObb(levels[current_level_num].list, ".png"));
					//ListBackground.setImageResource(R.drawable.list_4);
					Resources r = getResources();
					float w=ListBackground.getDrawable().getIntrinsicWidth();
					float h=ListBackground.getDrawable().getIntrinsicHeight();
					//float px=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
					float py = (float) Owner.getViewHeight() / 620f;//(float) ListBackground.getDrawable().getIntrinsicHeight();
					float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
					float px = (Owner.getViewWidth() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics())) / backWidth;//(float) ListBackground.getDrawable().getIntrinsicWidth();
					if (px < py)
						screen_factor = px;
					else
						screen_factor = py;
					//float screen_factor=px;//((float)getWidth()/(float)getHeight())/(640f/310f);
					bmp_factor = screen_factor;//620f/ListBackground.getHeight();///(620f/screen_factor);

					if(levelType==1)
						CenterX = Owner.getViewWidth() - bmp_factor * backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f;
					else if(levelType==5)
						CenterX=bmp_factor * backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f;
					else
						CenterX = Owner.getViewWidth() / 2f;
					Top = (Owner.getViewHeight() - bmp_factor * 620/*ListBackground.getDrawable().getIntrinsicHeight()*/) / 2f;
					Left=CenterX - bmp_factor * backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f;

					GameView.this.addView(ListBackground);

					if(levelType!=5 && isAnimateBackground)
					{
						GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
					}

					if(levelType==1)
						CreateLevelType1();
					else if(levelType==2)
						CreateLevelType2();
					else if(levelType==3)
						CreateLevelType3();
					else if(levelType==4)
						CreateLevelType4();
					else if(levelType==5)
						CreateLevelType5();
					else if(levelType==6)
						CreateLevelType6();
					else if(levelType==7)
						CreateLevelType7();
					isStartingLevel=false;
				}
			});
		}

		void CreateLevelType1()
		{
			float grid_scale = bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
			float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
			//float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
			setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
					Top,
					CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
					Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
			/*if(levels[current_level_num].animatedBackground!=0) {
				GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
			}*/
			ObjectLevelPosition = getRandomIndexes(levels[current_level_num].items.size());
			for (int i = 0; i < levels[current_level_num].items.size(); i++) {
				int index=ObjectLevelPosition[i];
				ListClearObjects[index] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
				ListObjects[index] = newDecodeResourceWithSize(levels[current_level_num].items.get(i).pictureResource, bmp_factor);
				ListObjects[index].mClearObject = ListClearObjects[index];
				ListObjects[index].mObjectNumber = i;

				float obj_height = ListObjects[index].getBitmapHeight();//getDrawable().getIntrinsicHeight();//*bmp_factor;
				float obj_width = ListObjects[index].getBitmapWidth();//getDrawable().getIntrinsicWidth();//*bmp_factor;
				float pos_y = (mRnd.nextInt(Owner.getViewHeight() - (int) obj_height));
				setBounds(ListObjects[index],
						0,
						pos_y,
						obj_width,
						pos_y + obj_height
				);
				setBounds(ListClearObjects[index],
						CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2,
						Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2,
						CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2,
						Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2 + obj_height
				);
				{
					Animation animation = new ScaleAnimation(0, 1.2f, 0, 1.2f);
					animation.setDuration(500);
					animation.setStartOffset(i * 200);
					final int finalI = index;
					animation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationEnd(Animation animation) {
							Animation animation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1);
							animation2.setDuration(500);
							ListClearObjects[finalI].startAnimation(animation2);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					ListClearObjects[index].startAnimation(animation);
				}
			}

			/*for (int i = 0; i < ListObjects.length; i++) {
				if (ListObjects[i] != null)
					GameView.this.removeView(ListObjects[i]);
			}*/

			/*for (int i = 0; i < ObjectLevelPosition.length; i++)
				ObjectLevelPosition[i] = -1;
			for (int i = 0; i < ObjectLevelPosition.length; i++) {
				int num = mRnd.nextInt(levels[current_level_num].items.size() - i);
				int real_num = 0;
				for (int j = 0; j < ObjectLevelPosition.length; j++) {
					if (ObjectLevelPosition[j] < 0) {
						if (real_num == num) {
							ObjectLevelPosition[j] = i;
							GameView.this.addView(ListObjects[j]);
							{
								Animation animation = new AlphaAnimation(0, 1);
								animation.setDuration(1500);
								ListObjects[j].startAnimation(animation);
							}

							break;
						}
						real_num++;
					}
				}
			}*/

			soundFromObb(levels[current_level_num].sound);
			CurrentScreenType = 1;
			start_time = System.currentTimeMillis();
			isStartingLevel = false;

		}

	void CreateLevelType2()
	{
		float grid_scale = bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		//CenterX = Owner.getViewWidth() / 2f;
		//Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
		setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
		/*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/

		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i] = newDecodeResourceWithSize2(levels[current_level_num].items.get(i).pictureResource, bmp_factor);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;

			final float obj_height = ListObjects[i].getBitmapHeight();//getDrawable().getIntrinsicHeight();//*bmp_factor;
			final float obj_width = ListObjects[i].getBitmapWidth();//getDrawable().getIntrinsicWidth();//*bmp_factor;
			float radius= (float) Math.sqrt((obj_height/2)*(obj_height/2)+(obj_width/2)*(obj_width/2));
			final float obj_size=radius*2;
			float pos_y = (mRnd.nextInt(Owner.getViewHeight() - (int) obj_height));
			setBounds(ListObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_size / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_size / 2f,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_size / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale + obj_size / 2f
			);
			((RotateImageView)ListObjects[i]).setCentralPoint(obj_size / 2f, obj_size / 2f);
			ListObjects[i].setAngle(mRnd.nextInt(300)+30);
			setBounds(ListClearObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2f,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale + obj_height / 2f
			);
			{
				Animation animation = new ScaleAnimation(0, 1.2f, 0, 1.2f, obj_width/2, obj_height/2);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				final int finalI = i;
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1, obj_width/2, obj_height/2);
						animation2.setDuration(500);
						ListClearObjects[finalI].startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				ListClearObjects[i].startAnimation(animation);
			}
			{
				Animation animationRotate = new RotateAnimation(0, 359, obj_size/2, obj_size/2);
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
						Animation animation2 = new RotateAnimation(0, ListObjects[finalI].getAngle(), obj_size/2, obj_size/2);
						animation2.setDuration((long) (400*(ListObjects[finalI].getAngle())/360));
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
			}
		}
		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	void CreateLevelType3()
	{
		float grid_scale = bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		//CenterX = Owner.getViewWidth() / 2f;
		//Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
		setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
		/*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/

		int[] randomIndexes=getRandomIndexes(levels[current_level_num].items.size());
		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i] = new ColorImageView(Owner,this,levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i].setImageBitmap(BitmapFactory.decodeResource(getResources(),levels[current_level_num].items.get(i).colorResource));
			//ListObjects[i].setBackgroundColor(Color.RED);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;
			this.addView(ListObjects[i]);

			final float obj_height = ListClearObjects[i].getBitmap().getHeight()*bmp_factor;
			final float obj_width = ListClearObjects[i].getBitmap().getWidth()*bmp_factor;
			float pos_y = (mRnd.nextInt(Owner.getViewHeight() - (int) obj_height));
			setBounds(ListObjects[i],
					CenterX -(levels[current_level_num].items.size()*100*grid_scale)/2 + randomIndexes[i] * 100 * grid_scale,
					Owner.getViewHeight() - 100 * grid_scale,
					CenterX -(levels[current_level_num].items.size()*100*grid_scale)/2 + (randomIndexes[i]+1) * 100 * grid_scale,
					Owner.getViewHeight()
			);
			//ListClearObjects[i].setBackgroundColor(Color.RED);
			ListObjects[i].setDockPoint(CenterX -(levels[current_level_num].items.size()*100*grid_scale)/2 + randomIndexes[i] * 100 * grid_scale,
					Owner.getViewHeight() - 100 * grid_scale);
			setBounds(ListClearObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2 + obj_height
			);
			//ListClearObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
			{
				Animation animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				ListClearObjects[i].startAnimation(animation);
				ListObjects[i].startAnimation(animation);
			}
		}
		ListBackground.bringToFront();
		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	void CreateLevelType4()
	{
		float grid_scale =  bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		//float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
		//CenterX = Owner.getViewWidth() / 2f;
		//Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
		setBounds(ListBackground, CenterX - /*ListBackground.getDrawable().getIntrinsicWidth()*/backWidth / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
		/*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/

		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i] = newDecodeResourceWithSize3(levels[current_level_num].items.get(i).pictureResource, bmp_factor);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;

			final float obj_height = ListObjects[i].getBitmap().getHeight();//getDrawable().getIntrinsicHeight();//*bmp_factor;
			final float obj_width = ListObjects[i].getBitmap().getWidth();//getDrawable().getIntrinsicWidth();//*bmp_factor;
			float pos_y = (mRnd.nextInt(Owner.getViewHeight() - (int) obj_height));
			setBounds(ListObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2 + obj_height
			);
			//ListObjects[i].setRotation(mRnd.nextInt(300)+30);
			setBounds(ListClearObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2 + obj_height
			);
			{
				Animation animation = new ScaleAnimation(0, 1.2f, 0, 1.2f, obj_width/2, obj_height/2);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				final int finalI = i;
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1, obj_width/2, obj_height/2);
						animation2.setDuration(500);
						ListClearObjects[finalI].startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				ListClearObjects[i].startAnimation(animation);
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
		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	void CreateLevelType5()
	{
		float grid_scale =  bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		//float backWidth=ListBackground.getDrawable().getIntrinsicHeight()/620f*ListBackground.getDrawable().getIntrinsicWidth();
		//CenterX = Owner.getViewWidth() / 2f;
		//Top = (Owner.getViewHeight() - bmp_factor * ListBackground.getDrawable().getIntrinsicHeight()) / 2f;
		setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
		/*if(levels[current_level_num].animatedBackground!=0) {
			GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
		}*/

		int[] randomIndexes=getRandomIndexes(levels[current_level_num].items.size());
		float resultLeft= backWidth;//ListBackground.getDrawable().getIntrinsicHeight()*bmp_factor);
		float resultCenter=(resultLeft+(1200-backWidth)/2)*grid_scale;
		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i] = new ApplicationImageView(Owner,this);
			//ListObjects[i].setBackgroundColor(Color.RED);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;
			this.addView(ListObjects[i]);

			final float obj_height = ListClearObjects[i].getBitmap().getHeight()*bmp_factor;
			final float obj_width = ListClearObjects[i].getBitmap().getWidth()*bmp_factor;
			ApplicationPictureItem pictureItem=(ApplicationPictureItem)levels[current_level_num].items.get(i);
			RectF rectF=new RectF(pictureItem.listX0*bmp_factor - obj_width/2f,
					Top + pictureItem.listY0*bmp_factor - obj_height / 2f,
					pictureItem.listX0*bmp_factor + obj_width/2f,
					Top + pictureItem.listY0*bmp_factor + obj_height / 2f);
			setBounds(ListObjects[i],rectF);
			((ApplicationImageView)ListObjects[i]).setStrokeWidth(Owner.getViewHeight()/200);
			((ApplicationImageView)ListObjects[i]).setRotateAngle(pictureItem.rotateAngle);
			((ApplicationImageView)ListObjects[i]).setImageBitmap(getImageFromObb(levels[current_level_num].items.get(i).pictureResource, ".png"),bmp_factor);
			//ListClearObjects[i].setBackgroundColor(Color.RED);
			//ListObjects[i].setDockPoint(CenterX -(levels[current_level_num].items.length*100*grid_scale)/2 + randomIndexes[i] * 100 * grid_scale,
			//		Owner.getViewHeight() - 100 * grid_scale);
			ApplicationPictureItem item= (ApplicationPictureItem) levels[current_level_num].items.get(i);
			setBounds(ListClearObjects[i],
					resultCenter + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2,
					resultCenter + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2 + obj_height
			);
			ListClearObjects[i].setRotation(pictureItem.rotateAngle);
			//ListClearObjects[i].setScaleType(ImageView.ScaleType.FIT_XY);
			{
				Animation animation = new AlphaAnimation(0, 1);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				ListClearObjects[i].startAnimation(animation);
				ListObjects[i].startAnimation(animation);
			}
		}
		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	void CreateLevelType6()
	{
		float grid_scale = bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
			/*if(levels[current_level_num].animatedBackground!=0) {
				GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
			}*/
		int[] objectsIndexes=getRandomIndexes(levels[current_level_num].items.size());
		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			//if (current_level_num != 29)
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			//else
			//	ListClearObjects[i] = newShadow2ImageView(levels[current_level_num].items[i].pictureResource);
			ListObjects[i] = new SymbolImageView(Owner,this,levels[current_level_num].items.get(i).colorResource);
			ListObjects[i].setImageBitmap(getImageFromObb(levels[current_level_num].items.get(i).pictureResource, ".png"),bmp_factor);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;
			this.addView(ListObjects[i]);

			float obj_height = ((WordLevelItem)levels[current_level_num]).getSymbolWidth()*bmp_factor;
			float obj_width = ((WordLevelItem)levels[current_level_num]).getSymbolWidth()*bmp_factor;
			float pos_y = (mRnd.nextInt(Owner.getViewHeight() - (int) obj_height));
			setBounds(ListObjects[i],
					(Owner.getViewWidth()-levels[current_level_num].items.size()*obj_width)/2f+objectsIndexes[i]*obj_width,
					Owner.getViewHeight()-obj_height,
					(Owner.getViewWidth()-levels[current_level_num].items.size()*obj_width)/2f+(objectsIndexes[i]+1)*obj_width,
					Owner.getViewHeight()
			);
			ListObjects[i].setDockPoint((Owner.getViewWidth()-levels[current_level_num].items.size()*obj_width)/2f+objectsIndexes[i]*obj_width,
					Owner.getViewHeight()-obj_height);
			setBounds(ListClearObjects[i],
					(Owner.getViewWidth()-levels[current_level_num].items.size()*obj_width)/2f+i*obj_width,
					(Owner.getViewHeight()-obj_height)/2f,
					(Owner.getViewWidth()-levels[current_level_num].items.size()*obj_width)/2f+(i+1)*obj_width,
					(Owner.getViewHeight()+obj_height)/2f
			);
			{
				Animation animation = new ScaleAnimation(0, 1.2f, 0, 1.2f);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				final int finalI = i;
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1);
						animation2.setDuration(500);
						ListClearObjects[finalI].startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				ListClearObjects[i].startAnimation(animation);
			}
		}

		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	void CreateLevelType7()
	{
		float grid_scale = bmp_factor;//(ListBackground.getDrawable().getIntrinsicHeight() * bmp_factor) / 620;
		float backWidth=ListBackground.getDrawable().getIntrinsicWidth()*(isScaledBitmaps()?2:1)/(ListBackground.getDrawable().getIntrinsicHeight()*(isScaledBitmaps()?2:1)/620f);
		setBounds(ListBackground, CenterX - backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top,
				CenterX + backWidth/*ListBackground.getDrawable().getIntrinsicWidth()*/ / 2f * bmp_factor,
				Top + 620/*ListBackground.getDrawable().getIntrinsicHeight()*/ * bmp_factor);
			/*if(levels[current_level_num].animatedBackground!=0) {
				GameView.this.makeAnimatedBackground(levels[current_level_num].animatedBackground);
			}*/
		int[] objectsIndexes=getRandomIndexes(levels[current_level_num].items.size());
		MoveToSidesLevelItem moveToSidesLevelItem=(MoveToSidesLevelItem)levels[current_level_num];
		int[] currentSizePositions=new int[moveToSidesLevelItem.sidesItems.length];
		for(int i=0;i<currentSizePositions.length;i++)
		{
			currentSizePositions[i]=0;
		}
		for (int i = 0; i < levels[current_level_num].items.size(); i++) {
			ListClearObjects[i] = newShadowImageView2(levels[current_level_num].items.get(i).pictureResource);
			ListObjects[i] = new SymbolImageView(Owner,this,levels[current_level_num].items.get(i).colorResource);
			ListObjects[i].setImageBitmap(getImageFromObb(levels[current_level_num].items.get(i).pictureResource, ".png"),bmp_factor);
			ListObjects[i].mClearObject = ListClearObjects[i];
			ListObjects[i].mObjectNumber = i;
			this.addView(ListObjects[i]);

			float obj_height =moveToSidesLevelItem.getSymbolWidth()*bmp_factor;
			float obj_width = moveToSidesLevelItem.getSymbolWidth()*bmp_factor;
			setBounds(ListObjects[i],
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2f,
					CenterX + levels[current_level_num].items.get(i).x0 * grid_scale + obj_width / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale + obj_height / 2f
			);
			ListObjects[i].setDockPoint(CenterX + levels[current_level_num].items.get(i).x0 * grid_scale - obj_width / 2f,
					Top + levels[current_level_num].items.get(i).y0 * grid_scale - obj_height / 2f);
			if(levels[current_level_num].items.get(i).colorResource==0) {
				float topPosition=(Owner.getViewHeight()-moveToSidesLevelItem.sidesItems[0]*obj_width)/2f;
				setBounds(ListClearObjects[i],
						0,
						topPosition + currentSizePositions[0]*obj_height,
						obj_width,
						topPosition + (currentSizePositions[0]+1)*obj_height
				);
			}
			if(levels[current_level_num].items.get(i).colorResource==1) {
				float topPosition=(Owner.getViewHeight()-moveToSidesLevelItem.sidesItems[1]*obj_width)/2f;
				setBounds(ListClearObjects[i],
						Owner.getViewWidth()-obj_width,
						topPosition + currentSizePositions[1]*obj_height,
						Owner.getViewWidth(),
						topPosition + (currentSizePositions[1]+1)*obj_height
				);
			}
			if(levels[current_level_num].items.get(i).colorResource==2) {
				float leftPosition=(Owner.getViewWidth()-moveToSidesLevelItem.sidesItems[2]*obj_width)/2f;
				setBounds(ListClearObjects[i],
						leftPosition + currentSizePositions[2]*obj_width,
						0,
						leftPosition + (currentSizePositions[2]+1)*obj_width,
						obj_height
				);
			}
			if(levels[current_level_num].items.get(i).colorResource==3) {
				float leftPosition=(Owner.getViewWidth()-moveToSidesLevelItem.sidesItems[3]*obj_width)/2f;
				setBounds(ListClearObjects[i],
						leftPosition + currentSizePositions[3]*obj_width,
						Owner.getViewHeight()-obj_height,
						leftPosition + (currentSizePositions[3]+1)*obj_width,
						Owner.getViewHeight()
				);
			}
			currentSizePositions[levels[current_level_num].items.get(i).colorResource]++;
			{
				Animation animation = new ScaleAnimation(0, 1.2f, 0, 1.2f);
				animation.setDuration(500);
				animation.setStartOffset(i * 200);
				final int finalI = i;
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1);
						animation2.setDuration(500);
						ListClearObjects[finalI].startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				ListClearObjects[i].startAnimation(animation);
			}
		}

		soundFromObb(levels[current_level_num].sound);
		CurrentScreenType = 1;
		start_time = System.currentTimeMillis();
		isStartingLevel = false;

	}

	public static int[] getRandomIndexes(int number) {
		int[] result = new int[number];

		for (int i = 0; i < number; i++)
			result[i] = -1;

		for (int i = 0; i < number; i++) {
			int index = mRnd.nextInt(number - i);
			int realIndex = 0;
			for (int j = 0; j < number; j++) {
				if (result[j] == -1) {
					if (realIndex == index) {
						result[j] = i;
						break;
					}
					realIndex++;
				}
			}
		}
		return result;
	}

	ImageView selectLevelsBackground=null;
		ImageView selectLevelsMask=null;
        private void ClearSelectLevels()
        {
         	for(int i=0;i<Levels.length;i++)
        	{
       			RecycleBitmap(Levels[i]);
        	}
			RecycleBitmap(selectLevelsBackground);
			RecycleBitmap(selectLevelsMask);
			RecycleBitmap(shtoraImageView);
        }

		private void startSelectLevel(final int nextScreen)
		{
			{
				if(nextScreen==3) {
					shtoraImageView = new ImageView(Owner);
					addView(shtoraImageView);
				}
				shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora2);
				AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
				animationDrawable.start();
				setBounds(shtoraImageView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
				long totalDuration = 0;
				for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
					totalDuration += animationDrawable.getDuration(i);
				}
				Timer timer = new Timer();
				TimerTask timerTask = new TimerTask(){
					@Override
					public void run() {
						shtoraImageView.post(new Runnable() {
							@Override
							public void run() {
								ClearMainMenu();
								if(nextScreen==3)
									ClearLevel();
								if(nextScreen==1 || nextScreen==3)
									MakeFourLevelsIcons();
								else if(nextScreen==2)
									InitSettings();
								RecycleBitmap(shtoraImageView);
								shtoraImageView = new ImageView(Owner);
								shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora1);
								addView(shtoraImageView);
								//shtoraImageView.bringToFront();
								//shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora1);
								AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
								animationDrawable.start();
								setBounds(shtoraImageView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
								long totalDuration = 0;
								for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
									totalDuration += animationDrawable.getDuration(i);
								}
								Timer timer = new Timer();
								TimerTask timerTask = new TimerTask(){
									@Override
									public void run() {
										shtoraImageView.post(new Runnable() {
											@Override
											public void run() {
												shtoraImageView.setBackgroundColor(Color.TRANSPARENT);
											}
										});
									}
								};
								timer.schedule(timerTask, totalDuration);
							}
						});
					}
				};
				timer.schedule(timerTask, totalDuration);
			}
		}
        private void MakeFourLevelsIcons()
        {
        	ClearSelectLevels();
			selectLevelsBackground=new ImageView(Owner);
			setBounds(selectLevelsBackground,0,0,Owner.getViewWidth(),Owner.getViewHeight());
			selectLevelsBackground.setBackgroundColor(Color.WHITE);
			addView(selectLevelsBackground);
        	float px=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        	float px2=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        	float width=Owner.getViewWidth()/2-px2-px;
        	float height=Owner.getViewHeight()/2-px;
			final float iMulWidth=Owner.getViewWidth()/1200f;
			final float iMulHeight=Owner.getViewHeight()/620f;
			final Rect[] levelsRects={
					new Rect(105,191,469,430),
					new Rect(415,41,780,280),
					new Rect(415,340,780,579),
					new Rect(732,191,1096,430),
			};
			Rect[] levelsExcludeRects={
					new Rect(402,191,469,288),
					new Rect(716,175,780,280),
					new Rect(415,340,485,446),
					new Rect(732,322,798,430),
			};
        	for(int i=0;i<levelsRects.length;i++)
        	{
        		if(select_level_part*4+i<this.levels.length)
        		{
					Rect excludeRect=new Rect(
							(int)((levelsExcludeRects[i].left-levelsRects[i].left)*iMulWidth),
							(int)((levelsExcludeRects[i].top-levelsRects[i].top)*iMulHeight),
							(int)((levelsExcludeRects[i].right-levelsRects[i].left)*iMulWidth),
							(int)((levelsExcludeRects[i].bottom-levelsRects[i].top)*iMulHeight));
        			Levels[i]=newLevelIcon(select_level_part*4+i,levelsRects[i].width()*iMulWidth,levelsRects[i].height()*iMulHeight,
							excludeRect);
        			int col=i/2;
        			int row=i%2;
        			int center_x=(int) (Owner.getViewWidth()/2+(col==0?-1:1)*(Levels[i].getDrawable().getIntrinsicWidth()/2+px/2));
        			int center_y=(int) (Owner.getViewHeight()/2+(row==0?-1:1)*(Levels[i].getDrawable().getIntrinsicHeight()/2+px/2));
           			setBounds(Levels[i],
							levelsRects[i].left*iMulWidth,
							levelsRects[i].top*iMulHeight,
							levelsRects[i].right*iMulWidth,
							levelsRects[i].bottom*iMulHeight
        					 );
					Levels[i].setScaleType(ImageView.ScaleType.FIT_XY);
					Animation animationAlpha=new AlphaAnimation(0,1);
					animationAlpha.setDuration(500);
					animationAlpha.setStartOffset(i*100);
					float x0=excludeRect.left;
					float y0=excludeRect.top;
					if(i==0 || i==2)
						y0=excludeRect.bottom;
					if(i==2 || i==3)
						x0=excludeRect.right;
					Animation animationScale=new ScaleAnimation(0.5f,1.04f,0.5f,1.04f,x0,y0);
					animationScale.setDuration(500);
					animationScale.setStartOffset(i*100);
					AnimationSet animationSet=new AnimationSet(true);
					animationSet.addAnimation(animationAlpha);
					animationSet.addAnimation(animationScale);
					final int finalI = i;
					final float finalX = x0;
					final float finalY = y0;
					animationSet.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							Animation animationScale=new ScaleAnimation(1.04f,1f,1.04f,1f, finalX, finalY);
							animationScale.setDuration(300);
							Levels[finalI].startAnimation(animationScale);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					Levels[i].startAnimation(animationSet);
         		}
        	}
			selectLevelsMask=newImageView(R.drawable.select_level_fon);
			selectLevelsMask.setScaleType(ImageView.ScaleType.FIT_XY);
			setBounds(selectLevelsMask,0,0,Owner.getViewWidth(),Owner.getViewHeight());
        	if(select_level_part>0)
        	{
        		Levels[4]=newImageView(R.drawable.prev);
        		Levels[4].setScaleType(ImageView.ScaleType.FIT_XY);
        		setBounds(Levels[4],
            			0,
            			Owner.getViewHeight()/2-px2,
            			px2,
            			Owner.getViewHeight()/2+px2
            			);
	    		//if(BlinkType)
	    		{
	    			setBlinkView(Levels[4]);
	    		}
        	}
    		if(select_level_part*4+3<this.levels.length)
    		{
        		Levels[5]=newImageView(R.drawable.next);
        		Levels[5].setScaleType(ImageView.ScaleType.FIT_XY);
            	setBounds(Levels[5],
            			Owner.getViewWidth()-px2,
            			Owner.getViewHeight()/2-px2,
            			Owner.getViewWidth(),
            			Owner.getViewHeight()/2+px2
            			);
	    		//if(BlinkType)
	    		{
	    			setBlinkView(Levels[5]);
	    		}
    		}
       }

        private Bitmap MakeLevelIcon(int num,float width,float height,Rect excludeRect)
        {
			BitmapFactory.Options opts1=new BitmapFactory.Options();
			opts1.inScaled=false;
			opts1.inSampleSize=4;

			//Bitmap ListBackground=Bitmap.createBitmap((int)width, (int)height, Config.ARGB_8888);
        	Bitmap ret=Bitmap.createBitmap((int)width, (int)height, Config.ARGB_8888);
            Bitmap ListObjects[]=new Bitmap[levels[num].items.size()];
            RectF ListObjectsRect[]=new RectF[levels[num].items.size()];
        	Bitmap ListBackground1=getImageFromObb(levels[num].list, ".png");//, opts1);
			Bitmap Background1=getImageFromObb(levels[num].background, ".jpg");//, opts1);

			float screen_factor,bmp_factor,CenterX,Top;
    		//Resources r=getResources();
        	float py=height/(float)ListBackground1.getHeight();
        	float px=width/(float)ListBackground1.getWidth();
        	if(px<py)
        		screen_factor=px;
        	else
        		screen_factor=py;
        	bmp_factor=screen_factor;//620f/ListBackground.getHeight();///(620f/screen_factor);
        	float grid_scale=(ListBackground1.getHeight()*bmp_factor)/620;
        	CenterX=width/2f;
        	Top=(height-bmp_factor*ListBackground1.getHeight())/2f;

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
        	for(int i=0;i<levels[num].items.size();i++)
        	{
        		/*if(PagesObjects[i+first_object_num].equals("-"))
        			break;
        		objects_num++;*/
        		ListObjects[i]=getImageFromObb(levels[num].items.get(i).pictureResource, ".png");//, opts1);

            	float obj_height=ListObjects[i].getHeight()*bmp_factor;
            	float obj_width=ListObjects[i].getWidth()*bmp_factor;
				if(levels[num].levelType==6)
				{
					obj_height=obj_width=((WordLevelItem)levels[num]).getSymbolWidth()*bmp_factor;
				}
				if(levels[num].levelType==7)
				{
					obj_height=obj_width=((MoveToSidesLevelItem)levels[num]).getSymbolWidth()*bmp_factor;
				}
            	ListObjectsRect[i]=new RectF(
        				CenterX+levels[num].items.get(i).x0*grid_scale-obj_width/2,
        				Top+levels[num].items.get(i).y0*grid_scale-obj_height/2,
        				CenterX+levels[num].items.get(i).x0*grid_scale+obj_width/2,
        				Top+levels[num].items.get(i).y0*grid_scale-obj_height/2+obj_height
        				);
        	}
        	Paint paint=new Paint();
        	Paint paint2=new Paint();
        	paint2.setColor(Color.WHITE);
        	//paint2.setAlpha(0x80);
        	Canvas canvas=new Canvas(ret);
			canvas.clipRect(excludeRect,  Region.Op.DIFFERENCE);
        	canvas.drawRect(0, 0, ret.getWidth(), ret.getHeight(), paint2);
			canvas.drawBitmap(Background1,null,new RectF(0,0,width,height),paint);
			if(levels[num].levelType!=5)
    			canvas.drawBitmap(ListBackground1, null,
    				new RectF(CenterX-ListBackground1.getWidth()/2f*bmp_factor,
    						Top,
    						CenterX+ListBackground1.getWidth()/2f*bmp_factor,
    						Top+ListBackground1.getHeight()*bmp_factor
    						), paint);
        	for(int i=0;i<levels[num].items.size();i++)
        	{
				if(levels[num].levelType!=5)
        			canvas.drawBitmap(ListObjects[i], null,ListObjectsRect[i], paint);
				else
				{
					canvas.save();
					canvas.rotate(((ApplicationPictureItem)levels[num].items.get(i)).rotateAngle,
							ListObjectsRect[i].left+ListObjectsRect[i].width()/2f,
							ListObjectsRect[i].top+ListObjectsRect[i].height()/2f);
					canvas.drawBitmap(ListObjects[i], null,ListObjectsRect[i], paint);
					canvas.restore();
				}
        	}
        	for(int i=0;i<ListObjects.length;i++)
        	{
        		if(ListObjects[i]!=null)
        			ListObjects[i].recycle();
        		ListObjects[i]=null;
        	}
        	ListBackground1.recycle();
        	return ret;
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
        
        private Bitmap MakeShadowBitmap(Bitmap bmp)
        {
    		int max_pos=bmp.getWidth()*bmp.getHeight();
    		int pixels1[]=new int[max_pos];
    		int pixels2[]=new int[max_pos];
    		Bitmap tmp_bmp=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
    		bmp.getPixels(pixels1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		for(int y=0;y<bmp.getHeight();y++)
    		{
    			int row_pos=y*bmp.getWidth();
				int prev_row_pos=(y-1)*bmp.getWidth();
				int prev_prev_row_pos=(y-2)*bmp.getWidth();
				int prev_prev_prev_row_pos=(y-3)*bmp.getWidth();
				int next_row_pos=(y+1)*bmp.getWidth();
				int next_next_row_pos=(y+2)*bmp.getWidth();
				int next_next_next_row_pos=(y+3)*bmp.getWidth();
    			for(int x=0;x<bmp.getWidth();x++)
    			{
    				int pixel=pixels1[row_pos+x];
    				if(pixel!=Color.TRANSPARENT)
    				{
    					/*int tr=(pixel&0xff000000)>>24;
    					tr=0x40-tr/4;
    					pixels2[row_pos+x]=(tr<<24);*/
						boolean isPrevRowTransparent=true;
						boolean isPrevPrevRowTransparent=true;
						boolean isPrevPrevPrevRowTransparent=true;
						if(prev_row_pos>=0)
							isPrevRowTransparent=pixels1[prev_row_pos+x]==Color.TRANSPARENT;
						if(prev_prev_row_pos>=0)
							isPrevPrevRowTransparent=pixels1[prev_prev_row_pos+x]==Color.TRANSPARENT;
						if(prev_prev_prev_row_pos>=0)
							isPrevPrevPrevRowTransparent=pixels1[prev_prev_prev_row_pos+x]==Color.TRANSPARENT;
						boolean isPrevPosTransparent=true;
						boolean isPrevPrevPosTransparent=true;
						boolean isPrevPrevPrevPosTransparent=true;
						if(x>0)
							isPrevPosTransparent=pixels1[row_pos+x-1]==Color.TRANSPARENT;
						if(x>1)
							isPrevPrevPosTransparent=pixels1[row_pos+x-2]==Color.TRANSPARENT;
						if(x>2)
							isPrevPrevPrevPosTransparent=pixels1[row_pos+x-3]==Color.TRANSPARENT;

						boolean isNextRowTransparent=true;
						boolean isNextNextRowTransparent=true;
						boolean isNextNextNextRowTransparent=true;
						if(y<bmp.getHeight()-1)
							isNextRowTransparent=pixels1[next_row_pos+x]==Color.TRANSPARENT;
						if(y<bmp.getHeight()-2)
							isNextNextRowTransparent=pixels1[next_next_row_pos+x]==Color.TRANSPARENT;
						if(y<bmp.getHeight()-3)
							isNextNextNextRowTransparent=pixels1[next_next_next_row_pos+x]==Color.TRANSPARENT;
						boolean isNextPosTransparent=true;
						boolean isNextNextPosTransparent=true;
						boolean isNextNextNextPosTransparent=true;
						if(x<bmp.getWidth()-1)
							isNextPosTransparent=pixels1[row_pos+x+1]==Color.TRANSPARENT;
						if(x<bmp.getWidth()-2)
							isNextNextPosTransparent=pixels1[row_pos+x+2]==Color.TRANSPARENT;
						if(x<bmp.getWidth()-3)
							isNextNextNextPosTransparent=pixels1[row_pos+x+3]==Color.TRANSPARENT;

						if(isPrevPosTransparent || isPrevPrevPosTransparent || isPrevPrevPrevRowTransparent || isPrevRowTransparent || isPrevPrevRowTransparent || isPrevPrevPrevPosTransparent ||
								isNextPosTransparent || isNextNextPosTransparent || isNextNextNextRowTransparent || isNextRowTransparent || isNextNextRowTransparent || isNextNextNextPosTransparent)
							pixels2[row_pos+x]=(pixel&0xff000000);//|0x8f000000;//|0x00ffffff;
						else
    						pixels2[row_pos+x]=(pixel&0xbf000000);
    				}
    				else
    					pixels2[row_pos+x]=Color.TRANSPARENT;
    			}
    		}
    		tmp_bmp.setPixels(pixels2, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		return tmp_bmp;
        }
        
        private Bitmap MakeShadowBitmap2(Bitmap bmp)
        {
    		int max_pos=bmp.getWidth()*bmp.getHeight();
    		int pixels1[]=new int[max_pos];
    		int pixels2[]=new int[max_pos];
    		Bitmap tmp_bmp=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
    		bmp.getPixels(pixels1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		for(int y=0;y<bmp.getHeight();y++)
    		{
    			int row_pos=y*bmp.getWidth();
    			for(int x=0;x<bmp.getWidth();x++)
    			{
    				int pixel=pixels1[row_pos+x];
    				if(pixel!=Color.TRANSPARENT)
    				{
    					/*int r=Color.red(pixel)*2/3;
    					int g=Color.green(pixel)*2/3;
    					int b=Color.blue(pixel)*2/3;
    					int a=Color.alpha(pixel);*/
    					pixels2[row_pos+x]=pixel;//Color.argb(a, r, g, b);
    				}
    				else
    					pixels2[row_pos+x]=Color.TRANSPARENT;
    			}
    		}
    		tmp_bmp.setPixels(pixels2, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		return tmp_bmp;
        }
        
        private Bitmap MakeWhiteBitmap(Bitmap bmp)
        {
    		int max_pos=bmp.getWidth()*bmp.getHeight();
    		int pixels1[]=new int[max_pos];
    		int pixels2[]=new int[max_pos];
    		Bitmap tmp_bmp=Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
    		bmp.getPixels(pixels1, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		for(int y=0;y<bmp.getHeight();y++)
    		{
    			int row_pos=y*bmp.getWidth();
    			for(int x=0;x<bmp.getWidth();x++)
    			{
    				int pixel=pixels1[row_pos+x];
    				if(pixel!=Color.TRANSPARENT)
    					pixels2[row_pos+x]=(pixel&0xff000000)|0x00ffffff;
    				else
    					pixels2[row_pos+x]=Color.TRANSPARENT;
    			}
    		}
    		tmp_bmp.setPixels(pixels2, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    		return tmp_bmp;
        }
        
        private void Vibrate(int val)
        {
        	if(VibrationType && vibrator!=null)
        		vibrator.vibrate(val);
        }
                
        private void StageComplete()
        {
			soundFromObb("change_level");
			for(int i=0;i<ListObjects.length;i++)
			{
				if(ListObjects[i]!=null)
					ListObjects[i].clearAnimation();
			}
			if(GlassView!=null)
				this.removeView(GlassView);
			GlassView=null;
	        //GameFrame=newImageView(R.drawable.frame);
	        //GameFrame.setVisibility(View.INVISIBLE);
	        GameButtonMoon=newImageView(R.drawable.prev);
	        GameButtonFlower=newImageView(R.drawable.refresh);
	        GameButtonSunshine=newImageView(R.drawable.next);
    		final int height=(int) (Owner.getViewHeight()*0.20f);
    		int left=Owner.getViewWidth()/4-height/2;
    		int top=(int) (Owner.getViewHeight()*0.8f);
    		int center=Owner.getViewWidth()/2-height/2;
    		int right=Owner.getViewWidth()*3/4-height/2;

    		setBounds(GameButtonMoon,
    				left,
    				top,
    				left+height,
    				top+height);
    		setBounds(GameButtonFlower,
					center,
					top,
					center + height,
					top + height);
    		setBounds(GameButtonSunshine,
					right,
					top,
					right + height,
					top + height);

			{
				Animation animation = new TranslateAnimation(0, 0, Owner.getViewHeight() * 0.2f, -height * 0.5f);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new TranslateAnimation(0, 0, -height * 0.5f, 0);
						animation2.setDuration(200);
						GameButtonMoon.startAnimation(animation2);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				animation.setDuration(500);
				GameButtonMoon.startAnimation(animation);
			}
			{
				Animation animation = new TranslateAnimation(0, 0, Owner.getViewHeight() * 0.2f, -height * 0.5f);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) { }
					@Override
					public void onAnimationRepeat(Animation animation) { }

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new TranslateAnimation(0, 0, -height * 0.5f, 0);
						animation2.setDuration(200);
						GameButtonFlower.startAnimation(animation2);
					}

				});
				animation.setDuration(500);
				animation.setStartOffset(200);
				GameButtonFlower.startAnimation(animation);
			}
			{
				Animation animation = new TranslateAnimation(0, 0, Owner.getViewHeight() * 0.2f, -height * 0.5f);
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) { }
					@Override
					public void onAnimationRepeat(Animation animation) { }

					@Override
					public void onAnimationEnd(Animation animation) {
						Animation animation2 = new TranslateAnimation(0, 0, -height * 0.5f, 0);
						animation2.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) { }
							@Override
							public void onAnimationRepeat(Animation animation) { }

							@Override
							public void onAnimationEnd(Animation animation) {
								//if (BlinkType)
								{
									//setBlinkView(GameButtonMoon);
									//setBlinkView(GameButtonFlower);
									//setBlinkView(GameButtonSunshine);
									{
										Animation animation3=new TranslateAnimation(0,-height/3,0,0);
										animation3.setDuration(1000);
										animation3.setRepeatMode(Animation.REVERSE);
										animation3.setRepeatCount(Animation.INFINITE);
										GameButtonMoon.startAnimation(animation3);
									}
									{
										Animation animation3=new RotateAnimation(-5,5,height/2f,height/2f);
										animation3.setDuration(1000);
										animation3.setRepeatMode(Animation.REVERSE);
										animation3.setRepeatCount(Animation.INFINITE);
										GameButtonFlower.startAnimation(animation3);
									}
									{
										Animation animation3 = new TranslateAnimation(0, height / 3, 0, 0);
										animation3.setDuration(1000);
										animation3.setRepeatMode(Animation.REVERSE);
										animation3.setRepeatCount(Animation.INFINITE);
										GameButtonSunshine.startAnimation(animation3);
									}
								}
							}

						});
						animation2.setDuration(200);
						GameButtonSunshine.startAnimation(animation2);
					}

				});
				animation.setDuration(500);
				animation.setStartOffset(400);
				GameButtonSunshine.startAnimation(animation);
			}
    		/*if(BlinkType)
    		{
    			setBlinkView(GameButtonMoon);
    			setBlinkView(GameButtonFlower);
    			setBlinkView(GameButtonSunshine);
    		}*/
        }
        
        private void setBlinkView(View v)
        {
			Animation anim = new AlphaAnimation(0.5f, 1f);
			anim.setDuration(1000); 
			//anim.setStartOffset(500);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(Animation.INFINITE);
			v.startAnimation(anim);
        }
       
        private void hideAnimation(View v)
        {
            if(v==null)
                return;
			Animation anim = new AlphaAnimation(1f, 0f);
			anim.setDuration(1000); 
			//anim.setStartOffset(500);
			//anim.setRepeatMode(Animation.REVERSE);
			//anim.setRepeatCount(Animation.INFINITE);
			anim.setFillAfter(true);
			v.startAnimation(anim);
        }
       
        public ImageView newImageView(int res_id)
        {
        	ImageView image=new ImageView(Owner);
        	image.setImageResource(res_id);
			this.addView(image);
        	return image;
        }

	public ImageView newImageView(Bitmap bmp)
	{
		ImageView image=new ImageView(Owner);
		image.setImageBitmap(bmp);
		this.addView(image);
		return image;
	}

	@SuppressLint("NewApi")
		public static void setAlpha(View view, float alpha)
        {
            if (Build.VERSION.SDK_INT < 11)
            {
                final AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
                animation.setDuration(0);
                animation.setFillAfter(true);
                view.startAnimation(animation);
            }
            else view.setAlpha(alpha);
        }        
        
        //@SuppressLint("NewApi")
		public ImageView newShadowImageView(String bmpName)
        {
        	ImageView image=new ImageView(Owner);
            Bitmap bmp=MakeShadowBitmap(getImageFromObb(bmpName, ".png"));
            image.setImageBitmap(bmp);
            //setAlpha(image, 0.5f);
			this.addView(image);
        	return image;
        }
	public ShadowImageView newShadowImageView2(String bmpName)
	{
		ShadowImageView image=new ShadowImageView(Owner);
		Bitmap bmp=MakeShadowBitmap(getImageFromObb(bmpName, ".png"));
		image.setShadowBitmap(bmp,Color.BLACK);
		//setAlpha(image, 0.5f);
		this.addView(image);
		return image;
	}

	public ImageView newShadowImageView(int res_id)
	{
		ImageView image=new ImageView(Owner);
		Bitmap bmp=MakeShadowBitmap(BitmapFactory.decodeResource(getResources(), res_id));
		image.setImageBitmap(bmp);
		setAlpha(image, 0.5f);
		this.addView(image);
		return image;
	}


	//@SuppressLint("NewApi")
		public ImageView newShadow2ImageView(String bmpName)
        {
        	ImageView image=new ImageView(Owner);
			Bitmap bmp=getImageFromObb(bmpName, ".png");
			image.setImageBitmap(bmp);
            setAlpha(image, 0.7f);
			this.addView(image);
        	return image;
        }
        
        public ImageView newWhiteImageView(int res_id)
        {
        	ImageView image=new ImageView(Owner);
            Bitmap bmp=MakeWhiteBitmap(BitmapFactory.decodeResource(getResources(), res_id));
            image.setImageBitmap(bmp);
			this.addView(image);
        	return image;
        }
        
        public ImageView newLevelIcon(int res_id,float width,float height,Rect excludeRect)
        {
        	ImageView image=new ImageView(Owner);
            Bitmap bmp=MakeLevelIcon(res_id, width, height,excludeRect);
            image.setImageBitmap(bmp);
			this.addView(image);
        	return image;
        }
        
        public void setBounds(View image,float left,float top,float right,float bottom)
        {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.leftMargin = (int)left;
			lp.topMargin = (int)top;
			lp.width=(int)(right-left);
			lp.height=(int)(bottom-top);
			if(right>=Owner.getViewWidth())
				lp.rightMargin= (int) (Owner.getViewWidth()-right-1);
			if(bottom>=Owner.getViewHeight())
				lp.bottomMargin= (int) (Owner.getViewHeight()-bottom-1);
			image.setLayoutParams(lp);
        }

	public void setBounds(View image,RectF rectF)
	{
		setBounds(image,rectF.left,rectF.top,rectF.right,rectF.bottom);
	}

		ImageView babochkaImageView=null;
		ImageView shtoraImageView=null;
        private void InitMainMenu()
        {
        	this.setBackgroundResource(R.drawable.start_fone2);
			float cvetyImul=Owner.getViewHeight()/720f;
			float starImul=Owner.getViewWidth()/1024f;
			float starSize=450f*cvetyImul;
			StartStar1Image=newImageView(R.drawable.menu_star2);
			setBounds(StartStar1Image,867*starImul-starSize/2,126*cvetyImul-starSize/2,867*starImul+starSize/2,126*cvetyImul+starSize/2);
			{
				Animation animation=new AlphaAnimation(0.2f,1f);
				animation.setDuration(1000);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartStar1Image.startAnimation(animation);
			}
			StartStar2Image=newImageView(R.drawable.menu_star2);
			setBounds(StartStar2Image,128*starImul-starSize/2,90*cvetyImul-starSize/2,128*starImul+starSize/2,90*cvetyImul+starSize/2);
			{
				Animation animation=new AlphaAnimation(0.2f,1f);
				animation.setDuration(500);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartStar2Image.startAnimation(animation);
			}
			StartStar3Image=newImageView(R.drawable.menu_star2);
			setBounds(StartStar3Image,669*starImul-starSize/2,276*cvetyImul-starSize/2,669*starImul+starSize/2,276*cvetyImul+starSize/2);
			{
				Animation animation=new AlphaAnimation(0.2f,1f);
				animation.setDuration(1200);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartStar3Image.startAnimation(animation);
			}
			StartStar4Image=newImageView(R.drawable.menu_star);
			setBounds(StartStar4Image,360*starImul-starSize/8,127*cvetyImul-starSize/8,360*starImul+starSize/8,127*cvetyImul+starSize/8);
			{
				Animation animation=new AlphaAnimation(0.2f,1f);
				animation.setDuration(300);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartStar4Image.startAnimation(animation);
			}
			StartStar5Image=newImageView(R.drawable.menu_star);
			setBounds(StartStar5Image,818*starImul-starSize/8,309*cvetyImul-starSize/8,818*starImul+starSize/8,309*cvetyImul+starSize/8);
			{
				Animation animation=new AlphaAnimation(0.2f,1f);
				animation.setDuration(400);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartStar5Image.startAnimation(animation);
			}
        	StartGameButton=new ImageView(Owner);
			StartGameButton.setBackgroundResource(R.drawable.start_button_anim);
			addView(StartGameButton);
			AnimationDrawable startAnimation=(AnimationDrawable)StartGameButton.getBackground();
			startAnimation.start();
        	//LevelGameButton=newImageView(R.drawable.level);
			StartListiyaImage=newImageView(R.drawable.start_listiya);
			setBounds(StartListiyaImage,-10*cvetyImul,Owner.getViewHeight()-507*cvetyImul+10*cvetyImul,556*cvetyImul-10*cvetyImul,Owner.getViewHeight()+10*cvetyImul);
			{
				Animation animation=new RotateAnimation(0,7f,0,507*cvetyImul);
				animation.setDuration(3000);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartListiyaImage.startAnimation(animation);
			}
			StartCvetyImage=newImageView(R.drawable.start_cvety2);
			setBounds(StartCvetyImage,+10*cvetyImul,Owner.getViewHeight()-666*cvetyImul+10*cvetyImul,491*cvetyImul+10*cvetyImul,Owner.getViewHeight()+10*cvetyImul);
			{
				Animation animation=new RotateAnimation(0,5f,0,666*cvetyImul);
				animation.setDuration(2000);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartCvetyImage.startAnimation(animation);
			}
			StartListikImage=newImageView(R.drawable.start_listik);
			setBounds(StartListikImage,-10*cvetyImul,Owner.getViewHeight()-324*cvetyImul+10*cvetyImul,218*cvetyImul-10*cvetyImul,Owner.getViewHeight()+10*cvetyImul);
			{
				Animation animation=new RotateAnimation(0,3f,0,324*cvetyImul);
				animation.setDuration(2500);
				animation.setRepeatMode(Animation.REVERSE);
				animation.setRepeatCount(Animation.INFINITE);
				StartListikImage.startAnimation(animation);
			}
			SettingsButton=new ImageView(Owner);
			SettingsButton.setBackgroundResource(R.drawable.animaciya_setting);
			addView(SettingsButton);
			AnimationDrawable settingsAnimation=(AnimationDrawable)SettingsButton.getBackground();
			settingsAnimation.start();
        	//SettingsButton=newImageView(R.drawable.settings);
			ExitGameButton=new ImageView(Owner);
			ExitGameButton.setBackgroundResource(R.drawable.exit);
			addView(ExitGameButton);
			AnimationDrawable exitAnimation=(AnimationDrawable)ExitGameButton.getBackground();
			exitAnimation.start();
        	//ExitGameButton=newImageView(R.drawable.exit);
        	int center_h=(Owner.getViewHeight()/*-ExitGameButton.getDrawable().getIntrinsicHeight()*/)/2;
        	float px=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        	int empty_height=Owner.getViewHeight()-(/*MainMenuCaption.getHeight()+*/ExitGameButton.getHeight());
        	float imul=1f;
			float startButtonWidth=540*1.25f;
			float startButtonHeight=458*1.25f;
        	if(empty_height<startButtonHeight)
        	{
        		imul=(float)empty_height/(float)(startButtonHeight);
   			}
        	//setBounds(MainMenuCaption,(int)((Owner.getViewWidth()-MainMenuCaption.getDrawable().getIntrinsicWidth()*imul)/2), 0, (int)(Owner.getViewWidth()+MainMenuCaption.getDrawable().getIntrinsicWidth()*imul)/2,  MainMenuCaption.getDrawable().getIntrinsicHeight()*imul);
        	setBounds(StartGameButton,(int)((Owner.getViewWidth()-startButtonWidth*imul)/2), (int)(center_h-startButtonHeight*imul/2), (int)(Owner.getViewWidth()+startButtonWidth*imul)/2,  center_h+startButtonHeight*imul/2);
            //setBounds(LevelGameButton,(int)((Owner.getViewWidth()-LevelGameButton.getDrawable().getIntrinsicWidth()*imul)/2), center_h, (int)(Owner.getViewWidth()+LevelGameButton.getDrawable().getIntrinsicWidth()*imul)/2,  (int)(center_h+LevelGameButton.getDrawable().getIntrinsicHeight()*imul));
            //int botton_buttons_width=/*ExitGameButton.getDrawable().getIntrinsicWidth()+*/SettingsButton.getDrawable().getIntrinsicWidth()+(int)px;
            setBounds(SettingsButton, 0, 0, 187*cvetyImul, 130*cvetyImul);
            setBounds(ExitGameButton, Owner.getViewWidth()-118*cvetyImul, 0, Owner.getViewWidth(), 130*cvetyImul);
			babochkaImageView=new ImageView(Owner);
			babochkaImageView.setBackgroundResource(R.drawable.animaciya_babochka);
			addView(babochkaImageView);
			AnimationDrawable babochkaAnimation=(AnimationDrawable)babochkaImageView.getBackground();
			babochkaAnimation.start();
			setBounds(babochkaImageView, 823*starImul-102*cvetyImul, 560*cvetyImul-64*cvetyImul, 823*starImul+102*cvetyImul, 560*cvetyImul+64*cvetyImul);
			babochkaImageView.setRotation(37);

			{
				//if(shtoraImageView!=null)
				//	((ViewGroup)shtoraImageView.getParent()).removeView(shtoraImageView);
				shtoraImageView=null;
				shtoraImageView = new ImageView(Owner);
				//Bitmap bmp=BitmapFactory.decodeResource(getResources(),R.drawable.animaciya_shtora11);
				shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora1);
				addView(shtoraImageView);
				AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
				/*animationDrawable.setAnimationFinishListener(new AnimationDrawable2.IAnimationFinishListener() {
					@Override
					public void onAnimationFinished() {
						removeView(shtoraImageView);
					}
				});*/
				animationDrawable.start();
				setBounds(shtoraImageView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
				long totalDuration = 0;
				for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
					totalDuration += animationDrawable.getDuration(i);
				}
				Timer timer = new Timer();
				TimerTask timerTask = new TimerTask(){
					@Override
					public void run() {
						shtoraImageView.post(new Runnable() {
							@Override
							public void run() {
								shtoraImageView.setBackgroundColor(Color.TRANSPARENT);
							}
						});
					}
				};
				timer.schedule(timerTask, totalDuration);
			}
      }
        
        private void ClearMainMenu()
        {
        	//if(Owner.buyButton!=null)
        	//	Owner.buyButton.setVisibility(View.INVISIBLE);
        	//if(Owner.btnSoobrazhalka2!=null)
        	//	Owner.btnSoobrazhalka2.setVisibility(View.INVISIBLE);
        	//if(Owner.btnMozaik!=null)
        	//	Owner.btnMozaik.setVisibility(View.INVISIBLE);
            //if(Owner.btnAzbuka!=null)
            //    Owner.btnAzbuka.setVisibility(View.INVISIBLE);
         	//RecycleBitmap(MainMenuCaption);
        	RecycleBitmap(StartGameButton);
        	//RecycleBitmap(LevelGameButton);
        	RecycleBitmap(SettingsButton);
        	RecycleBitmap(ExitGameButton);
			RecycleBitmap(StartCvetyImage);
			RecycleBitmap(StartListikImage);
			RecycleBitmap(StartListiyaImage);
			RecycleBitmap(StartStar1Image);
			RecycleBitmap(StartStar2Image);
			RecycleBitmap(StartStar3Image);
			RecycleBitmap(StartStar4Image);
			RecycleBitmap(StartStar5Image);
			RecycleBitmap(babochkaImageView);
			RecycleBitmap(shtoraImageView);
        }
        
        private void InitSettings()
        {
			ClearMainMenu();
	        BukvyTypeSettings=new ImageView(Owner);
			BukvyTypeSettings.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.addView(BukvyTypeSettings);
	        VibrationSettings=new ImageView(Owner);
			BukvyTypeSettings.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.addView(VibrationSettings);
			StarsSettings=new ImageView(Owner);
			StarsSettings.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.addView(StarsSettings);
			AnimateBackgroundSettings=new ImageView(Owner);
			AnimateBackgroundSettings.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.addView(AnimateBackgroundSettings);
			AnimateBackgroundSettings2=new ImageView(Owner);
			AnimateBackgroundSettings2.setScaleType(ImageView.ScaleType.FIT_CENTER);
			this.addView(AnimateBackgroundSettings2);

        	//int px=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
			float iMul=Owner.getViewWidth()/1200f;
			setBounds(VibrationSettings,
					0,
					(Owner.getViewHeight()/2-iMul*259f)/2f,
					Owner.getViewWidth()/2f,
					(Owner.getViewHeight()/2+iMul*259f)/2f);
			setBounds(BukvyTypeSettings,
					Owner.getViewWidth()/2f,
					(Owner.getViewHeight()/2-iMul*259f)/2f,
					Owner.getViewWidth(),
					(Owner.getViewHeight()/2+iMul*259f)/2f);
			setBounds(StarsSettings,
					0,
					(Owner.getViewHeight()*3/2-iMul*259f)/2f,
					Owner.getViewWidth()/2f,
					(Owner.getViewHeight()*3/2+iMul*259f)/2f);
			setBounds(AnimateBackgroundSettings,
					Owner.getViewWidth()/2f,
					(Owner.getViewHeight()*3/2-iMul*259f)/2f,
					Owner.getViewWidth(),
					(Owner.getViewHeight()*3/2+iMul*259f)/2f);
			setBounds(AnimateBackgroundSettings2,
					Owner.getViewWidth()/2f+(Owner.getViewWidth()/2f-iMul*204f)/2,
					(Owner.getViewHeight()*3/2-iMul*158f)/2f,
					Owner.getViewWidth()/2f+(Owner.getViewWidth()/2f+iMul*204f)/2,
					(Owner.getViewHeight()*3/2+iMul*158f)/2f);
			AnimateBackgroundSettings.setImageResource(R.drawable.animaciya_stars00);
    		SetVibrationType(VibrationType);
    		SetBukvyType(BukvyType);
			SetStars(isStars);
			SetAnimateBackground(isAnimateBackground);
			{
				shtoraImageView = new ImageView(Owner);
				shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora2);
				addView(shtoraImageView);
				AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
				animationDrawable.start();
				setBounds(shtoraImageView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
				long totalDuration = 0;
				for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
					totalDuration += animationDrawable.getDuration(i);
				}
				Timer timer = new Timer();
				TimerTask timerTask = new TimerTask(){
					@Override
					public void run() {
						shtoraImageView.post(new Runnable() {
							@Override
							public void run() {
								shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora1);
								AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
								animationDrawable.start();
								long totalDuration = 0;
								for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
									totalDuration += animationDrawable.getDuration(i);
								}
								Timer timer = new Timer();
								TimerTask timerTask = new TimerTask(){
									@Override
									public void run() {
										shtoraImageView.post(new Runnable() {
											@Override
											public void run() {
												shtoraImageView.setBackgroundColor(Color.TRANSPARENT);
											}
										});
									}
								};
								timer.schedule(timerTask, totalDuration);
							}
						});
					}
				};
				timer.schedule(timerTask, totalDuration);
			}

		}

	private void SetStars(boolean type)
	{
		isStars=type;
		StarsSettings.setImageResource(R.drawable.animaciya_stars00);
		if(type) {
			StarsSettings.setBackgroundResource(R.drawable.animaciya_stars);
			AnimationDrawable animation=(AnimationDrawable)StarsSettings.getBackground();
			animation.start();
		}
		else
			StarsSettings.setBackgroundColor(Color.TRANSPARENT);
	}

	private void SetAnimateBackground(boolean type)
	{
		isAnimateBackground=type;
		if(type) {
			AnimateBackgroundSettings2.setBackgroundResource(R.drawable.animaciya_babochka_1);
			AnimationDrawable animation=(AnimationDrawable)AnimateBackgroundSettings2.getBackground();
			animation.start();
		}
		else
			AnimateBackgroundSettings2.setBackgroundColor(Color.TRANSPARENT);
	}

	private void SetVibrationType(boolean type)
        {
        	VibrationType=type;
			VibrationSettings.setImageResource(R.drawable.no_vibration);
			if(type) {
				VibrationSettings.setBackgroundResource(R.drawable.animaciya_vibraciya);
				AnimationDrawable animation=(AnimationDrawable)VibrationSettings.getBackground();
				animation.start();
			}
			else
				VibrationSettings.setBackgroundColor(Color.TRANSPARENT);
        }
        
		private void SetBukvyType(int type)
        {
        	BukvyType=type;
			if(type==0)
				BukvyTypeSettings.setBackgroundResource(R.drawable.settings_zvuki);
			else
				BukvyTypeSettings.setBackgroundResource(R.drawable.settings_bukvy);
        	//setAlpha(BlinkSettingsShadow, type ? 0f : 1f);
        }
        
        public void SetCurrentScreenType(int type)
        {
        	CurrentScreenType=type;
        }
        
        public void SmallClick()
        {
       		Vibrate(50);
        }
        
        /*public void Play(int resource, boolean is_looping) {
        	try
        	{
                Stop();

                  mp = MediaPlayer.create(getContext(), resource);
                  mp.setLooping(is_looping);
                  mp.start();
        	}
        	catch(Exception ex)
        	{
        		
        	}
        }*/
        
        public void Pause() {
        	//isWasStoped=true;
        	/*try
        	{
                if (mp != null) {
                    mp.pause();
                 }
          	}
          	catch(Exception ex)
          	{
          		
          	}*/
        }
        
        public void Resume() {
        	try
        	{
            	/*StartLoop=24;
                if (mp != null) {
                   mp.start();
                }
                if (mp != null) {
                    mp.pause();
                 }
            	if(isWasStoped==true)
            	{
            		mTimerTask=null;
            		setRunning(true);
            	}*/
          	}
          	catch(Exception ex)
          	{
          		
          	}
         }

        /*public void Stop() {
        	try
        	{
                if (mp != null) 
                {
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
            catch(Exception ex)
            {
            		
            }
        }*/
        
        public void Exit()
        {
			CurrentScreenType=1000;
			Owner.StartNextStage(0);
			/*soundFromObb("vyhod");
			SmallClick();
   			new AlertDialog.Builder(Owner).
	   		setTitle("Вопрос").
	   		setMessage("Выйти из игры?").
	   		setPositiveButton("Выйти",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(
						DialogInterface arg0, int arg1) {
	            	android.os.Process.killProcess(android.os.Process.myPid());
				}
	   		}).
	   		setNegativeButton("Остаться",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(
						DialogInterface arg0, int arg1) {
				}
	   		}).
	   		show();*/
        }
        
    //}
	public void playItemName(int num)
	{
		if(levels[current_level_num].items.get(num).soundResource!=null) {
			if(levels[current_level_num].items.get(num).isBukva) {
				if (BukvyType == 1)
					soundFromObb(levels[current_level_num].items.get(num).soundResource[1]);
				else
					soundFromObb(levels[current_level_num].items.get(num).soundResource[0]);
			}
			else
				soundFromObb(levels[current_level_num].items.get(num).soundResource);
		}
		else
			soundFromObb("start_game");
	}
        public void CheckCompleteLevel(int num)
        {
        	boolean all_clear=true;
			for(int i=0;i<ListObjects.length;i++)
			{
				if(ListObjects[i]!=null && ListObjects[i].isSetOnPosition==false)
				{
					all_clear=false;
				}
			}
			{
				if(levels[current_level_num].items.get(num).soundResource!=null) {
					if (levels[current_level_num].items.get(num).isBukva) {
						if (BukvyType == 1)
							soundFromObb(levels[current_level_num].items.get(num).soundResource[1]);
						else
							soundFromObb(levels[current_level_num].items.get(num).soundResource[0]);
					}
					else
						soundFromObb(levels[current_level_num].items.get(num).soundResource);
				}
				else
				{
					int res_num=mRnd.nextInt(GoodStep.length);
					soundFromObb(GoodStep[res_num]);
				}
				if(all_clear)
				{
					for(int i=0;i<ListObjects.length;i++)
					{
						if(ListObjects[i]!=null && ListObjects[i].getVisibility()==VISIBLE)
							this.setBlinkView(ListObjects[i]);
					}
					Handler handlerTimer = new Handler();
					handlerTimer.postDelayed(new Runnable(){
					        public void run() {
		    					StageComplete();
	        					CurrentScreenType=2;
					      }}, 3000);
					hideAnimation(GlassView);
				}
				else
				{
					View reset_images[]=new View[ListObjects.length];
					int ri_num=0;
	    			for(int i=0;i<this.getChildCount();i++)
	    			{
	    				String class_name=this.getChildAt(i).getClass().getName();
	    				if(class_name.contains("MovingImageView"))
	    				{
	    					MovingImageView miv=(MovingImageView)this.getChildAt(i);
	    					if(miv.isSetOnPosition==false) 
							{
	    						reset_images[ri_num]=miv;
	    						ri_num++;
								//this.removeView(miv);
								//this.addView(miv);
							}
	    				}
					}
	    			for(int i=0;i<ri_num;i++)
	    			{
    					MovingImageView miv=(MovingImageView)reset_images[i];
						miv.bringToFront();
						//this.removeView(miv);
						//this.addView(miv);
					}
				}
			}
        }
	public void CheckCompleteLevelWithGoodSound(int num)
	{
		boolean all_clear=true;
		for(int i=0;i<ListObjects.length;i++)
		{
			if(ListObjects[i]!=null && ListObjects[i].isSetOnPosition==false)
			{
				all_clear=false;
			}
		}
		{
			{
				int res_num=mRnd.nextInt(GoodStep.length);
				soundFromObb(GoodStep[res_num]);
			}
			if(all_clear)
			{
				for(int i=0;i<ListObjects.length;i++)
				{
					if(ListObjects[i]!=null && ListObjects[i].getVisibility()==VISIBLE)
						this.setBlinkView(ListObjects[i]);
				}
				Handler handlerTimer = new Handler();
				handlerTimer.postDelayed(new Runnable(){
					public void run() {
						StageComplete();
						CurrentScreenType=2;
					}}, 3000);
				hideAnimation(GlassView);
			}
			else
			{
				View reset_images[]=new View[ListObjects.length];
				int ri_num=0;
				for(int i=0;i<this.getChildCount();i++)
				{
					String class_name=this.getChildAt(i).getClass().getName();
					if(class_name.contains("MovingImageView"))
					{
						MovingImageView miv=(MovingImageView)this.getChildAt(i);
						if(miv.isSetOnPosition==false)
						{
							reset_images[ri_num]=miv;
							ri_num++;
							//this.removeView(miv);
							//this.addView(miv);
						}
					}
				}
				for(int i=0;i<ri_num;i++)
				{
					MovingImageView miv=(MovingImageView)reset_images[i];
					this.removeView(miv);
					this.addView(miv);
				}
			}
		}
	}
	public Bitmap getImageFromObb(String pathToFileInsideZip, String extensionFile)
	{
		try {
			int resId = getResources().getIdentifier(pathToFileInsideZip, "drawable", Owner.getPackageName());
			if(resId>0)
			{
				if(isScaledBitmaps())
				{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = false;
					options.inSampleSize = 2;
					return BitmapFactory.decodeResource(Owner.getResources(), resId, options);
				}
				return BitmapFactory.decodeResource(Owner.getResources(),resId);
			}
			else {
				InputStream fileStream = expansionFile.getInputStream("images/" + pathToFileInsideZip + extensionFile);
				return BitmapFactory.decodeStream(fileStream);
			}
		}
		catch (IOException e)
		{
			Log.v("Load from zip error: ",e.getMessage());
		}
		return null;
	}

	public void soundFromObb(String pathToFileInsideZip)
	{
		try {

			Stop();

			int resId = getResources().getIdentifier(pathToFileInsideZip, "raw", Owner.getPackageName());
			if(resId>0)
			{
				if (isSound) {
					mp = MediaPlayer.create(Owner, resId);
					mp.start();
				}
			}
			else {
				AssetFileDescriptor fileDescriptor = expansionFile.getAssetFileDescriptor("sounds/" + pathToFileInsideZip + ".mp3");
				InputStream fileStream = expansionFile.getInputStream("sounds/" + pathToFileInsideZip + ".mp3");

				if (isSound) {
					mp = new MediaPlayer();
					mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mp.setDataSource(fileDescriptor.getFileDescriptor(),
							fileDescriptor.getStartOffset(), fileDescriptor.getLength());
					mp.prepare();
					mp.start();
				}
			}
		}
		catch (IOException e)
		{
			Log.v("Load from zip error: ",e.getMessage());
		}
	}

	public void soundFromObb(String[] pathToFileInsideZip) {
		soundFromObb(pathToFileInsideZip,0);
	}
	public void soundFromObb(final String[] pathToFileInsideZip, final int item)
	{
		try {

			Stop();
			if (!isSound)
				return;

			int resId = getResources().getIdentifier(pathToFileInsideZip[item], "raw", Owner.getPackageName());
			if(resId>0) {
				mp = MediaPlayer.create(Owner, resId);
			}
			else {
				AssetFileDescriptor fileDescriptor = expansionFile.getAssetFileDescriptor("sounds/" + pathToFileInsideZip[item] + ".mp3");
				//InputStream fileStream = expansionFile.getInputStream("sounds/" + pathToFileInsideZip + ".mp3");

				mp = new MediaPlayer();
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mp.setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(), fileDescriptor.getLength());
				mp.prepare();
			}
			if(item<pathToFileInsideZip.length-1)
			{
				mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						soundFromObb(pathToFileInsideZip,item+1);
					}
				});
			}
			mp.start();
		}
		catch (IOException e)
		{
			Log.v("Load from zip error: ",e.getMessage());
		}
	}

	public void Stop() {
		try
		{
			if (mp != null)
			{
				mp.stop();
				mp.release();
				mp = null;
			}
		}
		catch(Exception ex)
		{

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

	public boolean isHasMovingObjectAtPoint(Point point)
	{
		for(int i=0;i<ListObjects.length;i++)
		{
			if(ListObjects[i].isSetOnPosition==false)
			{
				LayoutParams layoutParams= (LayoutParams) ListObjects[i].getLayoutParams();
				if(ListObjects[i].isVisiblePoint(point.x-layoutParams.leftMargin,point.y-layoutParams.topMargin))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void ReturnToMainMenu()
	{
		{
			shtoraImageView = new ImageView(Owner);
			shtoraImageView.setBackgroundResource(R.drawable.animaciya_shtora2);
			addView(shtoraImageView);
			AnimationDrawable animationDrawable = (AnimationDrawable) shtoraImageView.getBackground();
			animationDrawable.start();
			setBounds(shtoraImageView, 0, 0, Owner.getViewWidth(), Owner.getViewHeight());
			long totalDuration = 0;
			for(int i = 0; i< animationDrawable.getNumberOfFrames();i++){
				totalDuration += animationDrawable.getDuration(i);
			}
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask(){
				@Override
				public void run() {
					shtoraImageView.post(new Runnable() {
						@Override
						public void run() {
							ClearSettings();
							ClearLevel();
							ClearSelectLevels();
							InitMainMenu();
						}
					});
				}
			};
			timer.schedule(timerTask, totalDuration);
		}

	}

	public static void setMemoryInfo()
	{
		memoryInfo = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) Owner.getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(memoryInfo);
	}

	public static boolean isScaledBitmaps()
	{
		return GameView.Owner.getViewHeight()<=480 || memoryInfo.totalMem<768*1024*1024;
	}

	private final static int maxInterstaatinalPeriod = 9;
	private int interstatinalPeriod = 0;
	public void showInterstatial() {
		if(interstatinalPeriod<maxInterstaatinalPeriod)
			interstatinalPeriod++;
		else if (Owner.mInterstitialAd.isLoaded()) {
			interstatinalPeriod=0;
			Owner.mInterstitialAd.show();
		}
	}


}
