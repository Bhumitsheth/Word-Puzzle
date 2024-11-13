package com.wordpuzzle.app.android.main.ui.play_game.adapter

import com.wordpuzzle.app.android.presentation.custom.LetterGridDataAdapter

/**
 * Created by abdularis on 09/07/17.
 */
class ArrayLetterGridDataAdapter(private var mGrid: Array<CharArray?>?) : LetterGridDataAdapter() {
    var grid: Array<CharArray?>?
        get() = mGrid
        set(grid) {
            if (grid != null && grid != mGrid) {
                mGrid = grid
                setChanged()
                notifyObservers()
            }
        }

    override val colCount: Int
        get() = mGrid!![0]!!.size

    override val rowCount: Int
        get() = mGrid!!.size

    override fun getLetter(row: Int, col: Int): Char {
        return mGrid!![row]!![col]
    }
}
