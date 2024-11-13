package com.wordpuzzle.app.android.main.ui.home.binding_adapter

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.utils.*

@BindingAdapter("drawableImage")
fun setDrawableImage(view: ImageView, strImg: Int?) {
    view.setImageDrawable(ContextCompat.getDrawable(view.context, strImg ?: R.drawable.ic_back))
}

@BindingAdapter("navProfileImage")
fun setNavProfileImage(image: ShapeableImageView, profileImage: String?) {
    if (profileImage == null) return
    if (profileImage != "") {
        ImageLoaderUtils.loadImageFromUrlViaGlide(
            image.context,
            profileImage,
            image, R.drawable.ic_edit_user_profile
        )
    } else {
        image.setImageResource(R.drawable.ic_edit_user_profile)
    }
}