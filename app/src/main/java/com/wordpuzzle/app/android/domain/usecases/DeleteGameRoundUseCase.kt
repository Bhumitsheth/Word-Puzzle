package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class DeleteGameRoundUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<DeleteGameRoundUseCase.Params?, DeleteGameRoundUseCase.Result?>() {
    override fun execute(params: Params?) {
        mDataSource.deleteGameRound(params!!.gameRoundId)
        callback!!.onSuccess(Result())
    }

    class Params(var gameRoundId: Int) : UseCase.Params
    class Result : UseCase.Result
}
