package com.wordpuzzle.app.android.main.base

enum class State {
    Loading, Error, Empty, Content, NoResult
}

data class ContentState(val state: State, val message: String?)