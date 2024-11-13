package com.wordpuzzle.app.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.wordpuzzle.app.android.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * ***************************************************************************
 * *************************** Show Error Snack Bar **************************
 * ***************************************************************************
 */
fun showErrorSnackBar(view: View?, text: String) {
    if (view != null) {
        val snackBar: Snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.black))
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        snackBar.show()
    }
}

/**
 * ***************************************************************************
 * ************************** Show Success Snack Bar *************************
 * ***************************************************************************
 */
fun showSuccessSnackBar(view: View?, text: String) {
    if (view != null) {
        val snackBar: Snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorGreen))
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(view.context, R.color.colorGreenTextColor))
        snackBar.show()
    }
}

/**
 * ***************************************************************************
 * ************************** Web Site Open Chrome ***************************
 * ***************************************************************************
 */
@SuppressLint("QueryPermissionsNeeded")
fun openWebsite(url: String, activity: Activity) {
    try {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        activity.startActivity(intent)
    } catch (e: Exception) {
        // Handle any exceptions that occur while parsing the URL
        showErrorSnackBarActivity(activity, "Invalid URL")
    }
}

/**
 * ***************************************************************************
 * *********************** Show Error Snack Bar Activity**********************
 * ***************************************************************************
 */
fun showErrorSnackBarActivity(activity: Activity?, text: String) {
    if (activity != null) {
        val snackBar: Snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.black))
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(activity, R.color.white))
        snackBar.show()
    }
}

/**
 * ***************************************************************************
 * *************************** Direct Call Number ****************************
 * ***************************************************************************
 */
fun callNumber(callNumber: String, activity: Activity) {
    val intent = Intent(Intent.ACTION_DIAL)
    val p = "tel:${callNumber}"
    intent.data = Uri.parse(p)
    activity.startActivity(intent)
}

/**
 * ***************************************************************************
 * ************************* Get Current Month Name **************************
 * ***************************************************************************
 */
fun getCurrentMonthName(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

/**
 * ***************************************************************************
 * ************************* Get Current Month Name **************************
 * ***************************************************************************
 */
fun getCurrentYearName(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    return year.toString()
}

/**
 * ***************************************************************************
 * ************************ Number Between Add Dash **************************
 * ***************************************************************************
 */
fun addSpacesToPhoneNumber(phoneNumber: String): String {
    // Define a regular expression pattern to match digits in groups of 2 and 5
    val regex = "(\\d{2})(\\d{5})(\\d+)"

    // Replace the matched groups with spaces in between
    return phoneNumber.replace(regex.toRegex(), "$1-$2-$3")
}

/**
 * ***************************************************************************
 * ************************** Open Link In Browser ***************************
 * ***************************************************************************
 */
fun openLinkInBrowser(url: String, activity: Activity) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
}

fun convertDateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    try {
        val date: Date = inputFormat.parse(inputDate)!!
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "" // Return an empty string or handle the error as needed
}

/**
 * ***************************************************************************
 * ************************** Extract Page Numbers ***************************
 * ***************************************************************************
 */
fun extractPageNumbers(no: String): Pair<String, String> {
    val pages = no.split("-")
    return when {
        pages.size == 2 -> Pair(pages[0], pages[1])
        pages.isEmpty() -> Pair("", "") // No pages found
        pages.size == 1 -> Pair(pages[0], "") // Only start page found
        else -> Pair(pages[0], pages.last()) // Multiple pages, take the first and the last
    }
}

/**
 * ***************************************************************************
 * ****************************** Get Current Date ***************************
 * ***************************************************************************
 */
fun getCurrentDate(format: String): String {
    val date = Calendar.getInstance().time
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    return sdf.format(date)
}

fun calculateScore(words: List<String>, strTime: String): Int {
    var totalCharCount = 0
    for (word in words) {
        val charCount = word.length
        totalCharCount += charCount
    }

    val strTimeDouble = convertTimeStringToDouble(strTime) ?: 0.0
    return when (strTimeDouble) {
        in 0.0..1.0 -> totalCharCount + 10
        in 1.0..2.0 -> totalCharCount + 9
        in 2.0..3.0 -> totalCharCount + 8
        in 3.0..4.0 -> totalCharCount + 7
        in 4.0..5.0 -> totalCharCount + 6
        in 5.0..6.0 -> totalCharCount + 5
        in 6.0..7.0 -> totalCharCount + 4
        in 7.0..8.0 -> totalCharCount + 3
        in 8.0..9.0 -> totalCharCount + 2
        in 9.0..10.0 -> totalCharCount + 1
        else -> totalCharCount
    }
}

fun convertTimeStringToDouble(strTime: String): Double? {
    return strTime.toDoubleOrNull()
}

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}