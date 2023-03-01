package com.team42.student_record_app.extentions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.isNotNullOrEmpty(errorString: String): Boolean {
    this.onChange { this.error = null }

    return if (this.text.toString().trim().isBlank()) {
        this.error = errorString
        false
    } else {
        true
    }
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
