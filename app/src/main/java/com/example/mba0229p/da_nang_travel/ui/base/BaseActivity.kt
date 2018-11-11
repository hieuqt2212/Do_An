package com.example.mba0229p.da_nang_travel.ui.base

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : AppCompatActivity() {

    private val subscription: CompositeDisposable = CompositeDisposable()

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
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