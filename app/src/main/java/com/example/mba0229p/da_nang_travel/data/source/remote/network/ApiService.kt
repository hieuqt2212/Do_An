package com.example.mba0229p.da_nang_travel.data.source.remote.network

import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") key: String,
                     @Query("sensor") sensor: String = "false",
                     @Query("mode") mode: String = "driving"): Single<DirectionMapResponse>

}
