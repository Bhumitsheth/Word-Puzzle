package com.wordpuzzle.app.android.service.model.static_model

class SelectBookPdfModel {
    var bookId = ""
    var bookName = ""
    var isSelected = false

    constructor(bookId: String, bookName: String, isSelected: Boolean) {
        this.bookId = bookId
        this.bookName = bookName
    }
}