package com.wordpuzzle.app.android.domain.usecases

/**
 * Created by abdularis on 09/07/17.
 */
interface UseCaseExecutor {
    fun <P : UseCase.Params?, O : UseCase.Result?> execute(
        useCase: UseCase<P, O>?,
        cb: UseCase.Callback<O>?
    )
}
