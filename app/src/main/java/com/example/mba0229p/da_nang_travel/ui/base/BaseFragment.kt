package com.example.mba0229p.da_nang_travel.ui.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus

open class BaseFragment : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDisposables(RxBus.listenBehavior(LocationEvent::class.java).observeOnUiThread().subscribe {
            getCurrentLocation(it)
        })
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    /**
     * This function is used to define subscription
     */
    open fun onBindViewModel() {}

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

//    protected fun getCurrentFragment(): Fragment = (activity as BaseActivity).getCurrentFragment()

    protected fun getChildCurrentFragment(id: Int): Fragment = childFragmentManager.findFragmentById(id)

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
