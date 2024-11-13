package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class ClearGameRoundsUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<UseCase.Params?, UseCase.Result?>() {
    override fun execute(params: Params?) {
        mDataSource.deleteGameRounds()
        callback!!.onSuccess(Result())
    }

    class Result : UseCase.Result
}
