package com.example.mba0229p.da_nang_travel.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Handle AfterTextChanged for EditText
 */
fun EditText.afterTextChanged(afterTextChanged: (text: String) -> Unit) {
    object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (tag == this) {
                afterTextChanged(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No-op
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No-op
        }
    }.run {
        tag = this
        addTextChangedListener(this)
    }
}

fun EditText.removeAfterTextChangedListener() {
    removeTextChangedListener(tag as? TextWatcher)
}
