package com.example.mba0229p.da_nang_travel.utils

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class LocationUtil(private val mContext: Context) : LocationListener {

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 5L
        private const val MIN_TIME_BW_UPDATES = 1000L
        private const val TAG = "xxx"
    }

    private var mIsGPSEnabled = false
    private var mCanGetLocation = false
    private var mIsNetworkEnabled = false
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null

    /**
     * To get the current location
     *
     * @return mLocation is the current location
     */
    fun getCurrentLocation(): Location? {
        if (canGetLocation()) {
            mCanGetLocation = true
            try {
                mLocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                checkGPSOrWifi()
                if (mIsNetworkEnabled) {
                    mLocationManager?.let {
                        it.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER
                                , MIN_TIME_BW_UPDATES
                                , MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                    if (mLocation != null) {
                        mLocationManager?.let {
                            mLocation = it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        }
                    }
                }
            } catch (e: SecurityException) {
                Log.e(TAG, e.toString())
            }
        } else {
            Log.d(TAG, "Can not get location!")
        }
        return mLocation
    }

    private fun canGetLocation(): Boolean {
        mLocationManager = mContext
                .getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        checkGPSOrWifi()
        mCanGetLocation = !(!mIsGPSEnabled && !mIsNetworkEnabled)
        return mCanGetLocation
    }

    private fun checkGPSOrWifi() {
        mLocationManager?.let {
            // getting GPS status
            mIsGPSEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting wifi status
            mIsNetworkEnabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }

    override fun onLocationChanged(location: Location) {}

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}