package com.us.telemedicine.global.extention

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import pub.devrel.easypermissions.EasyPermissions


fun Fragment.isConnected() = requireContext().isConnected()

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return cm != null && isConnected(cm)
}

fun isConnected(cm: ConnectivityManager): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
    } // absolutely needed for all versions below 23
    else {
        cm.activeNetworkInfo?.run {
            if (type == ConnectivityManager.TYPE_WIFI ||
                type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
    }
    return false
}

fun Context.drawableFrom(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)
fun Fragment.drawableFrom(@DrawableRes res: Int) = requireContext().drawableFrom(res)

fun Context.colorFrom(@ColorRes id: Int) = ContextCompat.getColor(applicationContext, id)
fun Fragment.colorFrom(@ColorRes id: Int) = requireContext().colorFrom(id)

fun Context.stringFrom(@StringRes res: Int) = getString(res)
fun Fragment.stringFrom(@StringRes res: Int) = requireContext().stringFrom(res)

fun Fragment.isLocationEnabled() = requireContext().isLocationEnabled()
fun Context.isLocationEnabled(): Boolean {
    val lm = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    lm ?: return false

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        lm.isLocationEnabled // This is new method provided in API 28
    } else {
        lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}

fun Fragment.hasLocationPermissions() = requireContext().hasLocationPermissions()
fun Context.hasLocationPermissions(): Boolean {
    return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)//*vararg  mismatch
    //return EasyPermissions.hasPermissions(this, *LOCATION)//*vararg  mismatch
}

fun checkPlayServices(context: Context): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}
