package com.wordpuzzle.app.android.main.ui.leader_board.binding_adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.SuperscriptSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.utils.ImageLoaderUtils
import java.util.Locale

@BindingAdapter("playerImage")
fun setPlayerImage(image: ImageView, imageUrl: String?) {
    if (imageUrl == null) return
    if (imageUrl != "") {
        ImageLoaderUtils.loadImageFromUrlViaGlide(
            image.context,
            imageUrl,
            image, R.drawable.ic_player
        )
    } else {
        image.setImageResource(R.drawable.ic_player )
    }
}

@BindingAdapter("capitalizeFirst")
fun capitalizeFirst(textView: TextView, text: String?) {
    text?.let {
        val capitalizedText = it.take(1).uppercase(Locale.getDefault()) + it.drop(1).toLowerCase()
        textView.text = capitalizedText
    }
}

@BindingAdapter("imageResource")
fun ImageView.setImageResource(isLoaded: Boolean) {
    val resource = if (isLoaded) {
        R.drawable.ic_weekly_score
    } else {
        R.drawable.ic_monthly_score
    }
    this.setImageResource(resource)
}

@BindingAdapter("superscriptText")
fun setSuperscriptText(textView: TextView, text: String?) {
    text?.let {
        val spannableString = SpannableString(it)
        val index2 = it.indexOf('2')
        val indexNd = it.indexOf("nd")

        if (index2 != -1) {
            spannableString.setSpan(AbsoluteSizeSpan(32, true), index2, index2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (indexNd != -1) {
            spannableString.setSpan(AbsoluteSizeSpan(13, true), indexNd, indexNd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(SuperscriptSpan(), indexNd, indexNd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textView.text = spannableString
    }
}

@BindingAdapter("superscriptTextForThird")
fun superscriptTextForThird(textView: TextView, text: String?) {
    text?.let {
        val spannableString = SpannableString(it)
        val index2 = it.indexOf('3')
        val indexNd = it.indexOf("rd")

        if (index2 != -1) {
            spannableString.setSpan(AbsoluteSizeSpan(32, true), index2, index2 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (indexNd != -1) {
            spannableString.setSpan(AbsoluteSizeSpan(13, true), indexNd, indexNd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(SuperscriptSpan(), indexNd, indexNd + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textView.text = spannableString
    }
}