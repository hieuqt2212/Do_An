package com.example.mba0229p.da_nang_travel.ui.eat.EatMaps

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_map.view.*

class EatMapAdapter(private val listEat: MutableList<Relax>, private val onItemEatClick: (Relax, Int) -> Unit) : RecyclerView.Adapter<EatMapAdapter.EatMapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatMapViewHolder =
            EatMapViewHolder(parent.initView(R.layout.item_list_map))

    override fun getItemCount(): Int = listEat.size

    override fun onBindViewHolder(holder: EatMapViewHolder, position: Int) {
        holder.onBind(listEat[position])
    }

    inner class EatMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemEatClick(listEat[adapterPosition], adapterPosition)
            }
        }

        fun onBind(relax: Relax) {
            itemView.apply {
                relax.nameLocation?.let { tvNameLocation.text = it }
                relax.address?.let { tvAddress.text = it }
            }
        }
    }
}
