package com.example.mba0229p.da_nang_travel.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng


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
}
