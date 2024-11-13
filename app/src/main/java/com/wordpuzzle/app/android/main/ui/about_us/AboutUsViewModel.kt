package com.wordpuzzle.app.android.main.ui.about_us

import com.wordpuzzle.app.android.databinding.ActivityAboutUsBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.repository.UserRepository

class AboutUsViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository

) : BaseViewModel() {
    //binding object
    private var binding: ActivityAboutUsBinding? = null
    private var activity: AboutUsActivity? = null

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityAboutUsBinding, activity: AboutUsActivity) {
        this.binding = binding
        this.activity = activity
    }
}