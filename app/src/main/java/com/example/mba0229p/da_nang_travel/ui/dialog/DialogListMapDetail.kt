package com.example.mba0229p.da_nang_travel.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.Window
import android.widget.TextView
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax


class DialogListMapDetail : DialogFragment() {

    internal var data: Relax? = null

    companion object {
        var isShowing = false
    }

    @SuppressLint("MissingPermission")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawableResource(R.color.colorDialogBackGroundTransparent)
            }
            setContentView(R.layout.dialog_fragment_detail)

            val btnCall = findViewById<TextView>(R.id.tvCall)
            val btnMaps = findViewById<TextView>(R.id.tvMaps)
            val tvItemTitle = findViewById<TextView>(R.id.tvItemTitle)
            val tvItemContent = findViewById<TextView>(R.id.tvItemContent)

            btnCall.setOnClickListener {
                if (data?.phone != null) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:" + data?.phone.toString())
                    context.startActivity(intent)
                }
            }

            tvItemTitle.text = data?.nameLocation

            tvItemContent.text = data?.description

            btnMaps.setOnClickListener {
                // Open Maps
            }
        }
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
}
