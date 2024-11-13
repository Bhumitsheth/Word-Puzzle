package com.wordpuzzle.app.android.main.ui.about_us

import com.wordpuzzle.app.android.databinding.ActivityAboutUsBinding
import com.wordpuzzle.app.android.main.base.BaseEventHandler

class AboutUsEventHandler constructor(private val activity: AboutUsActivity, val binding: ActivityAboutUsBinding): BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }
}