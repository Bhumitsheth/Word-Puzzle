package com.wordpuzzle.app.android.commons

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Created by abdularis on 19/07/17.
 */
object DurationFormatter {
    @JvmStatic
    fun fromInteger(duration: Int): String {
        if (duration <= 0) return "00:00"
        val secs = duration % 60
        val min = duration / 60
        return String.format(Locale.US, "%02d:%02d", min, secs)
    }
}
