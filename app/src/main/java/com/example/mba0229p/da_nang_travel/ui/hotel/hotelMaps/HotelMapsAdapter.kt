package com.example.mba0229p.da_nang_travel.ui.hotel.hotelMaps

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_map.view.*

class HotelMapsAdapter(private val listHotel: MutableList<Relax>, private val onItemHotelClick: (Relax, Int) -> Unit) :
        RecyclerView.Adapter<HotelMapsAdapter.HotelMapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelMapViewHolder =
            HotelMapViewHolder(parent.initView(R.layout.item_list_map))

    override fun getItemCount(): Int = listHotel.size

    override fun onBindViewHolder(holder: HotelMapViewHolder, position: Int) {
        holder.onBind(listHotel[position])
    }

    inner class HotelMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemHotelClick(listHotel[adapterPosition], adapterPosition)
            }
        }

        fun onBind(hotel: Relax) {
            itemView.apply {
                hotel.nameLocation?.let { tvNameLocation.text = it }
                hotel.address?.let { tvAddress.text = it }
            }
        }
    }
}
