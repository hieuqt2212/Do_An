package com.example.mba0229p.da_nang_travel.ui.relax.RelaxMap

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.source.HomeRepository
import com.example.mba0229p.da_nang_travel.data.source.datasource.Constants
import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponce
import com.example.mba0229p.da_nang_travel.extension.observeOnUiThread
import com.example.mba0229p.da_nang_travel.utils.LocationUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.fragment_relax_map.*

class RelaxMapFragment : Fragment() {
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
            currentLocation = context?.let { LocationUtil(it).getCurrentLocation() }
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

        repository.getDrectionMap("${targetLocation?.latitude},${targetLocation?.longitude}", "16.064563,108.149713", Constants.KEY_GOOGLE_MAP, "false", "driving")
                .observeOnUiThread()
                .subscribe({
                    drawPolyline(it)
                }, {})
        handleListener()
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
            val xxx = context?.let { LocationUtil(it).getCurrentLocation() }
            xxx?.let { location ->
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16f))
            }
        }
    }
}
