package com.wordpuzzle.app.android.main.ui.level_complete

import android.content.Intent
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardActivity
import com.wordpuzzle.app.android.main.ui.loading_animation.LoadingAnimationActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity

class LevelCompleteHandler internal constructor(private val activity: LevelCompleteActivity): BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }

    fun imageGoToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
    }

    fun nextLevelClick() {
        val intent = Intent(activity, LoadingAnimationActivity::class.java)
        intent.putExtra(AppConstants.bookId, activity.viewModel.bookId.value)
        intent.putExtra(AppConstants.bookName, activity.viewModel.bookName.value)
        intent.putExtra(AppConstants.selectPageNo, activity.viewModel.selectPageNo.value)
        activity.startActivity(intent)
    }

    fun newGameClick() {
        val intent = Intent(activity, SelectBookPdfActivity::class.java)
        activity.startActivity(intent)
    }

    fun leaderBoardClick() {
        val intent = Intent(activity, LeaderBoardActivity::class.java)
        activity.startActivity(intent)
    }
}