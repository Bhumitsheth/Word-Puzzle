package com.wordpuzzle.app.android.main.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityProfileBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.login.LoginActivity
import com.wordpuzzle.app.android.main.ui.profile.DI.ProfileComponent
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.CommonHeaderData
import com.wordpuzzle.app.android.service.model.response.EditProfileResponseModel
import com.wordpuzzle.app.android.service.model.response.GetProfileResponseModel
import com.wordpuzzle.app.android.utils.ImageLoaderUtils
import com.wordpuzzle.app.android.utils.showErrorSnackBar
import com.wordpuzzle.app.android.utils.showSuccessSnackBar
import java.io.File

class ProfileActivity : BaseMainActivity<ActivityProfileBinding>() {
    override fun getLayoutId() = R.layout.activity_profile

    override fun create(savedInstanceState: Bundle?) {
        binding.headerData = CommonHeaderData(true, resources.getString(R.string.profile), true, true)
        val handler = ProfileEventHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        viewModel.getLiveGetProfileData().observe(this) {
                response: Resource<GetProfileResponseModel?> -> consumeGetProfileResponse(response)
        }
        viewModel.getLiveEditProfileData().observe(this) {
            response: Resource<EditProfileResponseModel?> -> consumeEditProfileResponse(response)
        }
        // adding on back pressed callback listener.
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<ProfileComponent>()?.inject(this)
    }

    private fun consumeEditProfileResponse(response: Resource<EditProfileResponseModel?>) {
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
                    showSuccessSnackBar(binding.rootLayout, response.data.message!!)

                    viewModel.appPrefs.setString(AppPrefs.FULL_NAME, response.data.data.fullName)

                    viewModel.onCheckBoxClicked(false)

                    if (binding.cbChangePassword.isChecked){
                        viewModel.appPrefs.clearAll()
                        val intent = Intent(this, LoginActivity::class.java)
                        this.startActivity(intent)
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
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.REQUEST_CODE_CAMERA_STORAGE_RESULT) {
            if (resultCode == RESULT_OK) {
                if (data?.getStringExtra(AppConstants.image) != null) {
                    val image = data.getStringExtra(AppConstants.image)
                    val fullGalleryPath = data.getStringExtra("fullGalleryPath")
                    println("image name $image")
                    val file = File(image)
                    val length = file.length() / 1024
                    val length1 = length / 1024
                    if (length1 < 5) {
                        viewModel.fileName.value = image
                        ImageLoaderUtils.loadImageFromUrlViaGlide(
                            this,
                            image,
                            binding.ivUserProfile,
                            R.drawable.ic_user_profile
                        )
                    } else {
                        showErrorSnackBar(binding.rootLayout, resources.getString(R.string.maxUploadSizeIs5Mb))
                    }
                } else {
                    showErrorSnackBar(binding.rootLayout, resources.getString(R.string.dialog_file_not_found))
                }
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_OK)
            finish()
        }
    }
}