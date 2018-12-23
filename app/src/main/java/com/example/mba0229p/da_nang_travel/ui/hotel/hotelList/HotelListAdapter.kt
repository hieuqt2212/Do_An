package com.example.mba0229p.da_nang_travel.ui.hotel.hotelList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_relax.view.*
import java.util.concurrent.TimeUnit

class HotelListAdapter(private val listHotel: MutableList<Relax>) : RecyclerView.Adapter<HotelListAdapter.HotelListVH>() {

    internal var itemHotelListener: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelListVH =
            HotelListVH(parent.initView(R.layout.item_list_relax))

    override fun getItemCount() = listHotel.size

    override fun onBindViewHolder(holder: HotelListVH, position: Int) {
        holder.onBind(listHotel[position])
    }

    inner class HotelListVH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                setOnClickListener {
                    itemHotelListener(adapterPosition)
                }
            }
        }

        fun onBind(guestHouse: Relax) {
            itemView.apply {
                tvAddress.text = guestHouse.address
                tvHour.text = if (guestHouse.hour == null) "-- h" else String.format("%d phút", TimeUnit.SECONDS.toMinutes(guestHouse.hour!!.toLong()))
                tvDistance.text = if (guestHouse.distance == null) "-- Km" else String.format("%2.2fKm", (guestHouse.distance!!) / 1000)
                tvNameLocation.text = guestHouse.nameLocation
            }
        }
    }
}