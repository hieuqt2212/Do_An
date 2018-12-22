package com.example.mba0229p.da_nang_travel.utils

import android.app.Dialog
import android.content.Context
import android.support.v4.app.FragmentManager
import android.view.View
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogDetail.DialogListMapDetail
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogGenaral.GeneralDialogFragment
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome.InfoHomeDialog
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogSearch.SearchDialogFragment

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

    /**
     *  This method will be used when no network connection
     */
    fun showErrorNetWorkDialog(fragmentManager: FragmentManager,
                               message: String? = null,
                               leftButtonText: String? = null,
                               rightButtonText: String? = null,
                               rightButtonListener: View.OnClickListener? = null) {
        if (fragmentManager.findFragmentByTag(GeneralDialogFragment::class.java.name) != null) {
            return
        }
        GeneralDialogFragment().apply {
            this.message = message
            this.leftButtonText = leftButtonText
            this.rightButtonText = rightButtonText
            this.rightButtonClick(rightButtonListener)
        }.show(fragmentManager, GeneralDialogFragment::class.java.name)
    }

    /**
     *  This method dialog default
     */
    fun showDialogDefault(fragmentManager: FragmentManager,
                          title: String? = null,
                          message: String? = null,
                          leftButtonText: String? = null,
                          rightButtonText: String? = null,
                          rightButtonListener: View.OnClickListener? = null,
                          onDialogDismiss: (() -> Unit)? = null,
                          isCancelable: Boolean = true) {
        if (fragmentManager.findFragmentByTag(GeneralDialogFragment::class.java.name) != null) {
            return
        }
        GeneralDialogFragment().apply {
            this.isCancelable = isCancelable
            this.title = title
            this.message = message
            this.leftButtonText = leftButtonText
            this.rightButtonText = rightButtonText
            this.rightButtonClick(rightButtonListener)
            this.onDialogDismissListener(onDialogDismiss)
        }.show(fragmentManager, GeneralDialogFragment::class.java.name)
    }

    /**
     *  Dialog search
     */
    fun showDialogSearch(fragmentManager: FragmentManager,
                         list: MutableList<ItemSearch>,
                         onItemListener: (Int) -> Unit) {
        if (fragmentManager.findFragmentByTag(SearchDialogFragment::class.java.name) != null) {
            return
        }
        SearchDialogFragment().apply {
            this.onItemClick(onItemListener)
            this.list = list
        }.show(fragmentManager, SearchDialogFragment::class.java.name)
    }

    /**
     * Dialog detail
     */
    fun showDialogDetail(fragmentManager: FragmentManager,
                         data: Relax,
                         position: Int,
                         onBtnMapsListener: (Int) -> Unit) {
        if (fragmentManager.findFragmentByTag(DialogListMapDetail::class.java.name) != null) {
            return
        }
        DialogListMapDetail().apply {
            this.btnMapsClick(onBtnMapsListener)
            this.position= position
            this.data = data
        }.show(fragmentManager, DialogListMapDetail::class.java.name)
    }
}
