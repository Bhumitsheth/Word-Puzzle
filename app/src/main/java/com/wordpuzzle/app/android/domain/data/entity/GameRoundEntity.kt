package com.wordpuzzle.app.android.domain.data.entity

import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.domain.model.UsedWord

/**
 * Created by abdularis on 08/07/17.
 */
class GameRoundEntity {
    var info: GameRound.Info? = null
    var gridRowCount = 0
    var gridColCount = 0
    var gridData: String? = null
    var usedWords: List<UsedWord>? = null
}
