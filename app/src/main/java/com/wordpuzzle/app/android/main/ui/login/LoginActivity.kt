package com.wordpuzzle.app.android.main.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLoginBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.home.HomeActivity
import com.wordpuzzle.app.android.main.ui.login.di.LoginComponent
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.utils.showErrorSnackBar

class LoginActivity : BaseMainActivity<ActivityLoginBinding>() {
    override fun getLayoutId() = R.layout.activity_login

    override fun create(savedInstanceState: Bundle?) {
        val handler = LoginHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this,intent)
        viewModel.getLiveData().observe(this) { response: Resource<RegisterResponseModel?> -> consumeResponse(response) }
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<LoginComponent>()?.inject(this)
    }

    private fun consumeResponse(response: Resource<RegisterResponseModel?>) {
        when (response.status) {
            Resource.Status.LOADING -> showProgressBlocking()
            Resource.Status.ERROR -> {
                hideProgressBlocking()
                if (response.error?.message != null) {
                    showErrorSnackBar(binding.rootLayout, response.error.message!!)
                }
            }

            Resource.Status.SUCCESS -> {
                hideProgressBlocking()
                if (response.data!!.error == false && response.data.data != null) {
                    viewModel.appPrefs.setRegisterResponseData(response.data.data)
                    viewModel.appPrefs.setString(AppPrefs.FULL_NAME, response.data.data.fullName)
                    viewModel.appPrefs.setString(AppPrefs.EMAIL, response.data.data.email)
                    viewModel.appPrefs.setBoolean(AppPrefs.isSoundOnOff, true)
                    viewModel.appPrefs.setBoolean(AppPrefs.isMusicOnOff, true)
                    val intent = Intent(this, HomeActivity::class.java)
                    this.startActivity(intent)
                    finishAffinity()
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }
}