package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WordResponseList {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: WordDataModel? = null
}

class WordDataModel {
    @SerializedName("wordsList")
    @Expose
    val wordsList: List<String>? = null
}