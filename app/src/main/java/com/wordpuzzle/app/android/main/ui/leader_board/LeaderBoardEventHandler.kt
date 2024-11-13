package com.wordpuzzle.app.android.main.ui.leader_board

import com.wordpuzzle.app.android.main.base.BaseEventHandler

class LeaderBoardEventHandler constructor(private val activity: LeaderBoardActivity): BaseEventHandler() {

    override fun onBackClicked() {
        activity.finish()
    }
}