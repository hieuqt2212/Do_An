package com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_dialog_info_home.view.*

class DialogHomeAdapter(private val listImage: MutableList<String>) : RecyclerView.Adapter<DialogHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.initView(R.layout.item_dialog_info_home))

    override fun getItemCount(): Int = listImage.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listImage[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(image: String) {
            itemView?.run {
                Glide.with(context)
                        .load(image)
                        .into(imgDialog)
            }
        }
    }
}
