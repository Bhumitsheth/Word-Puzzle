package com.wordpuzzle.app.android.commons

/**
 * Created by abdularis on 29/06/17.
 */
class GridIndex @JvmOverloads constructor(row: Int = 0, col: Int = 0) {
    @JvmField
    var row = 0
    @JvmField
    var col = 0

    init {
        set(row, col)
    }

    operator fun set(row: Int, col: Int) {
        this.row = row
        this.col = col
    }
}
