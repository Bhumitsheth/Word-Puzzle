package com.wordpuzzle.app.android.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.NumberPicker
import android.widget.TextView
import com.wordpuzzle.app.android.R

class StyledNumberPicker(context: Context, attrs: AttributeSet) : NumberPicker(context, attrs) {
    override fun setDisplayedValues(displayedValues: Array<out String>?) {
        super.setDisplayedValues(displayedValues)
        if (displayedValues != null) {
            for (i in displayedValues.indices) {
                val child = getChildAt(i)
                if (child is TextView) {
                    // Set text style for selected item
                    if (i == value) {
//                        TextViewCompat.setTextAppearance(child, R.style.SelectedTextStyle)
//                        applyStyle(child, R.style.SelectedTextStyle)
                        child.setTextAppearance(R.style.SelectedTextStyle)
                    } else {
                        child.setTextAppearance(R.style.UnselectedTextStyle)
                    }
                }
            }
        }
    }
}
