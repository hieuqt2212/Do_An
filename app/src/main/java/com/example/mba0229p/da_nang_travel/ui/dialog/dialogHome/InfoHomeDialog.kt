package com.example.mba0229p.da_nang_travel.ui.dialog.dialogHome

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.Window
import android.widget.TextView
import com.example.mba0229p.da_nang_travel.R

class InfoHomeDialog : DialogFragment() {

    companion object {
        var isShowing = false
    }

    internal var listImage = mutableListOf<String>()
    internal var title: String? = null
    internal var content: String? = null
    internal var date: String? = null
    internal var day: String? = null
    private var imageAdapter: DialogHomeAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawableResource(R.color.colorDialogBackGroundTransparent)
            }
            setContentView(R.layout.dialog_fragment_home)

            val tvItemTitle = findViewById<TextView>(R.id.tvItemTitle)
            val tvDate = findViewById<TextView>(R.id.tvDate)
            val tvDay = findViewById<TextView>(R.id.tvDay)
            val tvItemContent = findViewById<TextView>(R.id.tvItemContent)
            val recyclerViewItemAvatar = findViewById<RecyclerView>(R.id.recyclerViewItemAvatar)

            imageAdapter = DialogHomeAdapter(listImage)
            recyclerViewItemAvatar.run {
                adapter = imageAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            tvDate.text = date
            tvDay.text = day
            tvItemTitle.text = title
            tvItemContent.text = content
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (isShowing) {
            isShowing = false
        }
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        dialog.window.let {
            it.windowManager.defaultDisplay.getMetrics(displayMetrics)
            it.attributes = dialog.window.attributes.apply {
                width = displayMetrics.widthPixels - displayMetrics.widthPixels / 5
                height = displayMetrics.heightPixels - displayMetrics.heightPixels / 6
            }
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
