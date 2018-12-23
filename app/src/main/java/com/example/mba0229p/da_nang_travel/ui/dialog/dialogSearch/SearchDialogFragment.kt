package com.example.mba0229p.da_nang_travel.ui.dialog.dialogSearch

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import com.example.mba0229p.da_nang_travel.R
import com.example.mba0229p.da_nang_travel.data.model.ItemSearch
import java.util.*


class SearchDialogFragment : DialogFragment() {

    companion object {
        var isShowing = false
    }

    private var itemListener: (Int) -> Unit = {}

    internal var list = mutableListOf<ItemSearch>()
    private var adapterSearch: SearchAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawableResource(R.color.colorDialogBackGroundTransparent)
            }
            setContentView(R.layout.dialog_search_fragment)

            val imgCloseSearch = findViewById<ImageView>(R.id.imgCloseSearch)
            val recyclerViewSearch = findViewById<RecyclerView>(R.id.recyclerViewSearch)
            val edtSearchFill = findViewById<EditText>(R.id.edtSearchFill)

            adapterSearch = SearchAdapter(list).apply {
                searchItemListener = {
                    itemListener.invoke(it)
                    dismiss()
                }
            }
            recyclerViewSearch.run {
                adapter = adapterSearch
                layoutManager = LinearLayoutManager(context)
            }

            imgCloseSearch.setOnClickListener {
                edtSearchFill.setText("")
            }

            edtSearchFill.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    adapterSearch?.filter(edtSearchFill.text.toString().toLowerCase(Locale.getDefault()))
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // No-oop
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // No-oop
                }

            })
        }
    }

    internal fun onItemClick(onItemClick: ((Int) -> Unit)) {
        this.itemListener = onItemClick
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