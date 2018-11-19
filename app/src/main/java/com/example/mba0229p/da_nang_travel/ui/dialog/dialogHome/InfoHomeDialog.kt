package com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.mba0229p.da_nang_travel.R
import kotlinx.android.synthetic.main.dialog_fragment_home.*

class InfoHomeDialog : DialogFragment() {

    companion object {
        var isShowing = false
    }

    internal var listImage = mutableListOf<String>()
    internal var title: String? = null
    internal var content: String? = null
    private var imageAdapter: DialogHomeAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawableResource(R.color.colorDialogBackGroundTransparent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleEvent()
    }


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (isShowing) {
            isShowing = false
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (!isShowing) {
            isShowing = true
            // Attach dialog with out common crash issues (lost state will be shown)
            manager?.beginTransaction()?.add(this, tag)?.commitAllowingStateLoss()
        }
    }

    private fun initView() {
        imageAdapter = DialogHomeAdapter(listImage)
        recyclerViewItemAvatar.run {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        tvItemTitle.text = title
        tvItemContent.text = content
    }

    private fun handleEvent() {
        btnItemCancel.setOnClickListener {
            dismiss()
        }
    }
}
