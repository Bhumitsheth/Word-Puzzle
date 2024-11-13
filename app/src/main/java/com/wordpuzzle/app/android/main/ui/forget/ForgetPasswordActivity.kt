package com.wordpuzzle.app.android.main.ui.forget

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityForgetPasswordBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.forget.di.ForgetPasswordComponent
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.*
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.utils.*

class ForgetPasswordActivity : BaseMainActivity<ActivityForgetPasswordBinding>() {
    override fun getLayoutId() = R.layout.activity_forget_password

    override fun create(savedInstanceState: Bundle?) {
        binding.headerData = CommonHeaderData(true, resources.getString(R.string.forget_password_small), true,true)
        val handler = ForgetPasswordHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        viewModel.getLiveForgotPasswordData()
            .observe(this) { response: Resource<ForgotPasswordResponseModel?> ->
                consumeForgotPasswordResponse(response)
            }
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: ForgetPasswordViewModel by lazy {
        ViewModelProvider(this, factory).get(ForgetPasswordViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<ForgetPasswordComponent>()?.inject(this)
    }

    private fun consumeForgotPasswordResponse(response: Resource<ForgotPasswordResponseModel?>) {
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
                if (response.data!!.error == false) {
                    showSuccessSnackBar(binding.rootLayout, response.data.message.toString())
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(LoginActivity::class.java)
                        finishAffinity()
                    }, 2000)
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }
}