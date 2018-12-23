package com.example.mba0229p.da_nang_travel.ui.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.data.source.Repository
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.home.header_home.HeaderAdapter
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit

class HomeFragment : BaseFragment() {
    private var positionItemHeaderHome = 0
    private var adapterImage: ImageHomeAdapter? = null
    private var listEventHome = mutableListOf<EventHome>()
    private var listImageHome = mutableListOf(R.drawable.ic_dn1, R.drawable.ic_dn2, R.drawable.ic_dn3, R.drawable.ic_dn4, R.drawable.ic_dn5, R.drawable.ic_dn6, R.drawable.ic_dn7)
    private var adapterEvent: EventHomeAdapter? = null
    private val listImageHeader = mutableListOf(R.drawable.bg_banner_app, R.drawable.bg_banner_app_2, R.drawable.bg_banner_app_3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.initView(R.layout.fragment_home)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enable swiping view pager header
        viewPagerHeaderHome.run {
            adapter = HeaderAdapter(context, listImageHeader)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
        }

        // Set auto change image in header
        Observable.interval(2, TimeUnit.SECONDS)
                .observeOnUiThread()
                .subscribe {
                    if (it.toInt() % 2 == 0) {
                        if (positionItemHeaderHome < listImageHeader.size - 1) positionItemHeaderHome += 1 else positionItemHeaderHome = 0
                        if (isAdded) {
                            viewPagerHeaderHome.setCurrentItem(positionItemHeaderHome, true)
                        }
                    }
                }
        initAdapter()
        initView()
        initListener()
    }

    private fun initAdapter() {
        adapterEvent = EventHomeAdapter(listEventHome)
        recyclerViewEvent.run {
            adapter = adapterEvent
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        adapterImage = ImageHomeAdapter(listImageHome)
        recyclerViewImageHome.run {
            adapter = adapterImage
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initView() {
        context?.let { context ->
            Repository().homeEventRepo(context)
                    .doOnSubscribe { dialog?.show() }
                    .doFinally { dialog?.dismiss() }
                    .subscribe({
                        listEventHome.clear()
                        listEventHome.addAll(it)
                        adapterEvent?.notifyDataSetChanged()
                    }, {})
        }
    }

    private fun initListener() {
        adapterEvent?.itemEventListener = { position ->
            fragmentManager?.let {
                DialogUtils.showDialogHomeInfo(it,
                        listEventHome[position].nameEvent,
                        listEventHome[position].month.toString(),
                        listEventHome[position].day.toString(),
                        listEventHome[position].image?.toMutableList(),
                        listEventHome[position].content)
            }
        }
    }
}
