package com.example.mba0229p.da_nang_travel.utils

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng
import pub.devrel.easypermissions.EasyPermissions


/**
 * Copyright © AsianTech Co., Ltd
 * Created by kietva on 4/4/18.
 */
object LocationUtils {
    /**
     * Get distance between 2 locations
     */
    fun distanceBetween(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results)
        return results[0]
    }

    /**
     * Get distance between 2 locations
     */
    fun distanceBetween(startLatLng: LatLng, endLatLng: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLatLng.latitude, startLatLng.longitude, endLatLng.latitude, endLatLng.longitude, results)
        return results[0] / 1000F
    }

    /**
     * Check Location permission is open
     * */
    fun checkIsOpenLocationPermission(context: Context): Boolean {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        return EasyPermissions.hasPermissions(context, *permissions)
    }

    /**
     * Check GPS is open
     */
    fun checkIsOpenGPS(context: Context?): Boolean = (context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager).run {
        isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
