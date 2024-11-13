package com.wordpuzzle.app.android.service.model.common

data class NavigationDrawerViewDataModel(
    var id: Int = 0,
    var image: Int = 0,
    var title: String = "",
    var isShowToggle: Boolean = false,
    var isToggleActive: Boolean = false
)