package com.example.mba0229p.da_nang_travel.ui.eat

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mba0229p.da_nang_travel.ui.eat.EatLits.EatListFragment
import com.example.mba0229p.da_nang_travel.ui.eat.EatMaps.EatMapFragment

class EatViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> EatListFragment()
        else -> EatMapFragment()
    }

    override fun getCount() = 2
}
