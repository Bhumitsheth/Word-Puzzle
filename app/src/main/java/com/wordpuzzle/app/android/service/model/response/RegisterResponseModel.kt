package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: RegisterDataModel? = null
}

class RegisterDataModel {
    @SerializedName("userCode")
    @Expose
    val userCode: Int? = null

    @SerializedName("fullName")
    @Expose
    val fullName: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("age")
    @Expose
    val age: String? = null

    @SerializedName("token")
    @Expose
    val token: String? = null
}