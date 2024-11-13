package com.wordpuzzle.app.android.main.ui.forget

import android.content.Intent
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.ui.login.LoginActivity

class ForgetPasswordHandler constructor(private val activity: ForgetPasswordActivity): BaseEventHandler() {
    override fun onBackClicked() {
        activity.finish()
    }

    fun tvLoginClick() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }

}