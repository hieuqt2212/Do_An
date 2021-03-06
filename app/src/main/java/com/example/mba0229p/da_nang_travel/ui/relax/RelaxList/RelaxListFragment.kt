package com.example.mba0229p.da_nang_travel.ui.relax.RelaxList

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.BtnMapsRelaxEvent
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.relax.RelaxFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import kotlinx.android.synthetic.main.fragment_relax_list.*

class RelaxListFragment : BaseFragment() {

    private var list = mutableListOf<Relax>()
    private var relaxListAdapter: RelaxListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_relax_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relaxListAdapter = RelaxListAdapter(list).apply {
            itemRelaxListener = {
                fragmentManager?.let { it1 ->
                    DialogUtils.showDialogDetail(it1, list[it], it) {
                        RxBus.publishToPublishSubject(BtnMapsRelaxEvent(it))
                    }
                }
            }
        }
        recyclerViewRelaxList.apply {
            adapter = relaxListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onBindViewModel() {
        super.onBindViewModel()
        (parentFragment as RelaxFragment).apply {
            addDisposables(this@apply.handleUpdateListRelax()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.isEmpty()) {
                            recyclerViewRelaxList.visibility = View.GONE
                            tvNoData.visibility = View.VISIBLE
                        } else {
                            recyclerViewRelaxList.visibility = View.VISIBLE
                            tvNoData.visibility = View.GONE
                            list.clear()
                            list.addAll(it)
                            relaxListAdapter?.notifyDataSetChanged()
                        }
                    })
        }
    }
}
