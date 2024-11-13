package com.wordpuzzle.app.android.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.wordpuzzle.app.android.constants.AppConstants
import java.util.Objects
import java.util.regex.Matcher
import java.util.regex.Pattern

object FormValidation {

    /**
     * ***************************************************************************
     * ************************** CHECK EMAIL VALIDATION *************************
     * ***************************************************************************
     */
    private fun isEmail(text: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val p: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val m: Matcher = p.matcher(text)
        return m.matches()
    }

    /**
     * ***************************************************************************
     * *********************** CHECK PHONE NUMBER VALIDATION *********************
     * ***************************************************************************
     */
    fun isPhone(text: String?): Boolean {
        return if (!TextUtils.isEmpty(text)) {
            TextUtils.isDigitsOnly(text)
        } else {
            false
        }
    }

    fun isValidMobile(phone: String): Boolean {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length in 7..10
        }
        return false
    }

    /**
     * ***************************************************************************
     * ************************** CHECK EMAIL VALIDATION *************************
     * ***************************************************************************
     */
    fun checkEmailView(
        editText: EditText,
        strErr: String?, strErr1: String?
    ): Boolean {
        val email = Objects.requireNonNull(editText.text).toString().trim { it <= ' ' }
        if (checkEmptyView(editText, strErr)) {
            return true
        } else if (!isValidEmail(editText.text.toString().trim { it <= ' ' })) {
            showErrorSnackBar(editText, strErr1!!)
            return true
        } else if (AppConstants.EMAIL_PATTERN.matcher(email).matches()) {
            showErrorSnackBar(editText, strErr1!!)
            return true
        }
        return false
    }

    /**
     * ***************************************************************************
     * ********************** CHECK EDITTEXT EMPTY VALIDATION ********************
     * ***************************************************************************
     */
    fun checkEmptyView(
        editText: EditText?,
        strErr: String?
    ): Boolean {
        if (editText != null) {
            val email = editText.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                showErrorSnackBar(editText, strErr!!)
                editText.requestFocus()
                return true
            }
            return false
        }
        return false
    }

    /**
     * ***************************************************************************
     * ********************* CHECK EDITTEXT LENGTH VALIDATION ********************
     * ***************************************************************************
     */
    fun checkLength(
        editText: EditText,
        error: String?,
        length: Int
    ): Boolean {
        if (editText.length() < length) {
            showErrorSnackBar(editText, error!!)
            editText.requestFocus()
            return true
        }
        return false
    }


    /**
     * ***************************************************************************
     * **************************** PERMISSION UTILS ***************************
     * ***************************************************************************
     */
    fun checkPermission(context: Context?, permission: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showPermissionsDialog(activity: Activity?, permissions: Array<String>, REQUEST_CODE: Int) {
        ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_CODE)
    }

    fun shouldShowRequestPermissionRationale(activity: Activity?, permission: String?): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission!!)
    }

    fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.length == 0
    }

    fun getCurrentNanoTime(): String {
        val tsLong = System.nanoTime()
        return tsLong.toString()
    }

    /**
     * ***************************************************************************
     * ************************ REMOVE FIRST THREE NUMBER ************************
     * ***************************************************************************
     */
    fun getPhoneNumber(phoneNumber: String):String {
        return phoneNumber.toString().substring(3)
    }

    /**
     * ***************************************************************************
     * ****************** TEXT INPUT LAYOUT ERROR MESSAGE UTILS ******************
     * ***************************************************************************
     */
    fun setErrorOnTextInputLayout(textInputLayout: TextInputLayout?, text: String?) {
        if (textInputLayout != null) {
            if (text != null) {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = text
            } else {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
            }
        }
    }

    /**
     * ***************************************************************************
     * *************************** VALIDATION EMAIL ID ***************************
     * ***************************************************************************
     */
    fun isValidEmail(email: String?): Boolean {
        val check: Boolean
        val p: Pattern
        val EMAIL_STRING = "[a-zA-Z0-9.+_-]+@[a-z]+\\.+[a-z]+"
        p = Pattern.compile(EMAIL_STRING)
        val m: Matcher = p.matcher(email!!)
        check = m.matches()
        return check
    }

    fun isValidName(name: String?): Boolean {
        val check: Boolean
        val p: Pattern
        val m: Matcher
        val NAME_STRING = "[a-zA-z]+([ '-][a-zA-Z]+)*"
        p = Pattern.compile(NAME_STRING)
        m = p.matcher(name)
        check = m.matches()
        return check
    }
}