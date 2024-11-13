package com.wordpuzzle.app.android.domain.model

/**
 * Created by abdularis on 08/07/17.
 */
class UsedWord {
    var id: Int
    var string = ""
    var answerLine: AnswerLine? = null
    var isAnswered = false
    var isMystery = false
    var revealCount = 0

    init {
        id = -1
    }

    class AnswerLine @JvmOverloads constructor(
        @JvmField var startRow: Int = 0,
        @JvmField var startCol: Int = 0,
        @JvmField var endRow: Int = 0,
        @JvmField var endCol: Int = 0,
        @JvmField var color: Int = 0
    ) {
        override fun toString(): String {
            return "$startRow,$startCol:$endRow,$endCol"
        }

        fun fromString(string: String?) {
            /*
                Expected format string = 1,1:6,6
             */
            if (string == null) return
            val split = string.split(":".toRegex(), limit = 2).toTypedArray()
            if (split.size >= 2) {
                val start = split[0].split(",".toRegex(), limit = 2).toTypedArray()
                val end = split[1].split(",".toRegex(), limit = 2).toTypedArray()
                if (start.size >= 2 && end.size >= 2) {
                    startRow = start[0].toInt()
                    startCol = start[1].toInt()
                    endRow = end[0].toInt()
                    endCol = end[1].toInt()
                }
            }
        }
    }
}
