package com.example.mba0229p.da_nang_travel.ui.eat

import android.location.Location
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.BtnMapsEatEvent
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.data.source.Repository
import com.example.mba0229p.da_nang_travel.data.source.datasource.Constants
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import io.reactivex.subjects.PublishSubject
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus
import kotlinx.android.synthetic.main.fragment_relax.*
import java.util.*

class EatFragment : BaseFragment() {

    private var publishUpdateListEat: PublishSubject<MutableList<Relax>> = PublishSubject.create()
    private var publishMoveListMapEat: PublishSubject<Int> = PublishSubject.create()
    private var currentLocation: Location? = null
    private var listEat = mutableListOf<Relax>()
    private var listEatAll = mutableListOf<Relax>()
    private val repository = Repository()

    companion object {
        val listStyleSort = listOf("Khoảng cách", "Theo quận")
        val listQuan = listOf("Tất cả", "Hải Châu", "Thanh Khê", "Sơn Trà",
                "Ngũ Hành Sơn", "Liên Chiểu", "Hòa Vang", "Cẩm Lệ")
        val listDistance = listOf("Tất cả", "Dưới 5Km", "5Km - 10Km", "Trên 10Km")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get data relax from fire base
        context?.let { context ->
            Repository().eatRepo(context).observeOnUiThread()
                    .doOnSubscribe { dialog?.show() }
                    .doFinally { dialog?.dismiss() }
                    .subscribe({
                        listEat.clear()
                        listEat.addAll(it)
                        listEatAll.clear()
                        listEatAll.addAll(it)
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

    override fun onBindViewModel() {
        super.onBindViewModel()
        RxBus.listenPublisher(BtnMapsEatEvent::class.java)
                .observeOnUiThread()
                .subscribe {
                    viewpagerRelax.setCurrentItem(1, true)
                    publishMoveListMapEat.onNext(it.position)
                }
    }

    private fun initSpinner() {
        spinnerStyleSort.setItems(listStyleSort)
        spinnerStyleSort.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> spinnerListSort.setItems(listDistance)
                1 -> spinnerListSort.setItems(listQuan)
            }
            listEat.clear()
            listEat.addAll(listEatAll)
            publishUpdateListEat.onNext(listEat)
            spinnerListSort.selectedIndex = 0
            spinnerListSort.visibility = View.VISIBLE
        }
        spinnerListSort.setOnItemSelectedListener { _, _, _, item ->
            when (item) {
                "Hải Châu" -> filterQuan("Châu")
                "Thanh Khê" -> filterQuan("Thanh Khê")
                "Sơn Trà" -> filterQuan("Sơn Trà")
                "Ngũ Hành Sơn" -> filterQuan("Ngũ Hành Sơn")
                "Liên Chiểu" -> filterQuan("Liên Chiểu")
                "Hòa Vang" -> filterQuan("Hòa Vang")
                "Cẩm Lệ" -> filterQuan("Cẩm Lệ")
                "Dưới 5Km" -> {
                    filterDistance(1)
                }
                "5Km - 10Km" -> {
                    filterDistance(2)
                }
                "Trên 10Km" -> {
                    filterDistance(3)
                }
                else -> {
                    listEat.clear()
                    listEat.addAll(listEatAll)
                    publishUpdateListEat.onNext(listEat)
                }
            }
        }
    }

    private fun initEvent() {
        imgSearch.setOnClickListener { _ ->
            val list = mutableListOf<ItemSearch>()
            listEat.forEachIndexed { index, eat ->
                eat.nameLocation?.let { nameLocation ->
                    eat.address?.let { address ->
                        list.add(ItemSearch(nameLocation, address, index))
                    }
                }
            }
            fragmentManager?.let {
                DialogUtils.showDialogSearch(it, list) { position ->
                    viewpagerRelax.setCurrentItem(1, true)
                    publishMoveListMapEat.onNext(position)
                }
            }
        }
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        currentLocation = locationEvent.location
        updateList()
    }

    private fun initView() {
        tvTitleRelax.text = "Ăn uống"
        tabRelax.run {
            removeAllTabs()
            addTab(tabRelax.newTab().setText("Danh sách"))
            addTab(tabRelax.newTab().setText("Bản đồ"))
        }
        viewpagerRelax.run {
            adapter = EatViewPagerAdapter(childFragmentManager)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabRelax))
            offscreenPageLimit = 2
        }
        tabRelax?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpagerRelax))
    }

    fun handleUpdateListEat(): PublishSubject<MutableList<Relax>> = publishUpdateListEat
    fun handleMoveListMapEat(): PublishSubject<Int> = publishMoveListMapEat

    private fun updateList() {

        // Get Point in direction Google Api
        var count = 0
        if (!listEat.isEmpty()) {
            if (currentLocation?.latitude != null && currentLocation?.longitude != null) {
                listEat.forEach {
                    repository.getDrectionMap("${currentLocation?.latitude},${currentLocation?.longitude}",
                            "${it.lat},${it.lng}",
                            Constants.KEY_GOOGLE_MAP)
                            .observeOnUiThread()
                            .doFinally {
                                count++
                                if (count == listEat.size) {
                                    publishUpdateListEat.onNext(listEat)
                                }
                            }
                            .subscribe({ response ->
                                if (response.routes.isNotEmpty()) {
                                    it.points = response.routes.first().overview_polyline.points
                                    it.distance = response.routes.first().legs.first().distance.value
                                    it.hour = response.routes.first().legs.first().duration.value
                                }
                            }, {})
                }
            } else {
                publishUpdateListEat.onNext(listEat)
            }
        } else {
            publishUpdateListEat.onNext(listEat)
        }
    }

    private fun filterQuan(charText: String) {
        val listFilter = mutableListOf<Relax>().apply {
            addAll(listEatAll)
        }
        listEat.clear()
        listFilter.forEach {
            if (it.address?.toLowerCase(Locale.getDefault())?.contains(charText.toLowerCase(Locale.getDefault()))!!) {
                listEat.add(it)
            }
        }
        publishUpdateListEat.onNext(listEat)
    }

    private fun filterDistance(case: Int) {
        val listFilter = mutableListOf<Relax>().apply {
            addAll(listEatAll)
        }
        listEat.clear()
        when (case) {
            1 -> {
                listFilter.forEach { eat ->
                    eat.distance?.let {
                        if (it.toInt() < 5000) {
                            listEat.add(eat)
                        }
                    }
                }
            }
            2 -> {
                listFilter.forEach { eat ->
                    eat.distance?.let {
                        if (it.toInt() in 5000..9999) {
                            listEat.add(eat)
                        }
                    }
                }
            }
            3 -> {
                listFilter.forEach { eat ->
                    eat.distance?.let {
                        if (it.toInt() >= 10000) {
                            listEat.add(eat)
                        }
                    }
                }
            }
        }
        publishUpdateListEat.onNext(listEat)
    }
}
