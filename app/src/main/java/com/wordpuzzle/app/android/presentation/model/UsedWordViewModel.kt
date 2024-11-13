package com.wordpuzzle.app.android.presentation.model

import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.presentation.custom.StreakView.StreakLine
import com.wordpuzzle.app.android.presentation.model.mapper.StreakLineMapper

/**
 * Created by abdularis on 18/07/17.
 */
class UsedWordViewModel(val usedWord: UsedWord) {

    val streakLine: StreakLine
        get() = sStreakLineMapper.map(usedWord.answerLine)
    var id: Int
        get() = usedWord.id
        set(id) {
            usedWord.id = id
        }
    var string: String?
        get() = usedWord.string
        set(string) {
            usedWord.string = string!!
        }
    val isAnswered: Boolean
        get() = usedWord.isAnswered
    val isMystery: Boolean
        get() = usedWord.isMystery

    companion object {
        private val sStreakLineMapper = StreakLineMapper()
    }
}
