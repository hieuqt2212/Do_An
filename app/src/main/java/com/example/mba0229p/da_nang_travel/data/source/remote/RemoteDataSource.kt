package com.example.mba0229p.da_nang_travel.data.source.remote

import com.example.mba0229p.da_nang_travel.data.source.datasource.DataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.network.ApiClient

class RemoteDataSource : DataSource {

    private val apiService = ApiClient.getInstance(null).service
}
