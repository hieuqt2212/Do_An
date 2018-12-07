package com.example.mba0229p.da_nang_travel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.main.header_home.HeaderAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment() {
    private var positionItemHeaderHome = 0
    private val listImageHeader = mutableListOf(R.drawable.bg_banner_app, R.drawable.bg_banner_app_2, R.drawable.bg_banner_app_3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.initView(R.layout.fragment_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Enable swiping view pager header
        viewPagerContainHome.run {
            adapter = HomePagerAdapter(childFragmentManager)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
        }

        // Enable swiping view pager header
        viewPagerHeaderHome.run {
            adapter = HeaderAdapter(context, listImageHeader)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
        }

        // Set auto change image in header
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOnUiThread()
                .subscribe {
                    if (it.toInt() % 2 == 0) {
                        if (positionItemHeaderHome < listImageHeader.size - 1) positionItemHeaderHome += 1 else positionItemHeaderHome = 0
                        if (isAdded) {
                            viewPagerHeaderHome.setCurrentItem(positionItemHeaderHome, true)
                        }
                    }
                }

        initListener()
    }

    private fun initListener() {
        rbInfo.setOnClickListener {
            rbInfo.isChecked = true
            viewPagerContainHome.setCurrentItem(0, true)
        }
        rbEvent.setOnClickListener {
            rbEvent.isChecked = true
            viewPagerContainHome.setCurrentItem(1, true)
        }
    }
}
