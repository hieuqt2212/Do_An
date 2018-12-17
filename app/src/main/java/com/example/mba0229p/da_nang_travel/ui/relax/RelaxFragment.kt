package com.example.mba0229p.da_nang_travel.ui.relax

import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.data.source.Repository
import com.example.mba0229p.da_nang_travel.data.source.datasource.Constants
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_relax.*
import java.util.*


class RelaxFragment : BaseFragment() {

    private var publishUpdateListRelax: PublishSubject<MutableList<Relax>> = PublishSubject.create()
    private var currentLocation: Location? = null
    private var listRelax = mutableListOf<Relax>()
    private val repository = Repository()

    companion object {
        val listStyleSort = listOf("Khoảng cách", "Theo quận")
        val listQuan = listOf("Tất cả", "Hải Châu", "Thanh Khê", "Sơn Trà",
                "Ngũ Hành Sơn", "Liên Chiểu", "Hoà Vang", "Cẩm Lệ")
        val listDistance = listOf("Tất cả", "Dưới 5Km", "5Km - 10Km", "Trên 10Km")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get data relax from fire base
        context?.let { context ->
            Repository().relaxRepo(context).observeOnUiThread()
                    .doOnSubscribe { dialog?.show() }
                    .doFinally { dialog?.dismiss() }
                    .subscribe({
                        listRelax.clear()
                        listRelax.addAll(it)
                        updateList()
                    }, {})
        }
        return container?.initView(R.layout.fragment_relax)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
        initSpinner()
    }

    private fun initSpinner() {
        spinnerStyleSort.setItems(listStyleSort)
        spinnerStyleSort.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> spinnerListSort.setItems(listDistance)
                1 -> spinnerListSort.setItems(listQuan)
            }
            spinnerListSort.selectedIndex = 0
            spinnerListSort.visibility = View.VISIBLE
        }

        spinnerListSort.setOnItemSelectedListener { view, position, id, item ->
//            when (item) {
//                "Hải Châu" -> filter("Hải Châu")
//                "Thanh Khê" -> filter("Thanh Khê")
//            }
            Snackbar.make(view, "Clicked $item", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun initEvent() {
        imgSearch.setOnClickListener { _ ->
            val list = mutableListOf<ItemSearch>()
            listRelax.forEachIndexed { index, relax ->
                relax.nameLocation?.let { nameLocation ->
                    relax.address?.let { address ->
                        list.add(ItemSearch(nameLocation, address, index))
                    }
                }
            }
            fragmentManager?.let {
                DialogUtils.showDialogSearch(it, list) { position ->
                }
            }
        }
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
//        publishUpdateListRelax.onNext(listRelax)

//         Get Point in direction Google Api
        var count = 0
        if (!listRelax.isEmpty()) {
            if (currentLocation?.latitude != null && currentLocation?.longitude != null) {
                listRelax.forEach {
                    repository.getDrectionMap("${currentLocation?.latitude},${currentLocation?.longitude}",
                            "${it.lat},${it.lng}",
                            Constants.KEY_GOOGLE_MAP, "true", "driving")
                            .observeOnUiThread()
                            .doFinally {
                                count++
                                if (count == listRelax.size) {
                                    publishUpdateListRelax.onNext(listRelax)
                                }
                            }
                            .subscribe({ response ->
                                //                                Log.d("xxx", "respon=== $response")
                                if (response.routes.isNotEmpty()) {
                                    it.points = response.routes.first().overview_polyline.points
                                    it.distance = response.routes.first().legs.first().distance.value
                                    it.hour = response.routes.first().legs.first().duration.value
                                }
                            }, {})
                }
            } else {
                publishUpdateListRelax.onNext(listRelax)
            }
        }
    }

    private fun filter(charText: String) {
        val listFilter = mutableListOf<Relax>().apply {
            addAll(listRelax)
        }
        val text = charText.toLowerCase(Locale.getDefault())
        listRelax.clear()
        if (text.isEmpty()) {
            listRelax.addAll(listFilter)
        } else {
            listFilter.forEach {
                if (it.nameLocation?.toLowerCase(Locale.getDefault())?.contains(charText)!!) {
                    listRelax.add(it)
                } else {
                    if (it.address?.toLowerCase(Locale.getDefault())?.contains(charText)!!) {
                        listRelax.add(it)
                    }
                }
            }
        }
    }
}
