package com.wordpuzzle.app.android.utils

import android.util.Patterns

object StringValidation {

    fun isStringEmpty(string: String?): Boolean {
        return string.isNullOrEmpty()
    }

    fun isStringValid(string: String?): Boolean {
        return !string.isNullOrEmpty()
    }

    fun isEmailValid(email: String?): Boolean {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhoneValid(phone: String?): Boolean {
        return phone != null && (phone.length == 8)
    }

    fun isPasswordValid(password: String?): Boolean {
        return password != null && password.length > 5
    }
}
//pHwspc@$%440