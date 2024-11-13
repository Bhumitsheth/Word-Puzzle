package com.wordpuzzle.app.android.service.model.static_model

class ScoreModel {
    var scoreId = ""
    var scoreName = ""
    var score = ""

    constructor(scoreId: String, scoreName: String, score: String) {
        this.scoreId = scoreId
        this.scoreName = scoreName
        this.score = score
    }
}