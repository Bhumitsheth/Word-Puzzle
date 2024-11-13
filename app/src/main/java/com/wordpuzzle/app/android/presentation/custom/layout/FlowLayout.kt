package com.wordpuzzle.app.android.presentation.custom.layout

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.wordpuzzle.app.android.R

/**
 * Created by abdularis on 24/06/17.
 *
 * FlowLayout meletakan content secara horizontal dan wrap secara vertical
 * ketika tidak ada lagi space horizontal
 */
class FlowLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var mXSpacing = 0
    private var mYSpacing = 0

    init {
        init(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(heightMeasureSpec)

        // final width and height
        var measuredWidth = 0
        var measuredHeight = 0

        // current maximum width and height in iteration
        var currMaxWidth = 0
        var currMaxHeight = 0
        val availWidth = widthSpec - paddingLeft - paddingRight - mXSpacing
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val cWidth = child.measuredWidth + mXSpacing
            val cHeight = child.measuredHeight + mYSpacing
            currMaxWidth += cWidth
            if (currMaxWidth > availWidth) {
                measuredWidth = Math.max(
                    measuredWidth,
                    Math.max(currMaxWidth - cWidth, cWidth)
                )
                measuredHeight += currMaxHeight
                currMaxWidth = cWidth
                currMaxHeight = 0
            }
            if (i == count - 1) {
                measuredHeight += Math.max(currMaxHeight, cHeight)
            }
            currMaxHeight = Math.max(currMaxHeight, cHeight)
        }

//        measuredWidth += mXSpacing;
        measuredHeight += mYSpacing
        measuredWidth += paddingLeft + paddingRight
        measuredHeight += paddingTop + paddingBottom
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) measuredWidth =
            widthSpec else if (widthMeasureSpec == MeasureSpec.AT_MOST) measuredWidth =
            Math.min(measuredWidth, widthSpec)
        if (heightMode == MeasureSpec.EXACTLY) measuredHeight =
            heightSpec else if (heightMode == MeasureSpec.AT_MOST) measuredHeight =
            Math.min(measuredHeight, heightSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childLeft = paddingLeft + mXSpacing
        val childTop = paddingTop + mYSpacing +10
        val childRight = width - paddingRight - mXSpacing
        val childBottom = height - paddingBottom - mYSpacing
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop

        var totalWidth = 0
        var totalHeight = 0
        var rowWidth = 0
        var rowMaxHeight = 0

        // Calculate the total width and height needed for the children
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (rowWidth + child.measuredWidth > childWidth) {
                totalHeight += rowMaxHeight + mYSpacing
                totalWidth = maxOf(totalWidth, rowWidth)
                rowWidth = child.measuredWidth
                rowMaxHeight = child.measuredHeight
            } else {
                rowWidth += child.measuredWidth + mXSpacing
                rowMaxHeight = maxOf(rowMaxHeight, child.measuredHeight)
            }
        }
        totalHeight += rowMaxHeight
        totalWidth = maxOf(totalWidth, rowWidth)

        // Calculate the starting positions to center the layout
        val startLeft = childLeft + (childWidth - totalWidth) / 2
        val startTop = childTop + (childHeight - totalHeight+20) / 2

        var currLeft = startLeft
        var currTop = startTop
        var currMaxHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (currLeft + child.measuredWidth > childRight) {
                currLeft = startLeft
                currTop += currMaxHeight + mYSpacing
                currMaxHeight = 0
                if (currTop > childBottom) break
            }
            child.layout(
                currLeft,
                currTop,
                Math.min(currLeft + child.measuredWidth, childRight),
                Math.min(currTop + child.measuredHeight, childBottom)
            )
            currLeft += child.measuredWidth + mXSpacing
            currMaxHeight = Math.max(currMaxHeight, child.measuredHeight)
        }
    }

    /*override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childLeft = paddingLeft + mXSpacing
        val childTop = paddingTop + mYSpacing
        val childRight = width - paddingRight - mXSpacing
        val childBottom = height - paddingBottom - mYSpacing
        val childWidth = childRight - childLeft
        val childHeight = childBottom - childTop
        var currLeft = childLeft
        var currTop = childTop
        var currMaxHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (currLeft + child.measuredWidth > childWidth) {
                currLeft = childLeft
                currTop += currMaxHeight + mYSpacing
                currMaxHeight = 0
                if (currTop > childHeight) break
            }
            child.layout(
                currLeft,
                currTop,
                Math.min(currLeft + child.measuredWidth, childRight),
                Math.min(currTop + child.measuredHeight, childBottom)
            )
            currLeft += child.measuredWidth + mXSpacing
            currMaxHeight = Math.max(currMaxHeight, child.measuredHeight)
        }
    }*/

    private fun init(context: Context, attrs: AttributeSet?) {
        mXSpacing = 5
        mYSpacing = 5
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0)
            mXSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, mXSpacing)
            mYSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, mYSpacing)
            a.recycle()
        }
    }
}