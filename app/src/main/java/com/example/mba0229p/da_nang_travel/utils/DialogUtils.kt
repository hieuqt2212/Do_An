package com.example.mba0229p.da_nang_travel.utils

import android.support.v4.app.FragmentManager
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome.InfoHomeDialog

object DialogUtils {

    fun showDialogHomeInfo(fragmentManager: FragmentManager,
                           title: String? = null,
                           listImage: MutableList<String>? = null,
                           content: String? = null) {
        if (fragmentManager.findFragmentByTag(InfoHomeDialog::class.java.name) != null) {
            return
        }
        InfoHomeDialog().apply {
            this.title = title
            listImage?.let { this.listImage = it }
            this.content = content
        }.show(fragmentManager, InfoHomeDialog::class.java.name)
    }
}
