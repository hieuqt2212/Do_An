package com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map

data class Routes(var overview_polyline: OverviewPolyline,
                  var legs: List<Legs>)
