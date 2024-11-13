package com.wordpuzzle.app.android.utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

object ImageLoaderUtils {
    fun <T> loadImageFromUrlViaGlide(context: Context, imgFile: T?, iv: ImageView, placeholderResourceID: Int) {
        val drawable = ContextCompat.getDrawable(context, placeholderResourceID)
        try {
            if (imgFile == null) {
                iv.setImageResource(placeholderResourceID)
            } else {
                Glide.with(context)
                    .load(imgFile)
                    .placeholder(placeholderResourceID)
                    .into(iv)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}