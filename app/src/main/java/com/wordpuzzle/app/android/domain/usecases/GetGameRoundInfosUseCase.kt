package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource.InfosCallback
import com.wordpuzzle.app.android.domain.model.GameRound
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class GetGameRoundInfosUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<GetGameRoundInfosUseCase.Params?, GetGameRoundInfosUseCase.Result?>() {
    override fun execute(params: Params?) {
        mDataSource.getGameRoundInfos(object : InfosCallback {
            override fun onLoaded(infoList: List<GameRound.Info?>?) {
                callback!!.onSuccess(Result(infoList))
            }
        })
    }

    class Params : UseCase.Params
    class Result(@JvmField var infoList: List<GameRound.Info?>?) : UseCase.Result
}
