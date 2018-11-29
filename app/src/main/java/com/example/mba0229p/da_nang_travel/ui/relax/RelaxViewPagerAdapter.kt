package com.example.mba0229p.da_nang_travel.ui.relax

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mba0229p.da_nang_travel.ui.relax.RelaxList.RelaxListFragment
import com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap.RelaxMapFragment

class RelaxViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> RelaxListFragment()
        else -> RelaxMapFragment()
    }

    override fun getCount() = 2
}
