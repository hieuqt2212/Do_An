package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.example.mba0229p.da_nang_travel.ui.relax.RelaxFragment
import com.example.mba0229p.da_nang_travel.utils.DialogUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_relax_map.*

class RelaxMapFragment : BaseFragment() {
    private var mGoogleMap: GoogleMap? = null
    private var targetLocation: LatLng? = null
    private var currentLocation: Location? = null
    private var polyline: Polyline? = null
    private var mapAdapter: RelaxMapAdapter? = null
    private var linearSnapHelper: LinearSnapHelper? = null
    private var listRelax = mutableListOf<Relax>()


    // Maps change
    private var isReadyToCalculate = false
    private var heightFrameLayout = 0

    private val currentLocationButtonLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
        gravity = Gravity.BOTTOM or Gravity.RIGHT
    }
    private val frameLayoutVisibilityListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        // Check params of margin
        if (!isReadyToCalculate) {
            // Update calculate
            val itemCount = relaxRecyclerView.layoutManager?.itemCount ?: 0
            if (itemCount > 0) {
                val childView = relaxRecyclerView.layoutManager?.findViewByPosition(0)
                childView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                heightFrameLayout = childView?.let { (it.measuredHeight.plus(it.paddingTop).plus(it.paddingBottom)) } ?: heightFrameLayout
                isReadyToCalculate = true
            } else {
                heightFrameLayout = tvNoData.height
            }
            // Update layout
            if (heightFrameLayout > 0) {
                val layoutParams = flContent.layoutParams
                layoutParams.height = heightFrameLayout
                flContent.requestLayout()
            }
            // Update the margin of current location button
            currentLocationButtonLayoutParams.setMargins(0, 0, 0,
                    if (slidingLayoutBottom.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) heightFrameLayout + slidingLayoutBottom.currentParallaxOffset
                    else 0)
            // Update the margin data into current location button
            imgMyLocation.layoutParams = currentLocationButtonLayoutParams
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mGoogleMap == null) {
            val supportMapFragment = SupportMapFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_map, supportMapFragment)?.commit()
            supportMapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap
                mGoogleMap?.setOnMapLoadedCallback {
                    mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    mGoogleMap?.uiSettings?.isZoomControlsEnabled = false
                    mGoogleMap?.uiSettings?.isMapToolbarEnabled = false
                }
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relax_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleListener()
        initAdapter()
        handleScrollMapItem()

    }

    private fun initAdapter() {
        mapAdapter = RelaxMapAdapter(listRelax, this::onItemClick)
        relaxRecyclerView.apply {
            adapter = mapAdapter
            // Init snap for list
            if (linearSnapHelper == null) {
                linearSnapHelper = LinearSnapHelper()
            } else {
                clearOnScrollListeners()
                onFlingListener = null
            }
            linearSnapHelper?.attachToRecyclerView(this)
            // Attach decoration
            addItemDecoration(ItemMapViewDecoration())
            // Update layout manager
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        }
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {

        // Init current location
        currentLocation = locationEvent.location

        // Draw marker current location
        mGoogleMap?.addMarker(
                MarkerOptions()
                        .position(LatLng(locationEvent.location.latitude, locationEvent.location.longitude))
                        .title("Current location")).apply {
            this?.showInfoWindow()
        }
    }

    override fun onResume() {
        super.onResume()
        isReadyToCalculate = false
        activity?.window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener(frameLayoutVisibilityListener)
        context?.let {
            imgArrow.setImageDrawable(ContextCompat.getDrawable(it,
                    if (slidingLayoutBottom.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) R.drawable.ic_map_arrow_down else R.drawable.ic_map_arrow_up))
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.decorView?.viewTreeObserver?.removeOnGlobalLayoutListener(frameLayoutVisibilityListener)
    }

    override fun onBindViewModel() {
        super.onBindViewModel()
        (parentFragment as RelaxFragment).apply {
            addDisposables(this@apply.handleUpdateListRelax()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe { listRepo ->
                        listRelax.clear()
                        listRelax.addAll(listRepo)
                        mapAdapter?.notifyDataSetChanged()
                        listRepo.forEach {
                            it.lat?.let { lat ->
                                it.lng?.let { lng ->
                                    it.nameLocation?.let { title ->
                                        drawMarker(LatLng(lat, lng), title)
                                    }
                                }
                            }
                        }
                        listRepo.first().apply {
                            this.points?.let { point ->
                                this.lat?.let { lat ->
                                    this.lng?.let { lng ->
                                        drawPolyline(point, LatLng(lat, lng))
                                    }
                                }
                            }
                        }

                        //show first
//                        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(listRelax[0].lat?.let { listRelax[0].lng?.let { it1 -> LatLng(it, it1) } }, 16f))
                        listRelax[0].lat?.let { listRelax[0].lng?.let { it1 -> LatLng(it, it1) } }?.let { showPosition(it) }
                    })
        }
    }

    private fun drawMarker(latLng: LatLng, title: String) {
        mGoogleMap?.addMarker(
                MarkerOptions()
                        .position(latLng)
                        .title(title)).apply {
            this?.showInfoWindow()
        }
    }

    private fun drawPolyline(points: String, latLng: LatLng) {
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        polyline?.remove()
        val listLatLng = PolyUtil.decode(points)
        currentLocation?.latitude?.let { lat ->
            currentLocation?.longitude?.let { lng ->
                listLatLng.add(0, LatLng(lat, lng))
            }
        }
        listLatLng.add(latLng)
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(listLatLng)
        polylineOptions.width(10.0f)
        polylineOptions.color(Color.BLUE)
        polylineOptions.geodesic(false)
        polyline = mGoogleMap?.addPolyline(polylineOptions)
        targetLocation?.latitude?.let { latitude ->
            targetLocation?.longitude?.let { longitude ->
                mGoogleMap?.addMarker(
                        MarkerOptions()
                                .position(LatLng(latitude, longitude))
                                .title("Bà Nà Hill")).apply {
                    this?.showInfoWindow()
                }
            }
        }
    }

    private fun handleNavigationBottom() {
        // Init for location
        slidingLayoutBottom.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        // Init bottom navigation
        slidingLayoutBottom.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {

                currentLocationButtonLayoutParams.setMargins(0, 0, 0,
                        if (relaxRecyclerView.measuredHeight > 0)
                            ((relaxRecyclerView.measuredHeight + slidingLayoutBottom.currentParallaxOffset) * slideOffset).toInt()
                        else
                            ((flContent.measuredHeight + slidingLayoutBottom.currentParallaxOffset) * slideOffset).toInt())
                imgMyLocation.layoutParams = currentLocationButtonLayoutParams
            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                context?.let { context ->
                    if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                        imgArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_map_arrow_down))
                        return
                    }
                    if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED || newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        imgArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_map_arrow_down))
                        return
                    }
                    if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED || newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        imgArrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_map_arrow_up))
                    }
                }
            }
        })
    }

    private fun initViews() {
        handleNavigationBottom()
    }

    private fun handleListener() {
        imgMyLocation.setOnClickListener {
            if (currentLocation == null) {
                fragmentManager?.let { it1 ->
                    DialogUtils.showDialogDefault(it1,
                            null,
                            "Vui lòng bật chức năng định vị!!!",
                            null,
                            getString(android.R.string.ok))
                }
            } else {
                currentLocation?.let { currentLocation ->
                    showPosition(LatLng(currentLocation.latitude, currentLocation.longitude))
                }
            }
        }
    }

    private fun handleScrollMapItem() {
        relaxRecyclerView?.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        val layout = recyclerView.layoutManager as LinearLayoutManager
                        val position = layout.findFirstCompletelyVisibleItemPosition()
                        // Get first position scroll
                        listRelax.let { todoList ->
                            if (position >= 0 && position < todoList.size) {
                                listRelax[position].lat?.let { listRelax[position].lng?.let { it1 -> LatLng(it, it1) } }?.let { showPosition(it) }
                            }
                        }
                    }
                }
            })
        }
    }

    private fun showPosition(latLng: LatLng) {
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latLng.latitude, latLng.longitude), 16f))
    }


    private fun onItemClick(itemRelax: Relax, position: Int) {
        Toast.makeText(context, "ITEMS = $position ,  $itemRelax", Toast.LENGTH_SHORT).show()
    }
}
