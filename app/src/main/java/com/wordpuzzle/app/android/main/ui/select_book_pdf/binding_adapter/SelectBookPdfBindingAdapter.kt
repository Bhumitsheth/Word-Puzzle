package com.wordpuzzle.app.android.main.ui.select_book_pdf.binding_adapter

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.wordpuzzle.app.android.R

@BindingAdapter("selectPageNoSpinner")
fun setSelectPageNoSpinner(spinner: Spinner, list: List<String>?) {
    if (list == null) {
        return
    }
    val adapter = ArrayAdapter(spinner.context, R.layout.layout_spinner_text_small, list)
    adapter.setDropDownViewResource(R.layout.layout_spinner_dropdown_small)
    spinner.adapter = adapter
}