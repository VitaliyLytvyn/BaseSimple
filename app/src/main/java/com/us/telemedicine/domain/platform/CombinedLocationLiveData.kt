package com.us.telemedicine.domain.platform

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.*
import androidx.lifecycle.LiveData
import android.os.Looper
import com.google.android.gms.location.*
import com.us.telemedicine.global.extention.hasLocationPermissions
import timber.log.Timber.e
import timber.log.Timber.d
import javax.inject.Inject


//@Suppress("DEPRECATION")
class CombinedLocationLiveData @Inject constructor(private val appContext: Application) : LiveData<CombinedLocationStatus>() {

    // Contains parameters used by FusedLocationProviderApi
    private lateinit var mLocationRequest: LocationRequest

    //Provides access to the Fused Location Provider API
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    //Provides access to satellites status
    private lateinit var mLocationManager: LocationManager

    // Callback for changes in location.
    private lateinit var mLocationCallback: LocationCallback

    init {
        buildGoogleApiClient(appContext)
    }

    @Synchronized
    private fun buildGoogleApiClient(appContext: Context) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
        mLocationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                locationResult?.let {
                    onNewLocation(locationResult.lastLocation)
                }
            }
        }

        createLocationRequest()
        getLastLocation()
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {

                            postValue(
                                CombinedLocationStatus(
                                    location = task.result!!,
                                    isLastKnown = true
                                )
                            )
                        }
                    }
        } catch (unlikely: SecurityException) {
            e("Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        postValue(CombinedLocationStatus(location = location))
    }

    //Sets the location request parameters
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest.interval =
            UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.maxWaitTime =
            UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.smallestDisplacement = 10F // 10 meters

    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        if (!appContext.hasLocationPermissions()) return
        d("CombinedLocationLiveData onActive")

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper())

        } catch (unlikely: SecurityException) {
            e("Lost location permission. Could not request updates. $unlikely")
        }
    }

    override fun onInactive() {
        d("CombinedLocationLiveData onInactive")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            // prefs.isStartedMonitoring = false

        } catch (unlikely: SecurityException) {
            e("Lost location permission. Could not remove updates. $unlikely")
        }
    }

    companion object{
       const val UPDATE_INTERVAL_IN_MILLISECONDS = 10_000L
       const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10_000L
    }

}

data class CombinedLocationStatus (
        val location: Location,
        val isLastKnown: Boolean = false
)