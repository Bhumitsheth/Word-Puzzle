package com.wordpuzzle.app.android.main.ui.profile.data_binding

import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.utils.ImageLoaderUtils

@BindingAdapter("profileImage")
fun setProfileImage(image: ShapeableImageView, profileImage: String?) {
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