package com.wordpuzzle.app.android.presentation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.commons.GridIndex
import com.wordpuzzle.app.android.commons.math.Vec2
import com.wordpuzzle.app.android.commons.math.Vec2.Companion.normalize
import com.wordpuzzle.app.android.commons.math.Vec2.Companion.sub
import com.wordpuzzle.app.android.presentation.custom.TouchProcessor
import com.wordpuzzle.app.android.presentation.custom.TouchProcessor.OnTouchProcessed
import java.util.Stack

/**
 * Created by abdularis on 20/06/17.
 *
 * Garis yang bisa didrag (coretan didalam word search game)
 */
class StreakView : View {
    enum class SnapType(var id: Int) {
        NONE(0),
        START_END(1),
        ALWAYS_SNAP(2);

        companion object {
            fun fromId(id: Int): SnapType {
                for (t in entries) {
                    if (t.id == id) return t
                }
                throw IllegalArgumentException()
            }
        }
    }

    private var mRect: RectF? = null
    private var mWidth = 0
    private var mPaint: Paint? = null
    private var mGridId = 0
    var grid: GridBehavior? = null
    private var mSnapToGrid: SnapType? = null
    private var mTouchProcessor: TouchProcessor? = null
    var isInteractive = false
    var isRememberStreakLine = false
    private var mLines: Stack<StreakLine>? = null
    private var mInteractionListener: OnInteractionListener? = null
    private var mEnableOverrideStreakLineColor = false
    private var mOverrideStreakLineColor = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    var streakWidth: Int
        get() = mWidth
        set(width) {
            mWidth = width
            invalidate()
        }
    var isSnapToGrid: SnapType?
        get() = mSnapToGrid
        set(snapToGrid) {
            check(!(mSnapToGrid != snapToGrid && mGridId == -1 && grid == null)) { "setGrid() first to set the grid object!" }
            mSnapToGrid = snapToGrid
        }

    fun setEnableOverrideStreakLineColor(enableOverrideStreakLineColor: Boolean) {
        mEnableOverrideStreakLineColor = enableOverrideStreakLineColor
    }

    fun setOverrideStreakLineColor(overrideStreakLineColor: Int) {
        mOverrideStreakLineColor = overrideStreakLineColor
    }

    fun setOnInteractionListener(listener: OnInteractionListener?) {
        mInteractionListener = listener
    }

    private fun pushStreakLine(streakLine: StreakLine, snapToGrid: Boolean) {
        mLines!!.push(streakLine)
        if (grid != null) {
            streakLine.start.x = grid!!.getCenterColFromIndex(streakLine.startIndex.col).toFloat()
            streakLine.start.y = grid!!.getCenterRowFromIndex(streakLine.startIndex.row).toFloat()
            streakLine.end.x = grid!!.getCenterColFromIndex(streakLine.endIndex.col).toFloat()
            streakLine.end.y = grid!!.getCenterRowFromIndex(streakLine.endIndex.row).toFloat()
        }
    }

    fun invalidateStreakLine() {
        for (streakLine in mLines!!) {
            streakLine.start.x = grid!!.getCenterColFromIndex(streakLine.startIndex.col).toFloat()
            streakLine.start.y = grid!!.getCenterRowFromIndex(streakLine.startIndex.row).toFloat()
            streakLine.end.x = grid!!.getCenterColFromIndex(streakLine.endIndex.col).toFloat()
            streakLine.end.y = grid!!.getCenterRowFromIndex(streakLine.endIndex.row).toFloat()
        }
    }

    fun addStreakLines(streakLines: List<StreakLine?>?, snapToGrid: Boolean) {
        for (line in streakLines!!) pushStreakLine(line!!, snapToGrid)
        invalidate()
    }

    fun addStreakLine(streakLine: StreakLine, snapToGrid: Boolean) {
        pushStreakLine(streakLine, snapToGrid)
        invalidate()
    }

    fun popStreakLine() {
        if (!mLines!!.isEmpty()) {
            mLines!!.pop()
            invalidate()
        }
    }

    fun removeAllStreakLine() {
        mLines!!.clear()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isInteractive) mTouchProcessor!!.onTouchEvent(event) else super.onTouchEvent(
            event
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mGridId != -1 && mSnapToGrid != SnapType.NONE) {
            grid = rootView.findViewById<View>(mGridId) as GridBehavior
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var measuredHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mSnapToGrid != SnapType.NONE && grid != null) {
            measuredWidth = grid!!.requiredWidth
            measuredHeight = grid!!.requiredHeight
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        for (line in mLines!!) {
            val v = sub(line.end, line.start)
            val length = v.length()
            var rot = Math.toDegrees(getRotation(v, Vec2.Right).toDouble())
            if (v.y < 0) rot = -rot
            canvas.save()
            if (!java.lang.Double.isNaN(rot)) canvas.rotate(
                rot.toFloat(),
                line.start.x,
                line.start.y
            )
            // Original Code
//             int halfWidth = mWidth / 2;
            //Update the code
            val halfWidth = mWidth
            if (mEnableOverrideStreakLineColor) {
                mPaint!!.color = mOverrideStreakLineColor
            } else {
                mPaint!!.color = line.color
            }
            mRect!![line.start.x - halfWidth, line.start.y - halfWidth, line.start.x + length + halfWidth] =
                line.start.y + halfWidth
            canvas.drawRoundRect(mRect!!, halfWidth.toFloat(), halfWidth.toFloat(), mPaint!!)
            canvas.restore()
        }
    }

    private fun getRotation(p1: Vec2, p2: Vec2): Float {
        val dot = normalize(p1).dot(normalize(p2))
        return Math.acos(dot.toDouble()).toFloat()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = Color.GREEN
        mWidth = 26
        mGridId = -1
        grid = null
        mSnapToGrid = SnapType.NONE
        isRememberStreakLine = false
        isInteractive = false
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.StreakView, 0, 0)
            mPaint!!.color = a.getInteger(R.styleable.StreakView_streakColor, mPaint!!.color)
            mWidth = a.getDimensionPixelSize(R.styleable.StreakView_streakWidth, mWidth)
            mGridId = a.getResourceId(R.styleable.StreakView_strekGrid, mGridId)
            mSnapToGrid = SnapType.fromId(a.getInt(R.styleable.StreakView_snapToGrid, 0))
            isInteractive = a.getBoolean(R.styleable.StreakView_interactive, isInteractive)
            isRememberStreakLine =
                a.getBoolean(R.styleable.StreakView_rememberStreakLine, isRememberStreakLine)
            a.recycle()
        }
        isSnapToGrid = mSnapToGrid
        mTouchProcessor = TouchProcessor(OnTouchProcessedListener(), 3.0f)
        mLines = Stack()
        mRect = RectF()
    }

    private inner class OnTouchProcessedListener : OnTouchProcessed {
        override fun onDown(event: MotionEvent?) {
            if (!isRememberStreakLine) {
                if (mLines!!.isEmpty()) mLines!!.push(StreakLine())
            } else {
                mLines!!.push(StreakLine())
            }
            val line = mLines!!.peek()
            val colIdx = grid!!.getColIndex(event!!.x.toInt())
            val rowIdx = grid!!.getRowIndex(event.y.toInt())
            line.startIndex[rowIdx] = colIdx
            if (mSnapToGrid != SnapType.NONE && grid != null) {
                line.start[grid!!.getCenterColFromIndex(colIdx).toFloat()] =
                    grid!!.getCenterRowFromIndex(rowIdx).toFloat()
                line.end[line.start.x] = line.start.y
            } else {
                line.start[event.x] = event.y
                line.end[event.x] = event.y
            }
            if (mInteractionListener != null) mInteractionListener!!.onTouchBegin(line)
            invalidate()
        }

        override fun onUp(event: MotionEvent?) {
            if (mLines!!.isEmpty()) return
            val line = mLines!!.peek()
            val colIdx = grid!!.getColIndex(event!!.x.toInt())
            val rowIdx = grid!!.getRowIndex(event.y.toInt())
            line.endIndex[rowIdx] = colIdx
            if (mSnapToGrid != SnapType.NONE && grid != null) line.end[grid!!.getCenterColFromIndex(
                colIdx
            ).toFloat()] = grid!!.getCenterRowFromIndex(rowIdx).toFloat() else line.end[event.x] =
                event.y
            if (mInteractionListener != null) mInteractionListener!!.onTouchEnd(line)
            invalidate()
        }

        override fun onMove(event: MotionEvent?) {
            if (mLines!!.isEmpty()) return
            val line = mLines!!.peek()
            val colIdx = grid!!.getColIndex(event!!.x.toInt())
            val rowIdx = grid!!.getRowIndex(event.y.toInt())
            line.endIndex[rowIdx] = colIdx
            if (mSnapToGrid == SnapType.ALWAYS_SNAP && grid != null) {
                line.end[grid!!.getCenterColFromIndex(colIdx).toFloat()] =
                    grid!!.getCenterRowFromIndex(rowIdx).toFloat()
            } else {
                val halfWidth = mWidth / 2
                val x =
                    Math.max(Math.min(event.x, (width - halfWidth).toFloat()), halfWidth.toFloat())
                val y =
                    Math.max(Math.min(event.y, (height - halfWidth).toFloat()), halfWidth.toFloat())
                line.end[x] = y
            }
            if (mInteractionListener != null) mInteractionListener!!.onTouchDrag(line)
            invalidate()
        }
    }

    //
    interface OnInteractionListener {
        fun onTouchBegin(streakLine: StreakLine?)
        fun onTouchDrag(streakLine: StreakLine?)
        fun onTouchEnd(streakLine: StreakLine?)
    }

    class StreakLine {
        var start: Vec2
        var end: Vec2
        var startIndex: GridIndex
        var endIndex: GridIndex
        @JvmField
        var color = Color.RED

        init {
            start = Vec2()
            end = Vec2()
            startIndex = GridIndex(-1, -1)
            endIndex = GridIndex(-1, -1)
        }
    }

    companion object {
        fun createStreakLine(start: GridIndex, end: GridIndex, color: Int): StreakLine {
            val streakLine = StreakLine()
            streakLine.color = color
            streakLine.startIndex = start
            streakLine.endIndex = end
            return streakLine
        }
    }
}
