package com.us.telemedicine.data

import android.app.Application
import com.us.telemedicine.global.extention.isConnected
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injectable class which returns information about the network connection state.
 */
@Singleton
class NetworkHandler
@Inject constructor(private val context: Application) {
    val isConnected: Boolean?
        get() { return context.isConnected() }
}
