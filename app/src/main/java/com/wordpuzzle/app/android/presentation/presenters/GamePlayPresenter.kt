package com.wordpuzzle.app.android.presentation.presenters

import android.os.Handler
import com.wordpuzzle.app.android.commons.Timer
import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.domain.usecases.AnswerWordUseCase
import com.wordpuzzle.app.android.domain.usecases.GetGameRoundUseCase
import com.wordpuzzle.app.android.domain.usecases.SaveDurationUseCase
import com.wordpuzzle.app.android.domain.usecases.UseCase
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import com.wordpuzzle.app.android.presentation.custom.StreakView
import com.wordpuzzle.app.android.presentation.model.mapper.StreakLineMapper
import com.wordpuzzle.app.android.presentation.model.mapper.UsedWordMapper
import com.wordpuzzle.app.android.presentation.views.GamePlayView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GamePlayPresenter @Inject constructor(
    private val caseExecutor: UseCaseExecutor,
    private val getGameRoundUseCase: GetGameRoundUseCase,
    private val answerWordUseCase: AnswerWordUseCase,
    private val saveDurationUseCase: SaveDurationUseCase
) {
    private val STREAK_LINE_MAPPER = StreakLineMapper()

    private var mView: GamePlayView? = null
    private var mCurrGameRound: Int = 0
    private var mCurrUsedWord: List<UsedWord>? = null
    private var mGenerating: Boolean = false
    private var mAlreadyFinished: Boolean = false
    private var mGameLoaded: Boolean = false
    private var mAnsweredWordsCount: Int = 0
    private var mCurrDuration: Int = 0
    private var mCurrDuration1: Long = 0
    private val mTimer = Timer(1000)

    init {
        mTimer.addOnTimeoutListener(object : Timer.OnTimeoutListener {
            override fun onTimeout(ellapsedTime: Long) {
                mCurrDuration1 = ellapsedTime
                mView?.showDuration(++mCurrDuration)
                mView?.showDurationMillisecond(formatDuration(mCurrDuration1))
                saveDurationUseCase.params = SaveDurationUseCase.Params(mCurrGameRound, mCurrDuration)
                caseExecutor.execute(saveDurationUseCase, object : UseCase.Callback<SaveDurationUseCase.Result?> {
                    override fun onSuccess(result: SaveDurationUseCase.Result?) {}

                    override fun onFailed(errMsg: String?) {}
                })
            }
        })
    }

    fun setView(view: GamePlayView) {
        mView = view
    }

    fun stopGame() {
        mTimer.stop()
    }

    fun resumeGame() {
        if (!mAlreadyFinished && mGameLoaded) {
            mTimer.start()
        }
    }

    fun loadGameRound(gid: Int) {
        if (mGenerating) return

        mGenerating = true
        mCurrGameRound = gid
        mView?.showLoading(true)
        getGameRoundUseCase.params = GetGameRoundUseCase.Params(gid)
        caseExecutor.execute(getGameRoundUseCase, object : UseCase.Callback<GetGameRoundUseCase.Result?> {
            override fun onSuccess(result: GetGameRoundUseCase.Result?) {
                mView?.apply {
                    showLetterGrid(result!!.gameRound!!.grid!!.array)
                    showDuration(result.gameRound!!.info.duration)
//                    showDurationMillisecond(formatDuration(result.gameRound!!.info.duration.toLong()))
                    showUsedWords(UsedWordMapper().map(result.gameRound!!.usedWords))
                    showWordsCount(result.gameRound!!.usedWords.size)
                    mAnsweredWordsCount = result.gameRound!!.answeredWordsCount
                    showAnsweredWordsCount(mAnsweredWordsCount)
                }

                mCurrUsedWord = result!!.gameRound!!.usedWords
                mCurrDuration = result.gameRound!!.info.duration
                mGenerating = false
                mGameLoaded = true
                mView?.apply {
                    showLoading(false)
                    doneLoadingContent()
                }

                if (mAnsweredWordsCount >= mCurrUsedWord!!.size) {
                    mView?.setGameAsAlreadyFinished()
                    mAlreadyFinished = true
                } else {
                    mAlreadyFinished = false
                    resumeGame()
                }
            }

            override fun onFailed(errMsg: String?) {
                mView?.showLoading(false)
                mGenerating = false
            }
        })
    }

    fun answerWord(str: String, streakLine: StreakView.StreakLine, reverseMatching: Boolean) {
        answerWordUseCase.params = AnswerWordUseCase.Params(
            str,
            STREAK_LINE_MAPPER.revMap(streakLine),
            mCurrUsedWord!!,
            reverseMatching
        )

        caseExecutor.execute(answerWordUseCase, object : UseCase.Callback<AnswerWordUseCase.Result?> {
            override fun onSuccess(result: AnswerWordUseCase.Result?) {
                mView?.apply {
                    if (result!!.mCorrect && result.mUsedWord != null) {
                        mAnsweredWordsCount++
                        showAnsweredWordsCount(mAnsweredWordsCount)
                        wordAnswered(true, result.mUsedWord!!.id)
                    } else {
                        wordAnswered(false, -1)
                    }

                    if (mAnsweredWordsCount >= mCurrUsedWord!!.size) {
                        Handler().postDelayed({
                            mView?.showFinishGame()
                        }, 800)
                    }
                }
            }

            override fun onFailed(errMsg: String?) {}
        })
    }

    private fun formatDuration(durationMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60
        val milliseconds = durationMillis % 1000
        return String.format("%02d:%02d.%06d", minutes, seconds, milliseconds)
    }
}
