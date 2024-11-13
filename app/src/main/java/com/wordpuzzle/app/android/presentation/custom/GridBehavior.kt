package com.wordpuzzle.app.android.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.wordpuzzle.app.android.R

/**
 * Created by abdularis on 22/06/17.
 *
 * Base class untuk semua class yang memiliki karakteristik seperti grid
 */
abstract class GridBehavior : View {
    private var mGridWidth = 0
    private var mGridHeight = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    var gridWidth: Int
        get() = (mGridWidth * scaleX).toInt()
        set(gridWidth) {
            mGridWidth = gridWidth
            invalidate()
        }
    var gridHeight: Int
        get() = (mGridHeight * scaleY).toInt()
        set(gridHeight) {
            mGridHeight = gridHeight
            invalidate()
        }
    abstract var colCount: Int
    abstract var rowCount: Int
    open val requiredWidth: Int
        /**
         * Return lebar minimum yang dibutuhkan, yg didapatkan dari jumlah dan lebar grid
         *
         * @return lebar grid view yang dibutuhkan
         */
        get() = paddingLeft + paddingRight + colCount * gridWidth
    open val requiredHeight: Int
        /**
         * Return tinggi minimum yang dibutuhkan, yg didapatkan dari jumlah dan tinggi grid
         *
         * @return tinggi grid view yng dibutuhkan
         */
        get() = paddingTop + paddingBottom + rowCount * gridHeight

    /**
     * Return column index grid pada posisi layar tertentu
     *
     * @param screenPos posisi pada layar relative terhadap view ini.
     * @return index column, dimana column >= 0 dan column < jumlah horizontal grid - 1
     */
    fun getColIndex(screenPos: Int): Int {
        return Math.max(Math.min((screenPos - paddingLeft) / gridWidth, colCount - 1), 0)
    }

    /**
     * Return row index grid pada posisi layar tertentu
     *
     * @param screenPos posisi pada layar relative terhadap view ini.
     * @return index row, dimana row >= 0 dan row < jumlah vertical grid - 1
     */
    fun getRowIndex(screenPos: Int): Int {
        return Math.max(Math.min((screenPos - paddingTop) / gridHeight, rowCount - 1), 0)
    }

    open fun getCenterColFromIndex(cIdx: Int): Int {
        return Math.min(Math.max(0, cIdx), colCount - 1) * gridWidth + gridWidth / 2 + paddingLeft
    }

    open fun getCenterRowFromIndex(rIdx: Int): Int {
        return Math.min(Math.max(0, rIdx), rowCount - 1) * gridHeight + gridHeight / 2 + paddingTop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var measuredWidth = requiredWidth
        var measuredHeight = requiredHeight
        if (widthMode == MeasureSpec.EXACTLY) measuredWidth =
            width else if (widthMeasureSpec == MeasureSpec.AT_MOST) measuredWidth =
            Math.min(measuredWidth, width)
        if (heightMode == MeasureSpec.EXACTLY) measuredHeight =
            height else if (heightMode == MeasureSpec.AT_MOST) measuredHeight =
            Math.min(measuredHeight, height)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mGridWidth = 50
        mGridHeight = 50
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.GridBehavior, 0, 0)
            mGridWidth = a.getDimensionPixelSize(R.styleable.GridBehavior_gridWidth, mGridWidth)
            mGridHeight = a.getDimensionPixelSize(R.styleable.GridBehavior_gridHeight, mGridHeight)
            a.recycle()
        }
    }
}
