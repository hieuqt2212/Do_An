package com.example.mba0229p.da_nang_travel.ui.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.utils.LocationUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import java.util.concurrent.TimeUnit


abstract class BaseActivity : AppCompatActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private companion object {
        const val UPDATE_INTERVAL: Long = 5000
        const val FASTEST_INTERVAL: Long = 5000
        const val REQUEST_LOCATION_PERMISSION = 100
    }

    private var disposable: Disposable? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastLocation: Location? = null
    private val subscription: CompositeDisposable = CompositeDisposable()
    private var mGoogleApiClient: GoogleApiClient? = null

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleApiClient?.connect()
        // Check device support google play service
        if (isPlayServicesAvailable()) {
            setUpLocationClientIfNeeded()
            buildLocationRequest()
        }
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOnUiThread()
                .subscribe {
                    checkGPS()
                    requestLocationPermissions()
                    disposable?.dispose()
                }
    }

    private fun checkGPS() {
        if (!isGpsOn()) {
            Toast.makeText(this, "GPS is OFF",
                    Toast.LENGTH_SHORT).show();
            return
        }
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    override fun onDestroy() {
        if (mGoogleApiClient != null
                && mGoogleApiClient?.isConnected!!) {
            stopLocationUpdates()
            mGoogleApiClient?.disconnect()
            mGoogleApiClient = null
        }
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestLocationPermissions()
            }
            else -> {
            }
        }
    }

    // Connect google location
    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    override fun onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.disconnect()
        }
        super.onStop()
    }

    override fun onConnected(bundle: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        if (lastLocation != null) {
            mLastLocation = lastLocation
            RxBus.publishToBehaviorSubject(LocationEvent(lastLocation))
        }
    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onLocationChanged(location: Location) {
        if (mLastLocation == null || mLastLocation?.longitude?.let { mLastLocation?.latitude?.let { it1 -> LocationUtils.distanceBetween(it1, it, location.latitude, location.longitude) } }!! >= 500) {
            RxBus.publishToBehaviorSubject(LocationEvent(location))
        }
        mLastLocation = location
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        mGoogleApiClient?.connect()
    }

    /**
     * This function return the fragment is displaying on top of container.
     */
//    fun getCurrentFragment(): Fragment = supportFragmentManager.findFragmentById(R.id.flContain)

    /**
     * This function is used to define subscription
     */
    open fun onBindViewModel() {
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)
        GoogleApiClient.getAllClients()
    }

    private fun stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
    }

    private fun isPlayServicesAvailable(): Boolean {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
    }

    private fun isGpsOn(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build().apply {
                        connect()
                    }
        }
        mGoogleApiClient?.connect()
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun buildLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = UPDATE_INTERVAL
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL
    }
}
