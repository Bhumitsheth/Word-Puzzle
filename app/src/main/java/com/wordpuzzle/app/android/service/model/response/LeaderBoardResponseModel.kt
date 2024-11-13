package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LeaderBoardResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: MonthlyWeeklyList? = null
}

class MonthlyWeeklyList {
    @SerializedName("monthlyList")
    @Expose
    var monthlyList: ArrayList<MonthlyListModel>? = null

    @SerializedName("weeklyList")
    @Expose
    var weeklyList: ArrayList<WeeklyListModel>? = null
}

class MonthlyListModel {
    @SerializedName("rank")
    @Expose
    val rank: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("score")
    @Expose
    val score: Int? = null

    @SerializedName("image")
    @Expose
    val image: String? = null
}

class WeeklyListModel {
    @SerializedName("rank")
    @Expose
    val rank: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("score")
    @Expose
    val score: Int? = null

    @SerializedName("image")
    @Expose
    val image: String? = null
}