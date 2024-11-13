package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.commons.Util.getReverseString
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.domain.model.UsedWord.AnswerLine
import javax.inject.Inject

/**
 * Created by abdularis on 18/07/17.
 */
class AnswerWordUseCase @Inject constructor(private val mDataSource: GameRoundDataSource) :
    UseCase<AnswerWordUseCase.Params?, AnswerWordUseCase.Result?>() {
    override fun execute(params: Params?) {
        var correct = false
        var correctWord: UsedWord? = null
        val answerStr = params!!.mString
        val answerStrRev = getReverseString(answerStr)
        for (word in params.mUsedWords) {
            if (word.isAnswered) continue
            val wordStr = word.string
            if (wordStr.equals(answerStr, ignoreCase = true) || wordStr.equals(
                    answerStrRev,
                    ignoreCase = true
                ) && params.reverseMatching
            ) {
                correct = true
                correctWord = word
                correctWord.isAnswered = true
                correctWord.answerLine = params.mLine
                break
            }
        }
        if (correct) {
            mDataSource.markWordAsAnswered(correctWord)
        }
        callback!!.onSuccess(Result(correct, correctWord))
    }

    class Params(
        var mString: String,
        var mLine: AnswerLine,
        var mUsedWords: List<UsedWord>,
        var reverseMatching: Boolean
    ) : UseCase.Params

    class Result(@JvmField var mCorrect: Boolean, @JvmField var mUsedWord: UsedWord?) : UseCase.Result
}
