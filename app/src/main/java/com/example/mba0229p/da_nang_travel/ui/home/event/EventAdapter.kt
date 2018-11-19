package com.example.mba0229p.da_nang_travel.ui.home.event

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_info_home.view.*

class EventAdapter(private val listInfo: MutableList<EventHome>) : RecyclerView.Adapter<EventAdapter.InfoViewHolder>() {

    internal var eventListener: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            InfoViewHolder(parent.initView(R.layout.item_info_home))

    override fun getItemCount(): Int = listInfo.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.onBind(listInfo[position])
    }

    inner class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView?.apply {
                setOnClickListener {
                    eventListener(adapterPosition)
                }
            }
        }

        fun onBind(eventHome: EventHome) {
            itemView?.run {
                tvNameLocation.text = eventHome.nameEvent
                tvContent.text = eventHome.content
                Glide.with(context)
                        .load(eventHome.image?.first())
                        .into(imgAvatar)
            }
        }
    }
}
