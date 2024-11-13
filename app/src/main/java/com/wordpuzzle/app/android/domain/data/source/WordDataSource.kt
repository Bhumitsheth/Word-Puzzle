package com.wordpuzzle.app.android.domain.data.source

import com.wordpuzzle.app.android.domain.model.Word

/**
 * Created by abdularis on 18/07/17.
 */
interface WordDataSource {
    interface Callback {
        fun onWordsLoaded(words: MutableList<Word>)
    }

    fun getWords(callback: Callback?)
}
