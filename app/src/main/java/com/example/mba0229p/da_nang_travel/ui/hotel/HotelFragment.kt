package com.example.mba0229p.da_nang_travel.ui.hotel

import android.location.Location
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.BtnMapsHotelEvent
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

class HotelFragment : BaseFragment() {

    private var publishUpdateListHotel: PublishSubject<MutableList<Relax>> = PublishSubject.create()
    private var publishMoveListMapHotel: PublishSubject<Int> = PublishSubject.create()
    private var currentLocation: Location? = null
    private var listHotel = mutableListOf<Relax>()
    private var listHotelAll = mutableListOf<Relax>()
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
            Repository().guestHouseRepo(context).observeOnUiThread()
                    .doOnSubscribe { dialog?.show() }
                    .doFinally { dialog?.dismiss() }
                    .subscribe({
                        listHotel.clear()
                        listHotel.addAll(it)
                        publishUpdateListHotel.onNext(listHotel)
                        listHotelAll.clear()
                        listHotelAll.addAll(it)
                        updateList()
                    }, {
                        publishUpdateListHotel.onNext(listHotel)
                    })
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
        RxBus.listenPublisher(BtnMapsHotelEvent::class.java)
                .observeOnUiThread()
                .subscribe {
                    viewpagerRelax.setCurrentItem(1, true)
                    publishMoveListMapHotel.onNext(it.position)
                }
    }

    private fun initSpinner() {
        spinnerStyleSort.setItems(listStyleSort)
        spinnerStyleSort.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> spinnerListSort.setItems(listDistance)
                1 -> spinnerListSort.setItems(listQuan)
            }
            listHotel.clear()
            listHotel.addAll(listHotelAll)
            publishUpdateListHotel.onNext(listHotel)
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
                    listHotel.clear()
                    listHotel.addAll(listHotelAll)
                    publishUpdateListHotel.onNext(listHotel)
                }
            }
        }
    }

    private fun initEvent() {
        imgSearch.setOnClickListener { _ ->
            val list = mutableListOf<ItemSearch>()
            listHotel.forEachIndexed { index, hotel ->
                hotel.nameLocation?.let { nameLocation ->
                    hotel.address?.let { address ->
                        list.add(ItemSearch(nameLocation, address, index))
                    }
                }
            }
            fragmentManager?.let {
                DialogUtils.showDialogSearch(it, list) { position ->
                    DialogUtils.showDialogDetail(it, listHotel[position], position) {
                        RxBus.publishToPublishSubject(BtnMapsHotelEvent(position))
                    }
                }
            }
        }
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        currentLocation = locationEvent.location
        updateList()
    }

    private fun initView() {
        tvTitleRelax.text = "Nhà nghỉ"
        tabRelax.run {
            removeAllTabs()
            addTab(tabRelax.newTab().setText("Danh sách"))
            addTab(tabRelax.newTab().setText("Bản đồ"))
        }
        viewpagerRelax.run {
            adapter = HotelAdapter(childFragmentManager)
            setEnabledSwiping(false)
            setOnTouchListener { _, _ -> true }
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabRelax))
            offscreenPageLimit = 2
        }
        tabRelax?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpagerRelax))
    }

    fun handleUpdateListHotel(): PublishSubject<MutableList<Relax>> = publishUpdateListHotel
    fun handleMoveListMapHotel(): PublishSubject<Int> = publishMoveListMapHotel

    private fun updateList() {

        // Get Point in direction Google Api
        var count = 0
        if (!listHotel.isEmpty()) {
            if (currentLocation?.latitude != null && currentLocation?.longitude != null) {
                listHotel.forEach {
                    repository.getDrectionMap("${currentLocation?.latitude},${currentLocation?.longitude}",
                            "${it.lat},${it.lng}",
                            Constants.KEY_GOOGLE_MAP)
                            .observeOnUiThread()
                            .doFinally {
                                count++
                                if (count == listHotel.size) {
                                    publishUpdateListHotel.onNext(listHotel)
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
                publishUpdateListHotel.onNext(listHotel)
            }
        } else {
            publishUpdateListHotel.onNext(listHotel)
        }
    }

    private fun filterQuan(charText: String) {
        val listFilter = mutableListOf<Relax>().apply {
            addAll(listHotelAll)
        }
        listHotel.clear()
        listFilter.forEach {
            if (it.address?.toLowerCase(Locale.getDefault())?.contains(charText.toLowerCase(Locale.getDefault()))!!) {
                listHotel.add(it)
            }
        }
        publishUpdateListHotel.onNext(listHotel)
    }

    private fun filterDistance(case: Int) {
        val listFilter = mutableListOf<Relax>().apply {
            addAll(listHotelAll)
        }
        listHotel.clear()
        when (case) {
            1 -> {
                listFilter.forEach { hotel ->
                    hotel.distance?.let {
                        if (it.toInt() < 5000) {
                            listHotel.add(hotel)
                        }
                    }
                }
            }
            2 -> {
                listFilter.forEach { hotel ->
                    hotel.distance?.let {
                        if (it.toInt() in 5000..9999) {
                            listHotel.add(hotel)
                        }
                    }
                }
            }
            3 -> {
                listFilter.forEach { hotel ->
                    hotel.distance?.let {
                        if (it.toInt() >= 10000) {
                            listHotel.add(hotel)
                        }
                    }
                }
            }
        }
        publishUpdateListHotel.onNext(listHotel)
    }
}
