package com.wordpuzzle.app.android.main.ui.home

import android.content.Intent
import androidx.core.view.GravityCompat
import com.wordpuzzle.app.android.databinding.ActivityHomeBinding
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity

class HomeEventHandler constructor(private val activity: HomeActivity, val binding: ActivityHomeBinding): BaseEventHandler() {

    override fun onBackClicked() {
        activity.finish()
    }

    fun clLoadGameClick() {
        val intent = Intent(activity, SelectBookPdfActivity::class.java)
//        val intent = Intent(activity, LoadingAnimationActivity::class.java)
        activity.startActivity(intent)
    }

    fun clNewGameClick() {
        val intent = Intent(activity, SelectBookPdfActivity::class.java)
        activity.startActivity(intent)
    }

    fun clLeaderboardClick() {
        val intent = Intent(activity, LeaderBoardActivity::class.java)
        activity.startActivity(intent)
    }

    fun onMenuClick() {
        with(binding.drawerLayout) {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            } else {
                openDrawer(GravityCompat.START)
            }
        }
    }

    fun onMenuCloseClick() {
        with(binding.drawerLayout) {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            } else {
                openDrawer(GravityCompat.START)
            }
        }
    }
}