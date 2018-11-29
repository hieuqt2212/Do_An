package com.example.mba0229p.da_nang_travel.ui.relax

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.extension.initView
import com.example.mba0229p.da_nang_travel.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_relax.*

class RelaxFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.initView(R.layout.fragment_relax)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initEvent() {
    }

    private fun initView() {
        tabRelax.run {
            removeAllTabs()
            addTab(tabRelax.newTab().setText("List"))
            addTab(tabRelax.newTab().setText("Map"))
        }
        viewpagerRelax?.adapter = RelaxViewPagerAdapter(childFragmentManager)
        viewpagerRelax?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabRelax))
        tabRelax?.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpagerRelax))
    }
}
