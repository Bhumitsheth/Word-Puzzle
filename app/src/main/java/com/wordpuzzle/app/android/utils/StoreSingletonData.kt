package com.wordpuzzle.app.android.utils

object StoreSingletonData {
    private var wordsList: List<String>? = null
    private var storeWordsList: List<String>? = null

    fun setWordListKey(wordsList: List<String>?) {
        this.wordsList = wordsList
    }

    fun setStoreWordListKey(storeWordsList: List<String>?) {
        this.storeWordsList = storeWordsList
    }

    fun getWordListKey(): List<String> {
        return wordsList!!
    }

    fun getStoreWordListKey(): List<String> {
        return storeWordsList!!
    }
}