package sgolovanov.knights_move.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object GamePreferences {

    private var sharedPreferences: SharedPreferences? = null

    private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"
    private const val ADS_COUNTER = "ADS_COUNTER"
    private const val ADS_COUNTER_VALUE = 5
    private const val LAST_LEVEL = "LAST_LEVEL"
    private const val USER_ID = "USER_ID"
    private const val USER_NAME = "USER_NAME"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0)
    }

    var adsCounter: Int
        get() = sharedPreferences?.getInt(ADS_COUNTER, 0) ?: 0
        set(value) {
            sharedPreferences?.edit {
                putInt(ADS_COUNTER, value)
            }
        }
    fun addAdsCounter() {
        adsCounter ++
    }
    fun checkReadyFroAds(): Boolean {
        addAdsCounter()
        return adsCounter >= ADS_COUNTER_VALUE
    }

    var lastLevel: Int
        get() = sharedPreferences?.getInt(LAST_LEVEL, 0) ?: 0
        set(value) {
            sharedPreferences?.edit {
                putInt(LAST_LEVEL, value)
            }
        }

    var userId: Long
        get() = sharedPreferences?.getLong(USER_ID, 0) ?: 0
        set(value) {
            sharedPreferences?.edit {
                putLong(USER_ID, value)
            }
        }

    var userName: String
        get() = sharedPreferences?.getString(USER_NAME, "") ?: ""
        set(value) {
            sharedPreferences?.edit {
                putString(USER_NAME, value)
            }
        }

}