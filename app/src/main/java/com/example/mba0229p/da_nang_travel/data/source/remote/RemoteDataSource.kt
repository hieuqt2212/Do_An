package com.example.mba0229p.da_nang_travel.data.source.remote

import com.example.mba0229p.da_nang_travel.data.source.datasource.DataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.network.ApiClient
import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponse
import io.reactivex.Single

class RemoteDataSource : DataSource {

    private val apiService = ApiClient.getInstance(null).service

    override fun getDrectionMap(origin: String, destination: String, key: String): Single<DirectionMapResponse> =
            apiService.getDirection(origin, destination, key, "true", "driving")
}
