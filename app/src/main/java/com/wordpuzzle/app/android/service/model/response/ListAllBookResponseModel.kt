package com.wordpuzzle.app.android.service.model.response

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ListAllBookResponseModel {
    @SerializedName("error")
    @Expose
    val error: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: BookAllDataModel? = null
}

class BookAllDataModel {
    @SerializedName("bookList")
    @Expose
    val bookList: ArrayList<BookListModel>? = null
}

class BookListModel {
    var isSelected = false

    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("ebook_name")
    @Expose
    val ebookName: String? = null

    @SerializedName("section_data")
    @Expose
    val sectionData: ArrayList<String>? = null
}

