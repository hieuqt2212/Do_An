package com.example.mba0229p.da_nang_travel.ui.main

import android.os.Bundle
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseActivity
import com.example.mba0229p.da_nang_travel.ui.main.header_home.HeaderAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity() {

    private var positionItemHeaderHome = 0

    private val listImageHeader = mutableListOf(R.drawable.bg_banner_app, R.drawable.bg_banner_app_2, R.drawable.bg_banner_app_3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enable swiping view pager header
        viewPagerHeaderHome.run {
            adapter = HeaderAdapter(this@MainActivity, listImageHeader)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
        }

        viewPagerContain.run {
            adapter = MainContainAdapter(supportFragmentManager)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
        }

        // Set auto change image in header
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOnUiThread()
                .subscribe {
                    if (it.toInt() % 2 == 0) {
                        if (positionItemHeaderHome < listImageHeader.size - 1) positionItemHeaderHome += 1 else positionItemHeaderHome = 0
                        viewPagerHeaderHome.setCurrentItem(positionItemHeaderHome, true)
                    }
                }

        viewBottom.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnHome -> viewPagerContain.setCurrentItem(0, false)
                R.id.btnRelax -> viewPagerContain.setCurrentItem(1, false)
                R.id.btnEat -> viewPagerContain.setCurrentItem(2, false)
                else -> viewPagerContain.setCurrentItem(3, false)
            }
            false
        }
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        super.getCurrentLocation(locationEvent)
    }
}
