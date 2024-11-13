package com.wordpuzzle.app.android.domain.usecases

/**
 * Created by abdularis on 08/07/17.
 */
abstract class UseCase<P : UseCase.Params?, O : UseCase.Result?> {
    var callback: Callback<O>? = null
    var params: P? = null
        internal set

    fun run() {
        execute(params)
    }

    protected abstract fun execute(p: P?)
    fun setParams(params: P) {
        this.params = params
    }

    interface Callback<O : Result?> {
        fun onSuccess(result: O)
        fun onFailed(errMsg: String?)
    }

    interface Params
    interface Result
}
