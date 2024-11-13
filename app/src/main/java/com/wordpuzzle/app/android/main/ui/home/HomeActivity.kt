package com.wordpuzzle.app.android.main.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityHomeBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.home.di.HomeComponent
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.main.ui.sign_up.SignUpActivity
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.GetProfileResponseModel
import com.wordpuzzle.app.android.service.model.response.LogoutResponseModel
import com.wordpuzzle.app.android.utils.ViewUtil
import com.wordpuzzle.app.android.utils.showErrorSnackBar

class HomeActivity : BaseMainActivity<ActivityHomeBinding>() {

    override fun getLayoutId() = R.layout.activity_home

    @RequiresApi(Build.VERSION_CODES.R)
    override fun create(savedInstanceState: Bundle?) {
        ViewUtil.setFullScreen(this, true)
//        ViewUtil.hideStatusBar(this)
        val handler = HomeEventHandler(this, binding)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        viewModel.getLiveData().observe(this) {
            response: Resource<LogoutResponseModel?> -> consumeResponse(response)
        }
        viewModel.getLiveGetProfileData().observe(this) {
            response: Resource<GetProfileResponseModel?> -> consumeGetProfileResponse(response)
        }
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<HomeComponent>()?.inject(this)
    }

    private fun consumeResponse(response: Resource<LogoutResponseModel?>) {
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
                    if (viewModel.isCheckTick.value!!) {
                        viewModel.appPrefs.clearAll()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra(AppConstants.isShowPopup,viewModel.isShowPopup.value)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        viewModel.appPrefs.clearAll()
                        val intent = Intent(this, SignUpActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }

    private fun consumeGetProfileResponse(response: Resource<GetProfileResponseModel?>) {
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
                    viewModel.userProfile.value = response.data.data
                    binding.tvPlayerName.text = response.data.data.fullName
                    viewModel.profileImage.value = response.data.data.profileImage
                    binding.navigationDrawerLayout.tvPlayerNameDrawer.text = response.data.data.fullName
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val name = viewModel.appPrefs.getString(AppPrefs.FULL_NAME)
        binding.tvPlayerName.text = name
        binding.navigationDrawerLayout.tvPlayerNameDrawer.text = name
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.getUserProfile()
        }
    }
}