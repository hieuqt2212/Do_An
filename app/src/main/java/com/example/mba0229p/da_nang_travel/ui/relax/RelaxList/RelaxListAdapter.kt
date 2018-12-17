package com.example.mba0229p.da_nang_travel.ui.relax.RelaxList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_relax.view.*
import java.util.concurrent.TimeUnit

class RelaxListAdapter(private val listRelax: MutableList<Relax>) : RecyclerView.Adapter<RelaxListAdapter.RelaxListVH>() {

    internal var itemRelaxListener: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelaxListVH =
            RelaxListVH(parent.initView(R.layout.item_list_relax))

    override fun getItemCount() = listRelax.size

    override fun onBindViewHolder(holder: RelaxListVH, position: Int) {
        holder.onBind(listRelax[position])
    }

    inner class RelaxListVH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                setOnClickListener {
                    itemRelaxListener(adapterPosition)
                }
            }
        }

        fun onBind(relax: Relax) {
            itemView.apply {
                tvAddress.text = relax.address
                tvHour.text = if (relax.hour == null) "-- h" else String.format("%d phút", TimeUnit.SECONDS.toMinutes(relax.hour!!.toLong()))
                tvDistance.text = if (relax.distance == null) "-- Km" else String.format("%2.2fKm", (relax.distance!!) / 1000)
                tvNameLocation.text = relax.nameLocation
            }
        }
    }
}