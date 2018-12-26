package com.example.mba0229p.da_nang_travel.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_dialog_info_home.view.*

class ImageHomeAdapter(private val listImage: MutableList<Int>) : RecyclerView.Adapter<ImageHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.initView(R.layout.item_dialog_info_home))

    override fun getItemCount(): Int = listImage.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listImage[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(image: Int) {
            itemView?.run {
                imgDialog.setImageResource(image)
            }
        }
    }
}
