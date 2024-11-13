package com.wordpuzzle.app.android.domain.model

/**
 * Created by abdularis on 08/07/17.
 */
class Grid {
    var array: Array<CharArray?>?
        private set

    constructor(grid: Array<CharArray?>?) {
        array = grid
    }

    constructor(rowCount: Int, colCount: Int) {
        require(!(rowCount <= 0 || colCount <= 0)) { "Row and column should be greater than 0" }
        array = Array(rowCount) { CharArray(colCount) }
        reset()
    }

    val rowCount: Int
        get() = array!!.size
    val colCount: Int
        get() = array!![0]!!.size

    fun at(row: Int, col: Int): Char {
        return array!![row]!![col]
    }

    fun setAt(row: Int, col: Int, c: Char) {
        array!![row]!![col] = c
    }

    fun reset() {
        for (i in array!!.indices) {
            for (j in array!![i]!!.indices) {
                array!![i]!![j] = NULL_CHAR
            }
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until rowCount) {
            for (j in 0 until colCount) {
                stringBuilder.append(at(i, j))
            }
            if (i != rowCount - 1) stringBuilder.append(GRID_NEWLINE_SEPARATOR)
        }
        return stringBuilder.toString()
    }

    companion object {
        const val GRID_NEWLINE_SEPARATOR = ','
        const val NULL_CHAR = '\u0000'
    }
}
