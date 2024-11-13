package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetProfileResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: GetProfileDataModel? = null
}

class GetProfileDataModel {
    @SerializedName("userCode")
    @Expose
    val userCode: Int? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("fullName")
    @Expose
    val fullName: String? = null

    @SerializedName("age")
    @Expose
    val age: String? = null

    @SerializedName("alreadyHavePassword")
    @Expose
    val alreadyHavePassword: Boolean? = null

    @SerializedName("profileImage")
    @Expose
    val profileImage: String? = null

    @SerializedName("score")
    @Expose
    val score: String? = null
}