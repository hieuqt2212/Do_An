package com.example.mba0229p.da_nang_travel.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.service.LocationService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import pub.devrel.easypermissions.EasyPermissions

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var locationService: LocationService? = null

    private val subscription: CompositeDisposable = CompositeDisposable()

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDisposables(RxBus.listenBehavior(LocationEvent::class.java).observeOnUiThread().subscribe {
            Log.d("xxx", "$it")
            getCurrentLocation(it)
            initService()
        })
    }

    private fun initService() {
        // Set up Service
        val intent = Intent(this, LocationService::class.java)
        // Start service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Attach Service like: Push, Analytics after the view createdl
        attachService()
    }

    private fun attachService() {
        // Check Google Play Service
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 10000).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    open fun getCurrentLocation(locationEvent: LocationEvent) {
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
}