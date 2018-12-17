package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_map.view.*

class RelaxMapAdapter(private val listRelax: MutableList<Relax>, private val onItemClick: (Relax, Int) -> Unit) : RecyclerView.Adapter<RelaxMapAdapter.RelaxMapViewHolder>() {

    private var listenerMap: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelaxMapViewHolder =
            RelaxMapViewHolder(parent.initView(R.layout.item_list_map))

    override fun getItemCount(): Int = listRelax.size

    override fun onBindViewHolder(holder: RelaxMapViewHolder, position: Int) {
        holder.onBind(listRelax[position])
    }

    inner class RelaxMapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                listenerMap(adapterPosition)
                onItemClick(listRelax[adapterPosition], adapterPosition)
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
