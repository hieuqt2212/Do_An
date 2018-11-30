package com.example.mba0229p.da_nang_travel.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.disposables.CompositeDisposable
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus


class LocationService : Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val MAXIMUM_RECONNECT_GOOGLE_API = 3
        // Default map address
        private const val DEFAULT_LATITUDE = 10.8012975
        private const val DEFAULT_LONGITUDE = 106.6496474
    }

    var locationRequest: LocationRequest? = null
    private lateinit var locationCallBack: LocationCallback
    private val subscription: CompositeDisposable = CompositeDisposable()
    private var attemptTimes = 0
    private var isResetInitialize = false
    private var mGoogleApiClient: GoogleApiClient? = null
    private val locationBinder = LocationBinder()

    override fun onCreate() {
        // Setup Google Api Client
        setUpLocationClientIfNeeded()
        locationRequest = LocationRequest.create().apply {
            // Priority for location request will be scaled by balanced power battery and accuracy.
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            // Param for time request.
            interval = 1000
            fastestInterval = interval
            // Param for distance per request.
            smallestDisplacement = 90f
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start to connect
        mGoogleApiClient?.connect()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return locationBinder
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy Location listener
        mGoogleApiClient?.disconnect()
        // Clear all subscription
        subscription.clear()
    }

    override fun onConnected(p0: Bundle?) {
        // Reset request Count
        attemptTimes = 0

        // Latest Location
        getLatestLocation()

        // Listener for get new location
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let {
                    it.lastLocation.run {
                        // Publish new location to all observable
                        publishToObservable(LocationEvent(this, isResetInitialize))
                    }
                }
            }
        }

        // Request to update location
        startLocationUpdate()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        attemptTimes++
        if (attemptTimes < MAXIMUM_RECONNECT_GOOGLE_API) {
            mGoogleApiClient?.reconnect()
        } else {
            // Disable connect google API client.
            mGoogleApiClient?.disconnect()
        }
    }

    /**
     * Get Latest Location
     */
    fun getLatestLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // In the case the permission is not enabled.
            publishToObservable(LocationEvent(Location("DEFAULT_PROVIDER").apply {
                latitude = DEFAULT_LATITUDE
                longitude = DEFAULT_LONGITUDE
            }, isResetInitialize))
            return
        }
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            // Sent latest location
            val location = it ?: Location("DEFAULT_PROVIDER").apply {
                latitude = DEFAULT_LATITUDE
                longitude = DEFAULT_LONGITUDE
            }
            // Publish data
            publishToObservable(LocationEvent(location, isResetInitialize))
        }
    }

    /**
     * Send data to subscriber
     */
    fun publishToObservable(locationEvent: LocationEvent) {
        Log.d("xxx", "lll $locationEvent")
        RxBus.publishToBehaviorSubject(locationEvent)
        // Reset case to reset initialize
        isResetInitialize = false
    }

    private fun setUpLocationClientIfNeeded() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    /**
     * Start request new Location
     */
    private fun startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocationBinder : Binder() {
        internal    // Return this instance of LocalService so clients can call public methods
        val service: LocationService
            get() = this@LocationService
    }
}
