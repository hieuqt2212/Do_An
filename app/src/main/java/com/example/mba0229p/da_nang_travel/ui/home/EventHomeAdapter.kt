package com.example.mba0229p.da_nang_travel.ui.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_event_home.view.*

class EventHomeAdapter(var listEvent: MutableList<EventHome>) : RecyclerView.Adapter<EventHomeAdapter.EventHomeVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHomeVH = EventHomeVH(parent.initView(R.layout.item_event_home))

    override fun getItemCount(): Int = listEvent.size

    override fun onBindViewHolder(holder: EventHomeVH, position: Int) {
        holder.onBind(listEvent[position])
    }

    inner class EventHomeVH(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(eventHome: EventHome) {
            itemView?.run {
                tvDateEvent.text = eventHome.month.toString()
                tvDayEvent.text = eventHome.day.toString()
                Glide.with(context)
                        .load(eventHome.image?.first())
                        .into(imgAvatarEvent)
                tvNameEvent.text = eventHome.nameEvent
            }
        }
    }
}
