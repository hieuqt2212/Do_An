package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.event.LocationEvent
import com.example.mba0229p.da_nang_travel.data.source.HomeRepository
import com.example.mba0229p.da_nang_travel.data.source.datasource.Constants
import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponce
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_relax_map.*

class RelaxMapFragment : BaseFragment() {
    private var mGoogleMap: GoogleMap? = null
    private var targetLocation: LatLng? = null
    private var userCurrentMarker: Marker? = null
    private val repository = HomeRepository()
    private var currentLocation: Location? = null
    private var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (mGoogleMap == null) {
            val supportMapFragment = SupportMapFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_map, supportMapFragment)?.commit()
            supportMapFragment.getMapAsync { googleMap ->
                mGoogleMap = googleMap
                mGoogleMap?.setOnMapLoadedCallback {
                    mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
                }
                currentLocation?.let { LocationEvent(it) }?.let { getCurrentLocation(it) }
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
    }

    private fun initViews() {
        handleNavigationBottom()
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        currentLocation = locationEvent.location
        userCurrentMarker = mGoogleMap?.addMarker(
                MarkerOptions()
                        .position(LatLng(locationEvent.location.latitude, locationEvent.location.longitude))
                        .title("Current location")).apply {
            this?.showInfoWindow()
        }
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationEvent.location.latitude, locationEvent.location.longitude), 16f))

        targetLocation = LatLng(16.0265452, 108.0304153)

        repository.getDrectionMap("${targetLocation?.latitude},${targetLocation?.longitude}",
                "${locationEvent.location.latitude},${locationEvent.location.longitude}",
                Constants.KEY_GOOGLE_MAP, "false", "driving")
                .observeOnUiThread()
                .subscribe({
                    drawPolyline(it)
                }, {})
    }

    private fun drawPolyline(directionResult: DirectionMapResponce) {
        polyline?.remove()
        val listLatLng = PolyUtil.decode(directionResult.routes.first().overview_polyline.points)
        currentLocation?.latitude?.let { lat ->
            currentLocation?.longitude?.let { lng ->
                listLatLng.add(LatLng(lat, lng))
            }
        }
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

    private fun handleListener() {
        imgMyLocation.setOnClickListener {
            currentLocation?.let { currentLocation ->
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 16f))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            imgArrow.setImageDrawable(ContextCompat.getDrawable(it,
                    if (slidingLayoutBottom.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) R.drawable.ic_map_arrow_down else R.drawable.ic_map_arrow_up))
        }
    }

    private fun handleNavigationBottom() {
        // Init for location
        slidingLayoutBottom.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        // Init bottom navigation
        slidingLayoutBottom.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
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
}
