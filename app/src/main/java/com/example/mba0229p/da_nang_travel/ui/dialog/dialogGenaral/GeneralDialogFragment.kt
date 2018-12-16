package com.example.mba0229p.da_nang_travel.ui.dialog.dialogGenaral

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.mba0229p.da_nang_travel.R
import kotlinx.android.synthetic.main.dialog_fragment_general.*

/**
 * GeneralDialogFragment
 */
class GeneralDialogFragment : DialogFragment() {
    internal var title: String? = null
    internal var message: String? = ""
    internal var rightButtonText: String? = null
    internal var leftButtonText: String? = null
    private var onDialogDismiss: (() -> Unit)? = null
    private var rightListener: View.OnClickListener? = null

    companion object {
        @Volatile
        internal var isShowing = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = super.onCreateDialog(savedInstanceState).apply {
        window?.run {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawableResource(R.color.dialogBackGroundTransparent)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_fragment_general, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        handleEvent()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (isShowing) {
            isShowing = false
            onDialogDismiss?.invoke()
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (!isShowing) {
            isShowing = true
            // Attach dialog with out common crash issues (lost state will be shown)
            manager?.beginTransaction()?.add(this, tag)?.commitAllowingStateLoss()
        }
    }

    /**
     * This function for handle when dismiss dialog
     */
    fun onDialogDismissListener(onDialogDismiss: (() -> Unit)?) {
        this.onDialogDismiss = onDialogDismiss
    }

    /**
     * This function for handle when click right button
     */
    fun rightButtonClick(listener: View.OnClickListener?) {
        rightListener = listener
    }

    private fun initView() {
        tvDialogMessage.text = message
        tvDialogTitle.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
        tvDialogTitle.text = title.toString()
        centerLine.visibility = if (leftButtonText.isNullOrEmpty()) View.GONE else View.VISIBLE
        btnDialogLeft.visibility = if (leftButtonText.isNullOrEmpty()) View.GONE else View.VISIBLE
        btnDialogLeft.text = leftButtonText.toString()
        btnDialogRight.visibility = if (rightButtonText.isNullOrEmpty()) View.GONE else View.VISIBLE
        if (leftButtonText.isNullOrEmpty()) {
//            btnDialogLeft.background = resources.getDrawable(R.drawable.bg_btn_dialog_bottom_round, resources.newTheme())
            btnDialogLeft.background = context?.resources?.getDrawable(R.drawable.bg_btn_dialog_bottom_round)
        }
        btnDialogRight.text = rightButtonText.toString()
    }

    private fun handleEvent() {
        btnDialogRight.setOnClickListener {
            rightListener?.onClick(view)
            dismiss()
        }

        btnDialogLeft.setOnClickListener {
            dismiss()
        }
    }
}
