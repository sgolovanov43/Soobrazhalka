package sgolovanov.childrenpiramid

import android.app.Application
import android.util.Log
import com.yandex.mobile.ads.common.YandexAds
import sgolovanov.knights_move.utils.GamePreferences

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        YandexAds.initialize(this) {
            Log.i("YandexAds", "Yandex Ads started")
        }
        GamePreferences.init(this)
        StarView.init(this)
    }
}