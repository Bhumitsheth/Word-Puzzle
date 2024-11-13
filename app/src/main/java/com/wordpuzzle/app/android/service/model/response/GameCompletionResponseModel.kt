package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GameCompletionResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null
}