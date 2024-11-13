package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class SaveDurationUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<SaveDurationUseCase.Params?, SaveDurationUseCase.Result?>() {
    override fun execute(params: Params?) {
        mDataSource.saveGameRoundDuration(params!!.gameRoundId, params.duration)
        callback!!.onSuccess(Result())
    }

    class Params(var gameRoundId: Int, var duration: Int) : UseCase.Params
    class Result : UseCase.Result
}
