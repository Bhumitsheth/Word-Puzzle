package com.wordpuzzle.app.android.presentation.views

import com.wordpuzzle.app.android.presentation.model.UsedWordViewModel

/**
 * Created by abdularis on 18/07/17.
 */
interface GamePlayView {
    fun doneLoadingContent()
    fun showLoading(enable: Boolean)
    fun showLetterGrid(grid: Array<CharArray?>?)
    fun showDuration(duration: Int)
    fun showDurationMillisecond(duration: String)
    fun showUsedWords(usedWords: List<UsedWordViewModel?>?)
    fun showAnsweredWordsCount(count: Int)
    fun showWordsCount(count: Int)
    fun showFinishGame()
    fun setGameAsAlreadyFinished()
    fun wordAnswered(correct: Boolean, usedWordId: Int)
}
