package com.wordpuzzle.app.android.main.ui.splash

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivitySplashBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity
import com.wordpuzzle.app.android.main.ui.splash.di.SplashComponent

class SplashActivity : BaseMainActivity<ActivitySplashBinding>() {
    override fun getLayoutId() = R.layout.activity_splash

    override fun create(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        viewModel.setBinding(binding, this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (viewModel.appPrefs.getRegisterResponseData() != null)
                startActivityWithClearStack(HomeActivity::class.java)
            else startActivityWithClearStack(SignUpActivity::class.java)
        }, 1000)
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this, factory).get(SplashViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<SplashComponent>()?.inject(this)
    }

    override fun getTheme(): Resources.Theme {
        val theme: Resources.Theme = super.getTheme()
        theme.applyStyle(R.style.AppTheme, true)
        // you could also use a switch if you have many themes that could apply
        return theme
    }
}