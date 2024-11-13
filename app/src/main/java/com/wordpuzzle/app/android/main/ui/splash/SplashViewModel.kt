package com.wordpuzzle.app.android.main.ui.splash

import com.wordpuzzle.app.android.databinding.ActivitySplashBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository

class SplashViewModel(
    val appPrefs: AppPrefs, val repository: UserRepository
) : BaseViewModel() {
    //binding object
    private var binding: ActivitySplashBinding? = null
    private var activity: SplashActivity? = null

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivitySplashBinding, activity: SplashActivity) {
        this.binding = binding
        this.activity = activity
    }
}