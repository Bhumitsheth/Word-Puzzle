package com.wordpuzzle.app.android.presentation

import android.os.Handler
import com.wordpuzzle.app.android.domain.usecases.UseCase
import com.wordpuzzle.app.android.domain.usecases.UseCaseExecutor
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by abdularis on 04/07/17.
 */
class AndroidUseCaseExecutor : UseCaseExecutor {
    private val mHandler = Handler()
    private val mThreadPool: ThreadPoolExecutor

    init {
        mThreadPool = ThreadPoolExecutor(
            POOL_SIZE, MAX_POOL_SIZE, TIMEOUT.toLong(), TimeUnit.SECONDS,
            ArrayBlockingQueue(MAX_POOL_SIZE)
        )
    }

    override fun <P : UseCase.Params?, O : UseCase.Result?> execute(
        useCase: UseCase<P, O>?,
        callback: UseCase.Callback<O>?
    ) {
        useCase!!.callback = UiThreadCallbackAdapter(callback)
        mThreadPool.execute { useCase.run() }
    }

    private inner class UiThreadCallbackAdapter<O : UseCase.Result?> internal constructor(
        var mCallback: UseCase.Callback<O>?
    ) : UseCase.Callback<O> {
        override fun onSuccess(result: O) {
            mHandler.post { mCallback!!.onSuccess(result) }
        }

        override fun onFailed(msg: String?) {
            mHandler.post { mCallback!!.onFailed(msg) }
        }
    }

    companion object {
        private const val POOL_SIZE = 2
        private const val MAX_POOL_SIZE = 4
        private const val TIMEOUT = 30
    }
}
