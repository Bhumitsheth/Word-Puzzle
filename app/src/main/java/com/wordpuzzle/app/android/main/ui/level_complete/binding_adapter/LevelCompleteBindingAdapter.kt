package com.wordpuzzle.app.android.main.ui.level_complete.binding_adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.utils.ImageLoaderUtils
import com.wordpuzzle.app.android.utils.convertTimeStringToDouble

@BindingAdapter("setLevelHandleImage")
fun setLevelHandleImage(image: ImageView, strTime: String?) {
    if (strTime.isNullOrEmpty()) {
        image.setImageResource(R.drawable.ic_play)
        return
    }

    val strTimeDouble = convertTimeStringToDouble(strTime) ?: 0.0
    val drawableResId = when {
        strTimeDouble in 0.0..1.0 -> R.drawable.ic_about
        strTimeDouble in 1.0..2.0 -> R.drawable.ic_play
        strTimeDouble in 2.0..3.0 -> R.drawable.ic_arrow_down
        strTimeDouble in 3.0..4.0 -> R.drawable.ic_audio_off
        strTimeDouble in 4.0..5.0 -> R.drawable.dialog_button_rounded_corners
        strTimeDouble in 5.0..6.0 -> R.drawable.ic_back
        strTimeDouble in 6.0..7.0 -> R.drawable.ic_button
        strTimeDouble in 7.0..8.0 -> R.drawable.ic_back
        strTimeDouble in 8.0..9.0 -> R.drawable.ic_button
        strTimeDouble in 9.0..10.0 -> R.drawable.ic_back
        else -> R.drawable.ic_play
    }

    ImageLoaderUtils.loadImageFromUrlViaGlide(image.context, drawableResId, image, drawableResId)
}