package com.wordpuzzle.app.android.main.ui.play_game

import com.wordpuzzle.app.android.main.base.BaseEventHandler

class PlayGameHandler internal constructor(private val activity: PlayGameActivity): BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }
}