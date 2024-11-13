package com.wordpuzzle.app.android.presentation.presenters

import com.wordpuzzle.app.android.domain.usecases.DeleteGameRoundUseCase
import com.wordpuzzle.app.android.domain.usecases.GetGameRoundStatUseCase
import com.wordpuzzle.app.android.domain.usecases.UseCase
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import com.wordpuzzle.app.android.presentation.views.GameOverView
import javax.inject.Inject

class GameOverPresenter @Inject constructor(
    private val caseExecutor: UseCaseExecutor,
    private val gameRoundStatUseCase: GetGameRoundStatUseCase,
    private val deleteGameRoundUseCase: DeleteGameRoundUseCase
) {

    private var mView: GameOverView? = null

    fun setView(view: GameOverView) {
        mView = view
    }

    fun loadData(gid: Int) {
        gameRoundStatUseCase.params = GetGameRoundStatUseCase.Params(gid)
        caseExecutor.execute(gameRoundStatUseCase, object : UseCase.Callback<GetGameRoundStatUseCase.Result?> {
            override fun onSuccess(result: GetGameRoundStatUseCase.Result?) {
                mView?.showGameStat(result!!.gameRoundStat)
            }

            override fun onFailed(errMsg: String?) {}
        })
    }

    fun deleteGameRound(gid: Int) {
        deleteGameRoundUseCase.params = DeleteGameRoundUseCase.Params(gid)
        caseExecutor.execute(deleteGameRoundUseCase, object : UseCase.Callback<DeleteGameRoundUseCase.Result?> {
            override fun onSuccess(result: DeleteGameRoundUseCase.Result?) {}

            override fun onFailed(errMsg: String?) {}
        })
    }
}
