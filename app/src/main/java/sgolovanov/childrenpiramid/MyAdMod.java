package sgolovanov.childrenpiramid;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by Sergey on 10.09.2016.
 */
public class MyAdMod extends RelativeLayout {
    AdView adView;
    public MyAdMod(Context context, AdSize adSize) {
        super(context);
        adView = new AdView(context);
        adView.setAdSize(adSize);
        adView.setAdUnitId("ca-app-pub-4919341775273582/5197519905");
        adView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(adView);
    }
    public void sendRequest()
    {
        AdRequest request = new AdRequest.Builder().build();//new AdRequest();
        adView.loadAd(request);
    }
}
