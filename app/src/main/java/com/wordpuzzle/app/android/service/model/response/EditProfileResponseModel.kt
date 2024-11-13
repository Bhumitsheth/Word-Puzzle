package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class EditProfileResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: ProfileDataModel? = null
}

class ProfileDataModel {
    @SerializedName("userCode")
    @Expose
    val userCode: Int? = null

    @SerializedName("fullName")
    @Expose
    val fullName: String? = null
}