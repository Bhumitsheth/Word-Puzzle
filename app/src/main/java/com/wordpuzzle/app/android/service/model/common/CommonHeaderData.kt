package com.wordpuzzle.app.android.service.model.common

class CommonHeaderData {
    var isShowLeftImg = false
    var title = ""
    var isShowTitle = false
    var isShowRightImg = false
    var image: Int = 0

    constructor(isShowLeftImg: Boolean, title: String, isShowTitle: Boolean) {
        this.isShowLeftImg = isShowLeftImg
        this.title = title
        this.isShowTitle = isShowTitle
    }

    constructor(isShowLeftImg: Boolean, title: String, isShowTitle: Boolean, isShowRightImg: Boolean) {
        this.isShowLeftImg = isShowLeftImg
        this.title = title
        this.isShowTitle = isShowTitle
        this.isShowRightImg = isShowRightImg
    }

    constructor(title: String) {
        this.title = title
    }
    constructor(title: String, image: Int, isShowRightImg: Boolean) {
        this.title = title
        this.image = image
        this.isShowRightImg = isShowRightImg
    }
}