package com.example.mba0229p.da_nang_travel.ui.hotel

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mba0229p.da_nang_travel.ui.hotel.hotelList.HotelListFragment
import com.example.mba0229p.da_nang_travel.ui.hotel.hotelMaps.HotelMapsFragment

class HotelAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> HotelListFragment()
        else -> HotelMapsFragment()
    }

    override fun getCount() = 2
}
