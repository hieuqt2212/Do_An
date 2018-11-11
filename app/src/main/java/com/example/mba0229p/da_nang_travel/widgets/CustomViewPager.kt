package com.example.mba0229p.da_nang_travel.widgets

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    internal var enabled = true

    override fun performClick(): Boolean = if (this.enabled) super.performClick() else false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (this.enabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setEnabledSwiping(enabled: Boolean) {
        this.enabled = enabled
    }
}