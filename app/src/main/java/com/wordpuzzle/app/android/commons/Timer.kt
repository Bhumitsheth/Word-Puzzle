package com.wordpuzzle.app.android.commons

import android.os.Handler
import android.os.SystemClock
import android.util.Log
import java.util.Timer
import java.util.TimerTask

/**
 * Created by abdularis on 14/04/17.
 */
class Timer(interval: Long) {
    private val mTimeoutListeners: MutableList<OnTimeoutListener>
    private val mStoppedListeners: MutableList<OnStoppedListener>
    private val mStartedListeners: MutableList<OnStartedListener>
    var isStarted = false
        private set
    private var mStartTime = 0L
    private var mEllapsedTime = 0L
    private val mInterval: Long
    private var mTimer: Timer? = null
    private val mHandler: Handler
    private val mRunnable: Runnable

    init {
        mTimeoutListeners = ArrayList()
        mStoppedListeners = ArrayList()
        mStartedListeners = ArrayList()
        mInterval = if (interval > 0) interval else 1000
        mHandler = Handler()
        mRunnable = Runnable {
            mEllapsedTime = SystemClock.uptimeMillis() - mStartTime
            callTimeoutListeners(mEllapsedTime)
        }
    }

    fun start() {
        if (isStarted) return
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                mHandler.post(mRunnable)
            }
        }
        isStarted = true
        mStartTime = SystemClock.uptimeMillis()
        mTimer = Timer()
        mTimer!!.schedule(task, mInterval, mInterval)
        callStartedListener()
    }

    fun stop() {
        if (!isStarted) return
        mTimer!!.cancel()
        mTimer = null
        isStarted = false
        mEllapsedTime = SystemClock.uptimeMillis() - mStartTime
        callStoppedListeners(mEllapsedTime)
        Log.v("MyTimer", "stop called")
    }

    fun addOnTimeoutListener(listener: OnTimeoutListener) {
        mTimeoutListeners.add(listener)
    }

    fun addOnStoppedListener(listener: OnStoppedListener) {
        mStoppedListeners.add(listener)
    }

    fun addOnStartedListener(listener: OnStartedListener) {
        mStartedListeners.add(listener)
    }

    private fun callTimeoutListeners(ellapsedTime: Long) {
        for (listener in mTimeoutListeners) listener.onTimeout(ellapsedTime)
    }

    private fun callStoppedListeners(ellapsedTime: Long) {
        for (listener in mStoppedListeners) listener.onStopped(ellapsedTime)
    }

    private fun callStartedListener() {
        for (listener in mStartedListeners) listener.onStarted()
    }

    interface OnTimeoutListener {
        fun onTimeout(ellapsedTime: Long)
    }

    interface OnStoppedListener {
        fun onStopped(ellapsedTime: Long)
    }

    interface OnStartedListener {
        fun onStarted()
    }
}
