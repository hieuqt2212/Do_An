package com.example.mba0229p.da_nang_travel.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.initView(layout: Int): View =
        LayoutInflater.from(this.context).inflate(layout, this, false)
