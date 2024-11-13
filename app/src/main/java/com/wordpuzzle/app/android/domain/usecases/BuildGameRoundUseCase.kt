package com.wordpuzzle.app.android.domain.usecases

import com.wordpuzzle.app.android.commons.Util.getRandomIntRange
import com.wordpuzzle.app.android.commons.Util.randomizeList
import com.wordpuzzle.app.android.commons.generator.StringListGridGenerator
import com.wordpuzzle.app.android.domain.data.mapper.GameRoundMapper
import com.wordpuzzle.app.android.domain.data.source.GameRoundDataSource
import com.wordpuzzle.app.android.domain.data.source.WordDataSource
import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.domain.model.Grid
import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.domain.model.Word
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Created by abdularis on 20/07/17.
 */
class BuildGameRoundUseCase @Inject constructor(
    private val mGameRoundDataSource: GameRoundDataSource,
    private val mWordDataSource: WordDataSource
) : UseCase<BuildGameRoundUseCase.Params?, BuildGameRoundUseCase.Result?>() {
    override fun execute(params: Params?) {
        val gameRound = createNewGameRound(params!!.rowCount, params.colCount, params.name)
        callback!!.onSuccess(Result(gameRound))
    }

    private fun createNewGameRound(rowCount: Int, colCount: Int, name: String?): GameRound {
        val gameRound = GameRound()
        mWordDataSource.getWords(object : WordDataSource.Callback {
            override fun onWordsLoaded(words: MutableList<Word>) {
//                val wordList: MutableList<Word> = ArrayList()
//                wordList.add(Word(1, "SWIFT"))
//                wordList.add(Word(2, "KOTLIN"))
//                wordList.add(Word(3, "PYTHON"))
//                wordList.add(Word(4, "ANDROID"))
//                wordList.add(Word(5, "PUZZLE"))
//                wordList.add(Word(6, "HEIGHT"))
//                wordList.add(Word(7, "GLOBAL"))
//                randomizeList<Word?>(words)
//                randomizeList(wordList)
                randomizeList(words)
                val grid = Grid(rowCount, colCount)
                val maxCharCount = Math.min(rowCount, colCount)
                //                List<String> usedStrings = new StringListGridGenerator().setGrid(getStringListFromWord(words, 100, maxCharCount), grid.getArray());
                val usedStrings = StringListGridGenerator().setGrid(
                    getStringListFromWord(
                        words,
                        words.size,
                        maxCharCount
                    ), grid.array
                )
                gameRound.addUsedWords(buildUsedWordFromString(usedStrings))
                gameRound.grid = grid
                if (name == null || name.isEmpty()) {
                    val name = "Puzzle " + SimpleDateFormat("HH.mm.ss").format(Date(System.currentTimeMillis()))
                    gameRound.info.name = name
                } else {
                    gameRound.info.name = name
                }
                mGameRoundDataSource.saveGameRound(GameRoundMapper().revMap(gameRound))
            }
        })
        return gameRound
    }

    private fun buildUsedWordFromString(strings: List<String>): List<UsedWord> {
        var mysteryWordCount = getRandomIntRange(strings.size / 2, strings.size)
        val usedWords: MutableList<UsedWord> = ArrayList()
        for (i in strings.indices) {
            val str = strings[i]
            val uw = UsedWord()
            uw.string = str
            uw.isAnswered = false
            if (mysteryWordCount > 0) {
                uw.isMystery = true
                uw.revealCount = getRandomIntRange(0, str.length - 1)
                mysteryWordCount--
            }
            usedWords.add(uw)
        }
        randomizeList(usedWords)
        return usedWords
    }

    private fun getStringListFromWord(
        words: List<Word>,
        count: Int,
        maxCharCount: Int
    ): List<String?> {
        var count = count
        count = Math.min(count, words.size)
        val stringList: MutableList<String?> = ArrayList()
        var temp: String
        for (i in words.indices) {
//            if (stringList.size() >= count) break;
            temp = words[i].string
            if (temp.length <= maxCharCount) {
                stringList.add(temp)
            }
        }
        return stringList
    }

    class Params(var rowCount: Int, var colCount: Int, var name: String) : UseCase.Params
    class Result(@JvmField var gameRound: GameRound) : UseCase.Result
}
