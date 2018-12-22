package com.example.mba0229p.da_nang_travel.ui.main

import android.os.Bundle
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPagerContain.run {
            adapter = MainContainAdapter(supportFragmentManager)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
            offscreenPageLimit = 4
        }

        viewBottom.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnHome -> viewPagerContain.setCurrentItem(0, false)
                R.id.btnRelax -> viewPagerContain.setCurrentItem(1, false)
                R.id.btnEat -> viewPagerContain.setCurrentItem(2, false)
                else -> viewPagerContain.setCurrentItem(3, false)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}
