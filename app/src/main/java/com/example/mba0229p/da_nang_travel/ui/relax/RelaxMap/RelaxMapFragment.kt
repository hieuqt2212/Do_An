package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

//import com.example.mba0229p.da_nang_travel.utils.LocationUtil
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_relax_map.*

class RelaxMapFragment : BaseFragment() {
    private var mGoogleMap: GoogleMap? = null
    private var targetLocation: LatLng? = null
    private var userCurrentMarker: Marker? = null
    private val repository = HomeRepository()
    private var currentLocation: Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_relax_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = SupportMapFragment()
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_map, supportMapFragment)?.commit()
        supportMapFragment.getMapAsync { googleMap ->
            mGoogleMap = googleMap
            mGoogleMap?.setOnMapLoadedCallback {
                mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
                mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
            }
//            currentLocation = context?.let { LocationUtil(it).getCurrentLocation() }
            if (currentLocation != null) {
                currentLocation?.latitude?.let { latitude ->
                    currentLocation?.longitude?.let { longitude ->
                        userCurrentMarker = mGoogleMap?.addMarker(
                                MarkerOptions()
                                        .position(LatLng(latitude, longitude))
                                        .title("Current location")).apply {
                            this?.showInfoWindow()
                        }
                        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 16f))
                    }
                }
            } else {
                Toast.makeText(context, "Can't get current location", Toast.LENGTH_SHORT).show()
            }
        }

        targetLocation = LatLng(16.0719673, 108.2217511)

        repository.getDrectionMap("${targetLocation?.latitude},${targetLocation?.longitude}", "${currentLocation?.latitude}, ${currentLocation?.longitude}", Constants.KEY_GOOGLE_MAP, "false", "driving")
                .observeOnUiThread()
                .subscribe({
//                    drawPolyline(it)
                }, {})

        initViews()
        handleListener()
    }

    override fun getCurrentLocation(locationEvent: LocationEvent) {
        Log.d("location", "$locationEvent")
        currentLocation = locationEvent.location
    }


    override fun onResume() {
        super.onResume()
        context?.let {
            imgArrow.setImageDrawable(ContextCompat.getDrawable(it,
                    if (slidingLayoutBottom.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) R.drawable.ic_map_arrow_down else R.drawable.ic_map_arrow_up))
        }
    }


    private fun initViews() {
        handleNavigationBottom()
    }

    private fun drawPolyline(directionResult: DirectionMapResponce) {
        val listLatLng = PolyUtil.decode(directionResult.routes.first().overview_polyline.points)
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(listLatLng)
        polylineOptions.width(10.0f)
        polylineOptions.color(Color.BLUE)
        polylineOptions.geodesic(false)
        mGoogleMap?.addPolyline(polylineOptions)
    }

    private fun handleListener() {
        imgMyLocation.setOnClickListener { _ ->
            mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation?.longitude?.let { currentLocation?.latitude?.let { it1 -> LatLng(it1, it) } }, 16f))
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
