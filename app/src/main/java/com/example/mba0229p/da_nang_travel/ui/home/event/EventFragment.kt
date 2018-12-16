package com.example.mba0229p.da_nang_travel.ui.home.event

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.data.source.Repository
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_info.*

class EventFragment : BaseFragment() {

    private var listEventHome = mutableListOf<EventHome>()
    private var recyclerViewEventHomeAdapter: EventAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            container?.initView(R.layout.fragment_info)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewEventHomeAdapter = EventAdapter(listEventHome)
        recyclerViewInfo.run {
            adapter = recyclerViewEventHomeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        context?.let { context ->
            Repository().homeEventRepo(context)
                    .doOnSubscribe { dialog?.show() }
                    .doFinally { dialog?.dismiss() }
                    .subscribe({
                        listEventHome.clear()
                        listEventHome.addAll(it)
                        recyclerViewEventHomeAdapter?.notifyDataSetChanged()
                    }, {})
        }

        handleListener()
    }

    private fun handleListener() {
        recyclerViewEventHomeAdapter?.eventListener = { position ->
            fragmentManager?.let { DialogUtils.showDialogHomeInfo(it, listEventHome[position].nameEvent, listEventHome[position].image?.toMutableList(), listEventHome[position].content) }
        }
    }
}
