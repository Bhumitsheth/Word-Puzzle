package com.wordpuzzle.app.android.presentation.model.mapper

import com.wordpuzzle.app.android.commons.Mapper
import com.wordpuzzle.app.android.domain.model.UsedWord.AnswerLine
import com.wordpuzzle.app.android.presentation.custom.StreakView.StreakLine

/**
 * Created by abdularis on 09/07/17.
 */
class StreakLineMapper : Mapper<AnswerLine?, StreakLine?>() {
    override fun map(obj: AnswerLine?): StreakLine {
        val s = StreakLine()
        s.startIndex[obj!!.startRow] = obj.startCol
        s.endIndex[obj.endRow] = obj.endCol
        s.color = obj.color
        return s
    }

    override fun revMap(obj: StreakLine?): AnswerLine {
        val a = AnswerLine()
        a.startRow = obj!!.startIndex.row
        a.startCol = obj.startIndex.col
        a.endRow = obj.endIndex.row
        a.endCol = obj.endIndex.col
        a.color = obj.color
        return a
    }
}
