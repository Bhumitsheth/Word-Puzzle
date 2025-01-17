package com.wordpuzzle.app.android.commons.generator

import com.wordpuzzle.app.android.domain.model.Grid

/**
 * Created by abdularis on 06/07/17.
 *
 * Parse dataInput kedalam array grid[][]
 */
class StringGridGenerator : GridGenerator<String, Boolean>() {

    override fun setGrid(dataInput: String, grid: Array<CharArray?>?): Boolean {
        if (dataInput == null || grid == null) return false

        var input = dataInput.trim()

        var row = 0
        var col = 0

        for (i in input.indices) {
            val c = input[i]

            if (c == Grid.GRID_NEWLINE_SEPARATOR) {
                row++
                col = 0
            } else {
                if (row >= grid.size || col >= grid[0]!!.size) {
                    resetGrid(grid)
                    return false
                }

                grid[row]!![col] = c

                col++
            }
        }

        return true
    }
}
