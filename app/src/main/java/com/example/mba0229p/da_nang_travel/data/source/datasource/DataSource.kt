package com.example.mba0229p.da_nang_travel.data.source.datasource

import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponce
import io.reactivex.Single

interface DataSource {

    fun getDrectionMap(origin: String, destination: String, key: String, sensor: String, mode: String): Single<DirectionMapResponce>
}
