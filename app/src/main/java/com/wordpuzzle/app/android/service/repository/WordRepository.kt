package com.wordpuzzle.app.android.service.repository

import android.content.Context
import com.wordpuzzle.app.android.service.main.ServerApi
import com.wordpuzzle.app.android.service.model.response.*
import io.reactivex.Observable

class WordRepository(val context: Context, private val serverApi: ServerApi) {
    fun generateWordListAPI(params: HashMap<String, Any>): Observable<GenerateWordListResponseModel> {
        return serverApi.generateWordListAPI(params)
    }

    fun gameCompletionAPI(params: HashMap<String, Any>): Observable<GameCompletionResponseModel> {
        return serverApi.gameCompletionAPI(params)
    }
}