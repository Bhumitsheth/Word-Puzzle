package com.wordpuzzle.app.android.presentation.custom


/**
 * Created by abdularis on 28/06/17.
 *
 * Sample data adapter (for preview in android studio visual editor)
 */
internal class SampleLetterGridDataAdapter(private val mRowCount: Int, private val mColCount: Int) :
    LetterGridDataAdapter() {
//    override fun getColCount(): Int {
//        return mColCount
//    }
//
//    override fun getRowCount(): Int {
//        return mRowCount
//    }

    override val colCount: Int
        get() = mColCount
    override val rowCount: Int
        get() = mRowCount

    override fun getLetter(row: Int, col: Int): Char {
        return 'A'
    }
}
