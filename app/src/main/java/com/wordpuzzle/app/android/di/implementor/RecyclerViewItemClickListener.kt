package com.wordpuzzle.app.android.di.implementor

import android.view.View

// onClickListener Interface
interface RecyclerViewItemClickListener {
    fun onItemClick(position: Int, flag: Int, view: View)
}