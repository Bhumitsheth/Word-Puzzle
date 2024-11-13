package com.wordpuzzle.app.android.presentation.views

import com.wordpuzzle.app.android.domain.model.GameRoundStat

/**
 * Created by abdularis on 23/07/17.
 */
interface GameOverView {
    fun showGameStat(stat: GameRoundStat?)
}
