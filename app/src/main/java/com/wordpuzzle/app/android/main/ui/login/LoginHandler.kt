package com.wordpuzzle.app.android.main.ui.login

import android.content.Intent
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.ui.forget.ForgetPasswordActivity
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity

class LoginHandler constructor(private val activity: LoginActivity) : BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }

    fun tvForgetPasswordClick() {
        val intent = Intent(activity, ForgetPasswordActivity::class.java)
        activity.startActivity(intent)
    }

    fun tvRegistrationClick() {
        val intent = Intent(activity, SignUpActivity::class.java)
        activity.startActivity(intent)
    }
}