package com.example.mba0229p.da_nang_travel.data.source.util

import com.example.mba0229p.da_nang_travel.data.source.local.sharedPref.SharedPrefDataSourceImpl
import com.example.mba0229p.da_nang_travel.data.source.remote.RemoteDataSource

class Repository() {

    private val remoteDataSource = RemoteDataSource()
    private val shared = SharedPrefDataSourceImpl()
}
