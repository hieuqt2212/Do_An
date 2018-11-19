package com.example.mba0229p.da_nang_travel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

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
