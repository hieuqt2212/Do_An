package com.example.mba0229p.da_nang_travel.ui.base

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import com.example.mba0229p.da_nang_travel.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus

open class BaseFragment : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()
    internal var dialog: Dialog? = null
    private var oldLocation: LocationEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        dialog = context?.let { DialogUtils.showProgressDialog(it) }
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        addDisposables(RxBus.listenBehavior(LocationEvent::class.java).observeOnUiThread()
                .subscribe { newLocation ->
                    if (oldLocation == null) {
                        oldLocation = newLocation
                        getCurrentLocation(newLocation)
                    } else {
                        oldLocation?.let { oldLocationEvent ->
                            oldLocationEvent.location.latitude
                            if (LocationUtils.distanceBetween(LatLng(newLocation.location.latitude, newLocation.location.longitude),
                                            LatLng(oldLocationEvent.location.latitude, oldLocationEvent.location.longitude)) > 500) {
                                getCurrentLocation(newLocation)
                                oldLocation = newLocation
                            }
                        }
                    }
                })
        onBindViewModel()
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    /**
     * This function is used to define subscription
     */
    open fun onBindViewModel() {}

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

//    protected fun getCurrentFragment(): Fragment = (activity as BaseActivity).getCurrentFragment()

    fun getCurrentFragmentParent(): Fragment = (activity as BaseActivity).getCurrentFragment()

    /**
     * This method is using for pop fragment
     */
    fun popFragment() {
        childFragmentManager.popBackStack()
    }

    /**
     * Add new child fragment on page
     *
     * @param fragment : new child fragment
     * @param containerViewId : Optional identifier of the container this fragment
     */
    internal fun addChildFragment(@IdRes containerViewId: Int, fragment: BaseFragment) {
        childFragmentManager.beginTransaction().run {
            add(containerViewId, fragment)
            addToBackStack(fragment::class.java.name)
            commit()
        }
    }

    open fun getCurrentLocation(locationEvent: LocationEvent) {
    }
}
