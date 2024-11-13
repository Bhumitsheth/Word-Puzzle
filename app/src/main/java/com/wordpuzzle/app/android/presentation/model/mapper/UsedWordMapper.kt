package com.wordpuzzle.app.android.presentation.model.mapper

import com.wordpuzzle.app.android.commons.Mapper
import com.wordpuzzle.app.android.domain.model.UsedWord
import com.wordpuzzle.app.android.presentation.model.UsedWordViewModel

/**
 * Created by abdularis on 18/07/17.
 */
class UsedWordMapper : Mapper<UsedWord?, UsedWordViewModel?>() {
    override fun map(obj: UsedWord?): UsedWordViewModel {
        return UsedWordViewModel(obj!!)
    }

    override fun revMap(obj: UsedWordViewModel?): UsedWord {
        return obj!!.usedWord
    }
}
