package com.example.mba0229p.da_nang_travel.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.example.mba0229p.da_nang_travel.ui.eat.EatFragment
import com.example.mba0229p.da_nang_travel.ui.home.HomeFragment
import com.example.mba0229p.da_nang_travel.ui.hotel.HotelFragment
import com.example.mba0229p.da_nang_travel.ui.relax.RelaxFragment

class MainContainAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> HomeFragment()
        1 -> RelaxFragment()
        2 -> EatFragment()
        else -> HotelFragment()
    }

    override fun getCount(): Int = 4

    override fun getItemPosition(`object`: Any): Int = PagerAdapter.POSITION_NONE
}
