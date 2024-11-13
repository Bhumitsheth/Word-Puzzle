package com.wordpuzzle.app.android.main.ui.play_game.binding_adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("selectWord")
fun setVisibilitySelectWordTextView(textView: TextView, selectWord: String?) {
    textView.apply {
        visibility = if (!selectWord.isNullOrBlank()) {
            text = selectWord
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}