package com.example.mba0229p.da_nang_travel.data.source.datasource

import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponse
import io.reactivex.Single

interface DataSource {

    fun getDrectionMap(origin: String, destination: String, key: String): Single<DirectionMapResponse>
}
