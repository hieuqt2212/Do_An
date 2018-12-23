package com.example.mba0229p.da_nang_travel.ui.hotel.hotelList

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.BtnMapsHotelEvent
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.hotel.HotelFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import kotlinx.android.synthetic.main.fragment_relax_list.*

class HotelListFragment : BaseFragment() {

    private var listHotel = mutableListOf<Relax>()
    private var hotelListAdapter: HotelListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_relax_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotelListAdapter = HotelListAdapter(listHotel).apply {
            itemHotelListener = {
                fragmentManager?.let { it1 ->
                    DialogUtils.showDialogDetail(it1, listHotel[it], it) {
                        RxBus.publishToPublishSubject(BtnMapsHotelEvent(it))
                    }
                }
            }
        }
        recyclerViewRelaxList.apply {
            adapter = hotelListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onBindViewModel() {
        super.onBindViewModel()
        (parentFragment as? HotelFragment)?.let { hotelFragment ->
            addDisposables(hotelFragment.handleUpdateListHotel()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.isEmpty()) {
                            recyclerViewRelaxList.visibility = View.GONE
                            tvNoData.visibility = View.VISIBLE
                        } else {
                            recyclerViewRelaxList.visibility = View.VISIBLE
                            tvNoData.visibility = View.GONE
                            listHotel.clear()
                            listHotel.addAll(it)
                            hotelListAdapter?.notifyDataSetChanged()
                        }
                    })
        }
    }
}
