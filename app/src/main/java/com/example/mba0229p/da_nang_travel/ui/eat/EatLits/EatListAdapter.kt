package com.example.mba0229p.da_nang_travel.ui.eat.EatLits

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_relax.view.*
import java.util.concurrent.TimeUnit

class EatListAdapter(private val listEat: MutableList<Relax>) : RecyclerView.Adapter<EatListAdapter.EatListVH>() {

    internal var itemEatListener: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EatListVH =
            EatListVH(parent.initView(R.layout.item_list_relax))

    override fun getItemCount() = listEat.size

    override fun onBindViewHolder(holder: EatListVH, position: Int) {
        holder.onBind(listEat[position])
    }

    inner class EatListVH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.apply {
                setOnClickListener {
                    itemEatListener(adapterPosition)
                }
            }
        }

        fun onBind(eat: Relax) {
            itemView.apply {
                tvAddress.text = eat.address
                tvHour.text = if (eat.hour == null) "-- h" else String.format("%d phút", TimeUnit.SECONDS.toMinutes(eat.hour!!.toLong()))
                tvDistance.text = if (eat.distance == null) "-- Km" else String.format("%2.2fKm", (eat.distance!!) / 1000)
                tvNameLocation.text = eat.nameLocation
            }
        }
    }
}