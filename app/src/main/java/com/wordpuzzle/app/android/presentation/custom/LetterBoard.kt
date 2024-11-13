package com.wordpuzzle.app.android.presentation.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.commons.Direction
import com.wordpuzzle.app.android.commons.Direction.Companion.fromLine
import com.wordpuzzle.app.android.commons.GridIndex
import com.wordpuzzle.app.android.commons.Util.getIndexLength
import com.wordpuzzle.app.android.presentation.custom.StreakView.OnInteractionListener
import com.wordpuzzle.app.android.presentation.custom.StreakView.StreakLine
import com.wordpuzzle.app.android.presentation.custom.layout.CenterLayout
import com.wordpuzzle.app.android.presentation.custom.LetterGrid
import com.wordpuzzle.app.android.presentation.custom.LetterGridDataAdapter
import com.wordpuzzle.app.android.presentation.custom.SampleLetterGridDataAdapter
import com.wordpuzzle.app.android.presentation.custom.StreakView
import java.util.Observable
import java.util.Observer

/**
 * Created by abdularis on 26/06/17.
 *
 * Compound view untuk wordsearch game
 * yang memiliki tiga layer yaitu
 * - GridLine sebagai background
 * - StreakView sebagai middleground jadi akan dirender diatas background
 * dan dibawah foreground
 * - LetterGrid sebagai foreground yang menampilkan letters (huruf-huruf)
 * yang akan dirender paling atas
 */
class LetterBoard : CenterLayout, Observer {
    var gridLineBackground: GridLine? = null
        private set
    var streakView: StreakView? = null
        private set
    var letterGrid: LetterGrid? = null
        private set
    private var mData: LetterGridDataAdapter? = null
    private var mInitialized = false
    var selectionListener: OnLetterSelectionListener? = null
        private set

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    override fun update(o: Observable, arg: Any) {
        if (o === mData) {
            /*
                ketika data grid berubah maka update row dan column count
                dari grid line agar memiliki dimensi yang sama
               */
            gridLineBackground!!.colCount = mData!!.colCount
            gridLineBackground!!.rowCount = mData!!.rowCount

            /*
                ketika dimensi row dan column dari data grid berubah
                maka harus di layout/dikalkulasikan kembali ukuran dari streak view
               */streakView!!.invalidate()
            streakView!!.requestLayout()
        }
    }

    fun scale(scaleX: Float, scaleY: Float) {
        if (mInitialized) {
            gridLineBackground!!.gridWidth = (gridLineBackground!!.gridWidth * scaleX).toInt()
            gridLineBackground!!.gridHeight = (gridLineBackground!!.gridHeight * scaleY).toInt()
            gridLineBackground!!.lineWidth = (gridLineBackground!!.lineWidth * scaleX).toInt()
            letterGrid!!.gridWidth = (letterGrid!!.gridWidth * scaleX).toInt()
            letterGrid!!.gridHeight = (letterGrid!!.gridHeight * scaleY).toInt()
            letterGrid!!.letterSize = letterGrid!!.letterSize * scaleY
            streakView!!.streakWidth = (streakView!!.streakWidth * scaleY).toInt()

            // remove all views and re attach them, so this layout get re measure
            removeAllViews()
            attachAllViews()
            streakView!!.invalidateStreakLine()
        }
    }

    val gridColCount: Int
        get() = mData!!.colCount
    val gridRowCount: Int
        get() = mData!!.rowCount
    var dataAdapter: LetterGridDataAdapter?
        get() = mData
        set(dataAdapter) {
            requireNotNull(dataAdapter) { "Data Adapter can't be null" }
            if (dataAdapter !== mData) {
                if (mData != null) mData!!.deleteObserver(this)
                mData = dataAdapter
                mData!!.addObserver(this)
                letterGrid!!.dataAdapter = mData
                gridLineBackground!!.colCount = mData!!.colCount
                gridLineBackground!!.rowCount = mData!!.rowCount
            }
        }

    fun addStreakLines(streakLines: List<StreakLine?>?) {
        streakView!!.addStreakLines(streakLines, false)
    }

    fun addStreakLine(streakLine: StreakLine?) {
        if (streakLine != null) streakView!!.addStreakLine(streakLine, true)
    }

    fun popStreakLine() {
        streakView!!.popStreakLine()
    }

    fun removeAllStreakLine() {
        streakView!!.removeAllStreakLine()
    }

    fun setGridWidth(width: Int) {
        gridLineBackground!!.gridWidth = width
        letterGrid!!.gridWidth = width
    }

    fun setGridHeight(height: Int) {
        gridLineBackground!!.gridHeight = height
        letterGrid!!.gridHeight = height
    }

    fun setGridLineVisibility(visible: Boolean) {
        if (!visible) gridLineBackground!!.visibility =
            INVISIBLE else gridLineBackground!!.visibility = VISIBLE
    }

    fun setGridLineColor(color: Int) {
        gridLineBackground!!.lineColor = color
    }

    fun setGridLineWidth(width: Int) {
        gridLineBackground!!.lineWidth = width
    }

    fun setLetterSize(size: Float) {
        letterGrid!!.letterSize = size
    }

    fun setLetterColor(color: Int) {
        letterGrid!!.letterColor = color
    }

    fun setStreakWidth(width: Int) {
        streakView!!.streakWidth = width
    }

    fun setOnLetterSelectionListener(listener: OnLetterSelectionListener?) {
        selectionListener = listener
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        gridLineBackground = GridLine(context)
        streakView = StreakView(context)
        letterGrid = LetterGrid(context)
        var gridWidth = 50
        var gridHeight = 50
        var gridColCount = 8
        var gridRowCount = 8
        var lineColor = Color.GRAY
        var lineWidth = 2
        var letterSize = 32.0f
        var letterColor = Color.GRAY
        var streakWidth = 35
        var snapToGrid = 0
        var gridLineVisibility = true
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.LetterBoard, 0, 0)
            gridWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_gridWidth, gridWidth)
            gridHeight = a.getDimensionPixelSize(R.styleable.LetterBoard_gridHeight, gridHeight)
            gridColCount = a.getInteger(R.styleable.LetterBoard_gridColumnCount, gridColCount)
            gridRowCount = a.getInteger(R.styleable.LetterBoard_gridRowCount, gridRowCount)
            lineColor = a.getColor(R.styleable.LetterBoard_lineColor, lineColor)
            lineWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_lineWidth, lineWidth)
            letterSize = a.getDimension(R.styleable.LetterBoard_letterSize, letterSize)
            letterColor = a.getColor(R.styleable.LetterBoard_letterColor, letterColor)
            streakWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_streakWidth, streakWidth)
            snapToGrid = a.getInteger(R.styleable.LetterBoard_snapToGrid, snapToGrid)
            gridLineVisibility =
                a.getBoolean(R.styleable.LetterBoard_gridLineVisibility, gridLineVisibility)
            setGridWidth(gridWidth)
            setGridHeight(gridHeight)
            setGridLineColor(lineColor)
            setGridLineWidth(lineWidth)
            setLetterSize(letterSize)
            setLetterColor(letterColor)
            setStreakWidth(streakWidth)
            setGridLineVisibility(gridLineVisibility)
            a.recycle()
        }
        dataAdapter = SampleLetterGridDataAdapter(gridRowCount, gridColCount)
        gridLineBackground!!.colCount = this.gridColCount
        gridLineBackground!!.rowCount = this.gridRowCount
        streakView!!.grid = gridLineBackground
        streakView!!.isInteractive = true
        streakView!!.isRememberStreakLine = true
        streakView!!.isSnapToGrid = StreakView.SnapType.fromId(snapToGrid)
        streakView!!.setOnInteractionListener(StreakViewInteraction())
        attachAllViews()
        mInitialized = true
        scaleX = scaleX
        scaleY = scaleY
    }

    private fun attachAllViews() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(gridLineBackground, layoutParams)
        addView(streakView, layoutParams)
        addView(letterGrid, layoutParams)
    }

    private inner class StreakViewInteraction : OnInteractionListener {
        private fun getStringInRange(start: GridIndex, end: GridIndex): String {
            val dir = fromLine(start, end)
            if (dir == Direction.NONE) return ""
            val count = getIndexLength(start, end)
            val buff = CharArray(count)
            for (i in 0 until count) {
                buff[i] = mData!!.getLetter(start.row + dir.yOff * i, start.col + dir.xOff * i)
            }
            return String(buff)
        }

        override fun onTouchBegin(streakLine: StreakLine?) {
            if (selectionListener != null) {
                val idx = streakLine!!.startIndex
                val str = mData!!.getLetter(idx.row, idx.col).toString()
                selectionListener!!.onSelectionBegin(streakLine, str)
            }
        }

        override fun onTouchDrag(streakLine: StreakLine?) {
            if (selectionListener != null) selectionListener!!.onSelectionDrag(
                streakLine,
                getStringInRange(streakLine!!.startIndex, streakLine.endIndex)
            )
        }

        override fun onTouchEnd(streakLine: StreakLine?) {
            if (selectionListener != null) {
                val str = getStringInRange(streakLine!!.startIndex, streakLine.endIndex)
                selectionListener!!.onSelectionEnd(streakLine, str)
            }
        }
    }

    interface OnLetterSelectionListener {
        fun onSelectionBegin(streakLine: StreakLine?, str: String?)
        fun onSelectionDrag(streakLine: StreakLine?, str: String?)
        fun onSelectionEnd(streakLine: StreakLine?, str: String?)
    }
}
