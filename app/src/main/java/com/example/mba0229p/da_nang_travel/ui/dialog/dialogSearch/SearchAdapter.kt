package com.example.mba0229p.da_nang_travel.ui.dialog.dialogSearch

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import com.example.mba0229p.da_nang_travel.extension.initView
import kotlinx.android.synthetic.main.item_recycler_view_search.view.*
import java.util.*


class SearchAdapter(private var listSearch: MutableList<ItemSearch>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    internal var searchItemListener: (Int) -> Unit = {}
    private var listResult = mutableListOf<ItemSearch>().apply {
        addAll(listSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
            SearchViewHolder(parent.initView(R.layout.item_recycler_view_search))

    override fun getItemCount(): Int = listSearch.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.onBind(listSearch[position])
    }

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                searchItemListener(listSearch[adapterPosition].position)
            }
        }

        fun onBind(itemSearch: ItemSearch) {
            itemView.apply {
                tvNameLocationSearch.text = itemSearch.nameLocation
                tvAddress.text = itemSearch.address
            }
        }
    }

    fun filter(charText: String) {
        val text = charText.toLowerCase(Locale.getDefault())
        listSearch.clear()
        if (text.isEmpty()) {
            listSearch.addAll(listResult)
        } else {
            listResult.forEach {
                if (it.nameLocation.toLowerCase(Locale.getDefault()).contains(charText)) {
                    listSearch.add(it)
                } else {
                    if (it.address.toLowerCase(Locale.getDefault()).contains(charText)) {
                        listSearch.add(it)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }
}
