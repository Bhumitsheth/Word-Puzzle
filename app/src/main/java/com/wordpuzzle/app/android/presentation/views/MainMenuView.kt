package com.wordpuzzle.app.android.presentation.views

import com.wordpuzzle.app.android.domain.model.GameRound

/**
 * Created by abdularis on 20/07/17.
 */
interface MainMenuView {
    fun setNewGameLoading(enable: Boolean)
    fun showGameInfoList(infoList: List<GameRound.Info?>?)
    fun showNewlyCreatedGameRound(gameRound: GameRound?)
    fun showGameRound(gid: Int)
    fun clearInfoList()
    fun deleteInfo(info: GameRound.Info?)
}
