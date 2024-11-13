package com.wordpuzzle.app.android.data.sqlite

import com.wordpuzzle.app.android.domain.data.source.WordDataSource
import com.wordpuzzle.app.android.domain.model.Word
import javax.inject.Inject

/**
 * Created by abdularis on 18/07/17.
 */
class WordSQLiteDataSource @Inject constructor(private val mHelper: DbHelper) : WordDataSource {
    override fun getWords(callback: WordDataSource.Callback?) {
        val db = mHelper.readableDatabase
        val cols = arrayOf<String>(
            DbContract.WordBank._ID,
            DbContract.WordBank.COL_STRING
        )
        val c = db.query(DbContract.WordBank.TABLE_NAME, cols, null, null, null, null, null)
        val wordList: MutableList<Word> = ArrayList()
        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                wordList.add(Word(c.getInt(0), c.getString(1)))
                c.moveToNext()
            }
        }
        c.close()
        callback!!.onWordsLoaded(wordList)
    }
}
