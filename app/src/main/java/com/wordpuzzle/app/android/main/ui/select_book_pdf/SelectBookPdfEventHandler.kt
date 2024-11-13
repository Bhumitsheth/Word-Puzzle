package com.wordpuzzle.app.android.main.ui.select_book_pdf

import com.wordpuzzle.app.android.BuildConfig
import com.wordpuzzle.app.android.utils.openLinkInBrowser
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.utils.*

class SelectBookPdfEventHandler constructor(private val activity: SelectBookPdfActivity): BaseEventHandler() {

    override fun onBackClicked() {
        activity.finish()
    }

    fun tvHowToPlayQuestionClick() {
        openLinkInBrowser(BuildConfig.howToPlayGuideLines, activity)
    }
}