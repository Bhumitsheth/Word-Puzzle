package com.wordpuzzle.app.android.main.ui.sign_up

import android.content.Intent
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity

class SignUpEventHandler internal constructor(private val activity: SignUpActivity): BaseEventHandler() {

    override fun onBackClicked() {
        activity.finish()
    }

    fun tvLoginClick() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }
}