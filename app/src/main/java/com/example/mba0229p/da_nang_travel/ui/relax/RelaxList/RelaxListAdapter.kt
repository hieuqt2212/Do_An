package com.example.mba0229p.da_nang_travel.ui.relax.RelaxList

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_list_relax.view.*

class RelaxListAdapter(private val listRelax: MutableList<Relax>) : RecyclerView.Adapter<RelaxListAdapter.RelaxListVH>() {

    private var itemRelaxListener: (Int) -> Unit = {}
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
                tvHour.text = relax.hour.toString()
                tvDistance.text = relax.distance.toString()
                tvNameLocation.text = relax.nameLocation
            }
        }
    }
}