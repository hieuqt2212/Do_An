package com.example.mba0229p.da_nang_travel.ui.home.header_home

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.mba0229p.da_nang_travel.R


class HeaderAdapter(private val context: Context, private val listImage: MutableList<Int>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_header_home, container, false)
        val img = view.findViewById<View>(R.id.imgHeaderHome) as ImageView
        img.setBackgroundResource(listImage[position])
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = listImage.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(container)
    }
}
