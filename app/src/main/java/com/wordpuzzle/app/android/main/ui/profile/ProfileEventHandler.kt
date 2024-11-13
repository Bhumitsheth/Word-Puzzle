package com.wordpuzzle.app.android.main.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.enums.Image
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.utils.CameraGalleryActivity

class ProfileEventHandler internal constructor(private val activity: ProfileActivity): BaseEventHandler() {

    override fun onBackClicked() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onSelectUserProfileClick() {
        val intent = Intent(activity, CameraGalleryActivity::class.java)
        intent.putExtra(AppConstants.FLAG_IS_SQUARE, true)
        intent.putExtra("compress", false)
        activity.startActivityForResult(intent, AppConstants.REQUEST_CODE_CAMERA_STORAGE_RESULT)
        CameraGalleryActivity.image = Image.ALL
    }
}