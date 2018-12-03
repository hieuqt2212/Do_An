package com.example.mba0229p.da_nang_travel.ui.home.info

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.InfoHome
import com.example.mba0229p.da_nang_travel.data.source.HomeRepository
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : BaseFragment() {

    private var listInfoHome = mutableListOf<InfoHome>()
    var recyclerViewInfoHomeAdapter: InfoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.initView(R.layout.fragment_info)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInfoHomeAdapter = InfoAdapter(listInfoHome)
        recyclerViewInfo.run {
            adapter = recyclerViewInfoHomeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        HomeRepository().homeInfoRepo().subscribe({
            listInfoHome.addAll(it)
            recyclerViewInfoHomeAdapter?.notifyDataSetChanged()
        }, {})

        handleListener()
    }

    private fun handleListener() {
        recyclerViewInfoHomeAdapter?.infoListener = { position ->
            fragmentManager?.let { DialogUtils.showDialogHomeInfo(it, listInfoHome[position].nameLocation, listInfoHome[position].image?.toMutableList(), listInfoHome[position].content) }
        }
    }
}
