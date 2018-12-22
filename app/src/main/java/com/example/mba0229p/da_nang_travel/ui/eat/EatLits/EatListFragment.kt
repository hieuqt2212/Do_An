package com.example.mba0229p.da_nang_travel.ui.eat.EatLits

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.BtnMapsEatEvent
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.eat.EatFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import kotlinx.android.synthetic.main.fragment_relax_list.*

class EatListFragment : BaseFragment() {

    private var list = mutableListOf<Relax>()
    private var eatListAdapter: EatListAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_relax_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eatListAdapter = EatListAdapter(list).apply {
            itemEatListener = {
                fragmentManager?.let { it1 ->
                    DialogUtils.showDialogDetail(it1, list[it], it) {
                        RxBus.publishToPublishSubject(BtnMapsEatEvent(it))
                    }
                }
            }
        }
        recyclerViewRelaxList.apply {
            adapter = eatListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onBindViewModel() {
        super.onBindViewModel()
        (parentFragment as? EatFragment)?.let { eatFragment ->
            addDisposables(eatFragment.handleUpdateListEat()
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
                            eatListAdapter?.notifyDataSetChanged()
                        }
                    })
        }
    }
}
