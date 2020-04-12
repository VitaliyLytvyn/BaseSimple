package com.me.basesimple.domain.platform

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.me.basesimple.BuildConfig
import com.me.basesimple.domain.entity.UserAuthent
import com.me.basesimple.presentation.entity.UserEntity
import timber.log.Timber.d
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("ApplySharedPref")
@Singleton
class PreferenceHelper @Inject constructor(context: Application) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    val profileAsLiveData = object : LiveData<UserEntity?>() {
        private val preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == PREFS_STATUS_PROFILE) {
                    value = currentProfile?.toUserEntity()
                }
            }

        override fun onActive() {
            super.onActive()
            d("profileAsLiveData onActive() $this")
            value = currentProfile?.toUserEntity()
            prefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
        }

        override fun onInactive() {
            d("profileAsLiveData onInactive()")
            prefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
            super.onInactive()
        }
    }

    val profileAsLiveData_2 =
        object : SharedPreferenceLiveData<UserEntity?>(prefs, PREFS_STATUS_PROFILE, null) {
            override fun getValueFromPreferences(
                key: String,
                defValue: UserEntity?
            ): UserEntity? {
                return currentProfile?.toUserEntity()
            }
        }


    var isLoggedIn: Boolean = false
        get() = currentProfile != null //prefs.getBoolean(PREFS_LOGGED_IN, false)
        private set

    var currentEqupAssetName: String?
        get() = prefs.getString(PREFS_CURRENT_EQUIPMENT_ASSET_NAME, null)
        set(value) {
            prefs.edit().putString(PREFS_CURRENT_EQUIPMENT_ASSET_NAME, value).commit()
        }

    var currentProfile: UserAuthent?
        get() {
            val profile = prefs.getString(PREFS_STATUS_PROFILE, null)
            return if (profile != null)
                Gson().fromJson(profile, UserAuthent::class.java)
            else null
        }
        set(value) {
            val jsonProfile =
                if (value == null) null
                else {
                    Gson().toJson(value)
                }
            prefs.edit().putString(PREFS_STATUS_PROFILE, jsonProfile).commit()
        }


    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    fun clearAll() = prefs.edit().clear().apply()

    companion object {
        const val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.prefs"
        const val PREFS_STATUS_PROFILE = "$PREFS_FILENAME.status_profile"
        const val PREFS_LOGGED_IN = "$PREFS_FILENAME.loged_in"
        const val PREFS_CURRENT_EQUIPMENT_ASSET_NAME =
            "$PREFS_FILENAME.current_eq_asset_name"  //EqAssetName

    }
}

abstract class SharedPreferenceLiveData<T>(
    private val sharedPrefs: SharedPreferences,
    private val key: String,
    private val defValue: T?
) : LiveData<T>() {

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == this.key) {
                value = getValueFromPreferences(key, defValue)
            }
        }

    abstract fun getValueFromPreferences(key: String, defValue: T?): T?

    override fun onActive() {
        super.onActive()
        d("SharedPreferenceLiveData onActive() $this")
        value = getValueFromPreferences(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        d("SharedPreferenceLiveData onInactive()")
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}
