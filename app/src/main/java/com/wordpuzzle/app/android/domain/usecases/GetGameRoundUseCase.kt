package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.entity.GameRoundEntity
import com.wordpuzzle.app.android.domain.data.mapper.GameRoundMapper
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.GameRoundCallback
import com.wordpuzzle.app.android.domain.model.GameRound
import javax.inject.Inject

/**
 * Created by abdularis on 18/07/17.
 */
class GetGameRoundUseCase @Inject constructor(private val mGameRoundDataSource: GameRoundDataSource) :
    UseCase<GetGameRoundUseCase.Params?, GetGameRoundUseCase.Result?>() {
    override fun execute(params: Params?) {
        mGameRoundDataSource.getGameRound(params!!.gameRoundId, object : GameRoundCallback {
            override fun onLoaded(gameRoundEnt: GameRoundEntity?) {
                val gameRound = GameRoundMapper().map(gameRoundEnt)
                callback!!.onSuccess(Result(gameRound))
            }
        })
    }

    class Params(var gameRoundId: Int) : UseCase.Params
    class Result(@JvmField var gameRound: GameRound?) : UseCase.Result
}
