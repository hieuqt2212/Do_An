package com.example.mba0229p.da_nang_travel.ui.relax

import android.location.Location
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.data.source.Repository
import com.example.mba0229p.da_nang_travel.data.source.datasource.Constants
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_relax.*

class RelaxFragment : BaseFragment() {

    private var publishUpdateListRelax: PublishSubject<MutableList<Relax>> = PublishSubject.create()
    private var currentLocation: Location? = null
    private var listRelax = mutableListOf<Relax>()
    private val repository = Repository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get data relax from fire base
        Repository().relaxRepo().observeOnUiThread()
                .doOnSubscribe { dialog?.show() }
                .doFinally { dialog?.dismiss() }
                .subscribe({
                    listRelax.clear()
                    listRelax.addAll(it)
                    updateList()
                }, {})
        return container?.initView(R.layout.fragment_relax)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initEvent() {
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        currentLocation = locationEvent.location
        updateList()
    }

    private fun initView() {
        tabRelax.run {
            removeAllTabs()
            addTab(tabRelax.newTab().setText("List"))
            addTab(tabRelax.newTab().setText("Map"))
        }
        viewpagerRelax?.adapter = RelaxViewPagerAdapter(childFragmentManager)
        viewpagerRelax?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabRelax))
        viewpagerRelax?.offscreenPageLimit = 2
        tabRelax?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpagerRelax))
    }

    fun handleUpdateListRelax(): PublishSubject<MutableList<Relax>> = publishUpdateListRelax

    private fun updateList() {
        var count = 0
        if (!listRelax.isEmpty()) {
            if (currentLocation?.latitude != null && currentLocation?.longitude != null) {
                listRelax.forEach {
                    repository.getDrectionMap("${currentLocation?.latitude},${currentLocation?.longitude}",
                            "${it.lat},${it.lng}",
                            Constants.KEY_GOOGLE_MAP, "false", "driving")
                            .observeOnUiThread()
                            .doFinally {
                                count++
                                if (count == listRelax.size) {
                                    publishUpdateListRelax.onNext(listRelax)
                                }
                            }
                            .subscribe({ response ->
                                it.points = response.routes.first().overview_polyline.points
                                it.distance = response.routes.first().legs.first().distance.value
                                it.hour = response.routes.first().legs.first().duration.value
                            }, {})
                }
            } else {
                publishUpdateListRelax.onNext(listRelax)
            }
        }
    }
}
