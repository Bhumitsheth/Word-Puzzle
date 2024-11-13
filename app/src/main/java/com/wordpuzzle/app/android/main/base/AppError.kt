package com.wordpuzzle.app.android.main.base

data class AppError(
    val title:String?,
    val body: String?,
    val ex: Throwable?,
    val code: Int
)