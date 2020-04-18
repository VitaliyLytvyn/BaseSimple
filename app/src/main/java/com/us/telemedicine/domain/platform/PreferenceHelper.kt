package com.us.telemedicine.domain.platform

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.us.telemedicine.BuildConfig
import com.us.telemedicine.domain.entity.UserAuthent
import com.us.telemedicine.domain.entity.UserEntity
import timber.log.Timber.d
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("ApplySharedPref")
@Singleton
class PreferenceHelper @Inject constructor(context: Application) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    val profileAsLiveData =
        object : SharedPreferenceLiveData<UserEntity?>(prefs, PREFS_STATUS_PROFILE, null) {
            override fun getValueFromPreferences(
                key: String,
                defValue: UserEntity?
            ): UserEntity? {
                return currentProfile
            }
        }

    val loginStatusAsLiveData =
        object : SharedPreferenceLiveData<Boolean?>(prefs, PREFS_LOGGED_IN, false) {
            override fun getValueFromPreferences(
                key: String,
                defValue: Boolean?
            ): Boolean {
                return isLoggedIn
            }
        }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(PREFS_LOGGED_IN, false)
        private set(value) {
            prefs.edit().putBoolean(PREFS_LOGGED_IN, value).commit()
        }

    var tokenExpiresAt: String?
        get() = prefs.getString(PREFS_ACCESS_TOKEN_EXPIRES_AT, null)
        set(value) {
            prefs.edit().putString(PREFS_ACCESS_TOKEN_EXPIRES_AT, value).commit()
        }

    var accessToken: String?
        get() = prefs.getString(PREFS_ACCESS_TOKEN, null)
        set(value) {
            isLoggedIn = value != null
            prefs.edit().putString(PREFS_ACCESS_TOKEN, value).commit()
        }

    var refreshToken: String?
        get() = prefs.getString(PREFS_REFRESH_TOKEN, null)
        set(value) {
            prefs.edit().putString(PREFS_REFRESH_TOKEN, value).commit()
        }

    var currentProfile: UserEntity?
        get() {
            val profile = prefs.getString(PREFS_STATUS_PROFILE, null)
            return if (profile != null)
                Gson().fromJson(profile, UserEntity::class.java)
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

    fun clearAll() = prefs.edit().clear().apply()

    companion object {
        const val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.prefs"
        const val PREFS_STATUS_PROFILE = "$PREFS_FILENAME.status_profile"
        const val PREFS_LOGGED_IN = "$PREFS_FILENAME.logged_in"
        const val PREFS_ACCESS_TOKEN = "$PREFS_FILENAME.accessToken"
        const val PREFS_REFRESH_TOKEN = "$PREFS_FILENAME.refreshToken"
        const val PREFS_ACCESS_TOKEN_EXPIRES_AT = "$PREFS_FILENAME.tokenExpiresAt"
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
