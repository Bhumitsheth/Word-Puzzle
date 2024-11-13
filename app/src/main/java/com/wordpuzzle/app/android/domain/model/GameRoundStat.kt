package com.wordpuzzle.app.android.domain.model

/**
 * Created by abdularis on 24/07/17.
 */
class GameRoundStat @JvmOverloads constructor(
    var name: String = "", var duration: Int = 0, var usedWordCount: Int = 0,
    var gridRowCount: Int = 0, var gridColCount: Int = 0
)
