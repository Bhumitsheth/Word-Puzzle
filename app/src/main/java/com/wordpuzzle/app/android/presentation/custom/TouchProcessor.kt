package com.wordpuzzle.app.android.presentation.custom

import android.view.MotionEvent

//import android.support.annotation.NonNull;
/**
 * Created by abdularis on 15/06/17.
 *
 * Digunakan untuk memproses data dari MouseEvent agar lebih mudah digunakan,
 * memiliki movement threshold untuk mengubah sensitivitas perpindahan sentuhan
 */
internal class TouchProcessor(private val mListener: OnTouchProcessed, moveThreshold: Float) {
    private var mIsDown = false
    private val mMoveThreshold: Float
    private var mLastX = 0f
    private var mLastY = 0f

    init {
        mMoveThreshold = Math.max(moveThreshold, 0.1f)
    }

    /*
        call this function to process touch event
     */
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
                mLastY = event.y
                mIsDown = true
                mListener.onDown(event)
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsDown = false
                mListener.onUp(event)
            }

            MotionEvent.ACTION_MOVE -> if (mIsDown &&
                (Math.abs(mLastX - event.x) > mMoveThreshold || Math.abs(mLastY - event.y) > mMoveThreshold)
            ) {
                mLastX = event.x
                mLastY = event.y
                mListener.onMove(event)
            }

            else -> return false
        }
        return true
    }

    internal interface OnTouchProcessed {
        fun onDown(event: MotionEvent?)
        fun onUp(event: MotionEvent?)
        fun onMove(event: MotionEvent?)
    }
}
