package com.example.mba0229p.da_nang_travel.ui.home.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment

class InforFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            container?.initView(R.layout.fragment_info)
}
