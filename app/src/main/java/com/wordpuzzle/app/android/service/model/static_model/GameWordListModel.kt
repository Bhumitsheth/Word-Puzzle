package com.wordpuzzle.app.android.service.model.static_model

class GameWordListModel {
    var word = ""
    var isFound = false

    constructor(word: String, isSelected: Boolean) {
        this.word = word
        this.isFound = isSelected
    }
}