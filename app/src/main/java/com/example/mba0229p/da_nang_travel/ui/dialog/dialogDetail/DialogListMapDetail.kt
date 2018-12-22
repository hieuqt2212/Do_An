package com.example.mba0229p.da_nang_travel.ui.dialog.dialogDetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.TextView
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome.DialogHomeAdapter


class DialogListMapDetail : DialogFragment() {

    internal var data: Relax? = null
    internal var position: Int? = null
    private var btnMapsListener: (Int) -> Unit = {}
    private var imageAdapter: DialogHomeAdapter? = null


    companion object {
        @Volatile
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
            val recyclerViewItemAvatar = findViewById<RecyclerView>(R.id.recyclerViewItemAvatar)

            imageAdapter = data?.image?.let {
                DialogHomeAdapter(it)
            }
            recyclerViewItemAvatar.run {
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }

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
                dismiss()
                position?.let { it1 -> btnMapsListener.invoke(it1) }
            }
        }
    }

    internal fun btnMapsClick(btnMapsClick: ((Int) -> Unit)) {
        this.btnMapsListener = btnMapsClick
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
