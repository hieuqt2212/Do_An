package com.example.mba0229p.da_nang_travel.utils

import android.app.Dialog
import android.content.Context
import android.support.v4.app.FragmentManager
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome.InfoHomeDialog

object DialogUtils {

    // Show progress loading
    fun showProgressDialog(context: Context) = Dialog(context, R.style.ProgressDialog).apply {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setContentView(R.layout.dialog_progress)
    }

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
