package com.wordpuzzle.app.android.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.presentation.custom.LetterGridDataAdapter
import com.wordpuzzle.app.android.presentation.custom.SampleLetterGridDataAdapter
import java.util.Observable
import java.util.Observer

/**
 * Created by abdularis on 22/06/17.
 *
 * Render grid of letters
 */
class LetterGrid : GridBehavior, Observer {
    private var mPaint: Paint? = null
    private var mCharBounds: Rect? = null
    private var mData: LetterGridDataAdapter? = null
    private var mTypeface: Typeface? = null  // For custom font

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    var letterSize: Float
        get() = mPaint!!.textSize
        set(letterSize) {
            mPaint!!.textSize = letterSize
            invalidate()
        }
    var letterColor: Int
        get() = mPaint!!.color
        set(color) {
            mPaint!!.color = color
            invalidate()
        }
    var dataAdapter: LetterGridDataAdapter?
        get() = mData
        set(data) {
            requireNotNull(data) { "Data Adapater can't be null" }
            if (data !== mData) {
                if (mData != null) mData!!.deleteObserver(this)
                mData = data
                mData!!.addObserver(this)
                invalidate()
                requestLayout()
            }
        }
    override var colCount: Int
        get() = mData!!.colCount
        set(colCount) {
            // do nothing
        }
    override var rowCount: Int
        get() = mData!!.rowCount
        set(rowCount) {
            // do nothing
        }

    override fun update(o: Observable, arg: Any) {
        invalidate()
        requestLayout()
    }

    override fun onDraw(canvas: Canvas) {
        val gridColCount = colCount
        val gridRowCount = rowCount
        val halfWidth = gridWidth / 2
        val halfHeight = gridHeight / 2
        var x: Int
        var y = halfHeight + paddingTop

        // iterate and render all letters found in grid data adapter
        for (i in 0 until gridRowCount) {
            x = halfWidth + paddingLeft
            for (j in 0 until gridColCount) {
                val letter = mData!!.getLetter(i, j)
                mPaint!!.getTextBounds(letter.toString(), 0, 1, mCharBounds)
                canvas.drawText(
                    letter.toString(),
                    x - mCharBounds!!.exactCenterX(), y - mCharBounds!!.exactCenterY(), mPaint!!
                )
                x += gridWidth
            }
            y += gridHeight
        }
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.textSize = 32.0f
        mCharBounds = Rect()

        // Load custom font from assets
        mTypeface = ResourcesCompat.getFont(context, R.font.akaya_kanadaka_regular)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LetterGrid, 0, 0)
            mPaint!!.textSize = a.getDimension(R.styleable.LetterGrid_letterSize, mPaint!!.textSize)
            mPaint!!.color = a.getColor(R.styleable.LetterGrid_letterColor, Color.GRAY)
            a.recycle()
        }

        // Set custom typeface to paint
        mPaint!!.typeface = mTypeface

        dataAdapter = SampleLetterGridDataAdapter(8, 8)
    }
}
