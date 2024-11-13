package com.wordpuzzle.app.android.main.ui.loading_animation

import com.wordpuzzle.app.android.main.base.BaseEventHandler

class LoadingAnimationEventHandler constructor(private val activity: LoadingAnimationActivity): BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }
}