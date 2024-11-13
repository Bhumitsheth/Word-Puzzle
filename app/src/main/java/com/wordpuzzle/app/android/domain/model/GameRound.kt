package com.wordpuzzle.app.android.domain.model

/**
 * Created by abdularis on 08/07/17.
 */
class GameRound @JvmOverloads constructor(
    var info: Info = Info(),
    var grid: Grid? = null,
    private val mUsedWords: MutableList<UsedWord> = ArrayList()
) {
    val usedWords: List<UsedWord>
        get() = mUsedWords
    val answeredWordsCount: Int
        get() {
            var count = 0
            for (uw in mUsedWords) {
                if (uw.isAnswered) {
                    count++
                }
            }
            return count
        }

    fun addUsedWord(usedWord: UsedWord) {
        mUsedWords.add(usedWord)
    }

    fun addUsedWords(usedWords: List<UsedWord>?) {
        mUsedWords.addAll(usedWords!!)
    }

    class Info @JvmOverloads constructor(
        var id: Int = -1,
        var name: String = "",
        var duration: Int = 0
    )
}
