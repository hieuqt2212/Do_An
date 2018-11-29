package com.example.mba0229p.da_nang_travel.ui.home.info

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.InfoHome
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_info_home.view.*

class InfoAdapter(private val listInfo: MutableList<InfoHome>) : RecyclerView.Adapter<InfoAdapter.InfoViewHolder>() {

    internal var infoListener: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            InfoViewHolder(parent.initView(R.layout.item_info_home))

    override fun getItemCount(): Int = listInfo.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.onBind(listInfo[position])
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                setOnClickListener {
                    infoListener(adapterPosition)
                }
            }
        }

        fun onBind(infoHome: InfoHome) {
            itemView.run {
                tvNameLocation.text = infoHome.nameLocation
                tvContent.text = infoHome.content
                Glide.with(context)
                        .load(infoHome.image?.first())
                        .into(imgAvatar)
            }
        }
    }
}
