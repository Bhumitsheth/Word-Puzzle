package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.StatCallback
import com.wordpuzzle.app.android.domain.model.GameRoundStat
import javax.inject.Inject

/**
 * Created by abdularis on 24/07/17.
 */
class GetGameRoundStatUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<GetGameRoundStatUseCase.Params?, GetGameRoundStatUseCase.Result?>() {
    override fun execute(params: Params?) {
        mDataSource.getGameRoundStat(params!!.gameRoundId, object : StatCallback {
            override fun onLoaded(stat: GameRoundStat?) {
                callback!!.onSuccess(Result(stat))
            }
        })
    }

    class Params(var gameRoundId: Int) : UseCase.Params
    class Result(@JvmField var gameRoundStat: GameRoundStat?) : UseCase.Result
}
