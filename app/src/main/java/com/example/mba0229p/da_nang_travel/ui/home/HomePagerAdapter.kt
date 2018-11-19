package com.example.mba0229p.da_nang_travel.ui.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.example.mba0229p.da_nang_travel.ui.home.event.EventFragment
import com.example.mba0229p.da_nang_travel.ui.home.info.InfoFragment

class HomePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> InfoFragment()
        else -> EventFragment()
    }

    override fun getCount(): Int = 2

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}
