package com.wordpuzzle.app.android.domain.data.source

import com.wordpuzzle.app.android.domain.data.entity.GameRoundEntity
import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.domain.model.GameRoundStat
import com.wordpuzzle.app.android.domain.model.UsedWord

/**
 * Created by abdularis on 18/07/17.
 */
interface GameRoundDataSource {
    interface GameRoundCallback {
        fun onLoaded(gameRound: GameRoundEntity?)
    }

    interface InfosCallback {
        fun onLoaded(infoList: List<GameRound.Info?>?)
    }

    interface StatCallback {
        fun onLoaded(stat: GameRoundStat?)
    }

    fun getGameRound(gid: Int, callback: GameRoundCallback?)
    fun getGameRoundInfos(callback: InfosCallback?)
    fun getGameRoundStat(gid: Int, callback: StatCallback?)
    fun saveGameRound(gameRound: GameRoundEntity?)
    fun deleteGameRound(gid: Int)
    fun deleteGameRounds()
    fun saveGameRoundDuration(gid: Int, newDuration: Int)
    fun markWordAsAnswered(usedWord: UsedWord?)
}
