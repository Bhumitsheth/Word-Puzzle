package com.wordpuzzle.app.android.data.sqlite

import android.provider.BaseColumns

/**
 * Created by abdularis on 18/07/17.
 */
internal abstract class DbContract {
    internal object WordBank : BaseColumns {
        const val TABLE_NAME = "word_bank"
        const val _ID = "_id"
        const val COL_STRING = "string"
    }

    internal object GameRound : BaseColumns {
        const val TABLE_NAME = "game_round"
        const val _ID = "_id"
        const val COL_NAME = "name"
        const val COL_DURATION = "duration"
        const val COL_GRID_ROW_COUNT = "grid_row_count"
        const val COL_GRID_COL_COUNT = "grid_col_count"
        const val COL_GRID_DATA = "grid_data"
    }

    internal object UsedWord : BaseColumns {
        const val TABLE_NAME = "used_words"
        const val _ID = "_id"
        const val COL_GAME_ROUND_ID = "game_round_id"
        const val COL_WORD_STRING = "word_id"
        const val COL_ANSWER_LINE_DATA = "answer_line_data"
        const val COL_LINE_COLOR = "line_color"
        const val COL_IS_MYSTERY = "mystery"
        const val COL_REVEAL_COUNT = "reveal_count"
    }
}
