package com.wordpuzzle.app.android.presentation.presenters

import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.domain.usecases.BuildGameRoundUseCase
import com.wordpuzzle.app.android.domain.usecases.ClearGameRoundsUseCase
import com.wordpuzzle.app.android.domain.usecases.DeleteGameRoundUseCase
import com.wordpuzzle.app.android.domain.usecases.GetGameRoundInfosUseCase
import com.wordpuzzle.app.android.domain.usecases.UseCase
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.presentation.views.MainMenuView
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class MainMenuPresenter @Inject constructor(
    private val mCaseExecutor: UseCaseExecutor,
    private val mBuildGameRoundUseCase: BuildGameRoundUseCase,
    private val mGameRoundInfosUseCase: GetGameRoundInfosUseCase,
    private val mClearGameRoundsUseCase: ClearGameRoundsUseCase,
    private val mDeleteGameRoundUseCase: DeleteGameRoundUseCase
) {
    @JvmField
    @Inject
    var mPref: AppPrefs? = null
    private var mView: MainMenuView? = null
    var mInfoList: List<GameRound.Info?>? = null
    fun setView(view: MainMenuView?) {
        mView = view
    }

    fun loadData() {
        mCaseExecutor.execute(
            mGameRoundInfosUseCase, object : UseCase.Callback<GetGameRoundInfosUseCase.Result?> {
                override fun onSuccess(result: GetGameRoundInfosUseCase.Result?) {
                    mInfoList = result!!.infoList
                    mView!!.showGameInfoList(result.infoList)
                }

                override fun onFailed(errMsg: String?) {}
            })
    }

    fun clearAll() {
        mCaseExecutor.execute(
            mClearGameRoundsUseCase, object : UseCase.Callback<UseCase.Result?> {
                override fun onSuccess(result: UseCase.Result?) {
//                    mPref!!.resetSaveGameDataCount()
//                    mView!!.clearInfoList()
                }

                override fun onFailed(errMsg: String?) {}
            })
    }

    fun deleteGameRound(info: GameRound.Info) {
        mDeleteGameRoundUseCase.setParams(DeleteGameRoundUseCase.Params(info.id))
        mCaseExecutor.execute(
            mDeleteGameRoundUseCase, object : UseCase.Callback<DeleteGameRoundUseCase.Result?> {
                override fun onSuccess(result: DeleteGameRoundUseCase.Result?) {
                    mView!!.deleteInfo(info)
                }

                override fun onFailed(errMsg: String?) {}
            })
    }

    fun newGameRound(rowCount: Int, colCount: Int) {
        mView!!.setNewGameLoading(true)
        val puzzleName = "Puzzle " + mPref!!.previouslySavedGameDataCount
        mBuildGameRoundUseCase.setParams(
            BuildGameRoundUseCase.Params(
                rowCount,
                colCount,
                puzzleName
            )
        )
        mCaseExecutor.execute(
            mBuildGameRoundUseCase, object : UseCase.Callback<BuildGameRoundUseCase.Result?> {
                override fun onSuccess(result: BuildGameRoundUseCase.Result?) {
                    mPref!!.incrementSavedGameDataCount()
                    mView!!.setNewGameLoading(false)
                    mView!!.showNewlyCreatedGameRound(result!!.gameRound)
                }

                override fun onFailed(errMsg: String?) {
                    mView!!.setNewGameLoading(false)
                }
            })
    }

    fun gameRoundSelected(info: GameRound.Info) {
        mView!!.showGameRound(info.id)
    }
}
