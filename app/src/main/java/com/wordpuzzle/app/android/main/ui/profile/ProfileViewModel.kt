package com.wordpuzzle.app.android.main.ui.profile

import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityProfileBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.EditProfileResponseModel
import com.wordpuzzle.app.android.service.model.response.GetProfileDataModel
import com.wordpuzzle.app.android.service.model.response.GetProfileResponseModel
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.FormValidation
import com.wordpuzzle.app.android.utils.RestClient
import com.wordpuzzle.app.android.utils.showErrorSnackBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Objects

class ProfileViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository

) : BaseViewModel() {
    //binding object
    private var binding: ActivityProfileBinding? = null
    private var activity: ProfileActivity? = null

    //Live data Object Object
    val checkBoxChecked = MutableLiveData<Boolean>()
    val fileName: MutableLiveData<String> = MutableLiveData("")
    var userProfile = MutableLiveData<GetProfileDataModel>()

    private val liveEditProfileData = MutableLiveData<Resource<EditProfileResponseModel?>>()
    private val liveGetProfileData = MutableLiveData<Resource<GetProfileResponseModel?>>()

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityProfileBinding, activity: ProfileActivity) {
        this.binding = binding
        this.activity = activity

        getUserProfile()
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveGetProfileData(): MutableLiveData<Resource<GetProfileResponseModel?>> {
        return liveGetProfileData
    }

    fun getLiveEditProfileData(): MutableLiveData<Resource<EditProfileResponseModel?>> {
        return liveEditProfileData
    }

    /**
     * TODO: API---> user/profile
     * API REQUEST FOR GET USER PROFILE
     */
    private fun getUserProfile() {
        hideKeyboard()
        compositeDisposable.add(repository.getProfileUserAPI()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveGetProfileData.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveGetProfileData.setValue(Resource.success(responseModel)) },
                { throwable -> liveGetProfileData.setValue(Resource.error(throwable)) }
            )
        )
    }

    /**
     * TODO: API---> user/account/profile/save
     * API REQUEST USER EDIT PROFILE
     */
    fun tvSaveClick() {
        hideKeyboard()
        if (isValidInput()) {
            var body: MultipartBody.Part? = null
            if (!fileName.value.isNullOrEmpty()) {
                val file = File(fileName.value!!)
                if (file.exists()) {
                    body = MultipartBody.Part.createFormData(AppConstants.profileImage, file.name, RestClient.createRequestBody(file))
                }
            }

            val fullName: String = Objects.requireNonNull(binding!!.etEditName.text).toString().trim { it <= ' ' }
            val changePassword: String = if (binding!!.cbChangePassword.isChecked) "true" else "false"
            val currentPassword: String = Objects.requireNonNull(binding!!.etCurrentPassword.text).toString().trim { it <= ' ' }
            val newPassword: String = Objects.requireNonNull(binding!!.etNewPassword.text).toString().trim { it <= ' ' }

            val params1 = HashMap<String, RequestBody>()
            val params2 = HashMap<String, Any>()

            if (body != null) {
                params1[AppConstants.fullName] = RestClient.createRequestBody(binding!!.etEditName.text.toString().trim())
                params1[AppConstants.changePassword] = RestClient.createRequestBody(if (binding!!.cbChangePassword.isChecked) "true" else "false")
                params1[AppConstants.currentPassword] = RestClient.createRequestBody(binding!!.etCurrentPassword.text.toString().trim())
                params1[AppConstants.newPassword] = RestClient.createRequestBody(binding!!.etNewPassword.text.toString().trim())
            } else {
                params2[AppConstants.fullName] = fullName
                params2[AppConstants.changePassword] = changePassword
                params2[AppConstants.currentPassword] = currentPassword
                params2[AppConstants.newPassword] = newPassword
            }

            getEditProfileUserAPI(params1, params2, body)
        }
    }

    /**
     * ALL INPUT FILL VALIDATION CHECK
     */
    private fun isValidInput(): Boolean {
        val currentPassword: String = Objects.requireNonNull(binding!!.etCurrentPassword.text).toString().trim { it <= ' ' }
        val newPassword: String = Objects.requireNonNull(binding!!.etNewPassword.text).toString().trim { it <= ' ' }
        val confirmPassword: String = Objects.requireNonNull(binding!!.etConfirmPassword.text).toString().trim { it <= ' ' }

        return if (FormValidation.checkEmptyView(binding!!.etEditName, binding!!.etEditName.resources.getString(R.string.nameIsRequire))) {
            binding!!.etEditName.requestFocus()
            false
        } else if(binding!!.cbChangePassword.isChecked) {
            if (FormValidation.checkEmptyView(binding!!.etCurrentPassword, binding!!.etCurrentPassword.resources.getString(R.string.currentPasswordIsRequire))) {
                binding!!.etCurrentPassword.requestFocus()
                false
            } else if (FormValidation.checkEmptyView(binding!!.etNewPassword, binding!!.etNewPassword.resources.getString(R.string.newPasswordIsRequire))) {
                binding!!.etNewPassword.requestFocus()
                false
            } else if (FormValidation.checkEmptyView(binding!!.etConfirmPassword, binding!!.etConfirmPassword.resources.getString(R.string.confirmPasswordIsRequire))) {
                binding!!.etConfirmPassword.requestFocus()
                false
            } else if (newPassword != confirmPassword) {
                showErrorSnackBar(binding!!.etConfirmPassword, binding!!.etConfirmPassword.resources.getString(R.string.confirmPasswordDoesNotMatchWithNewPassword))
                binding!!.etConfirmPassword.requestFocus()
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    /**
     * EDIT USER PROFILE API
     */
    private fun getEditProfileUserAPI(params1: HashMap<String, RequestBody>, params2: HashMap<String, Any>, body: MultipartBody.Part?) {
        if (body != null) {
            compositeDisposable.add(repository.editProfileUserImageAPI(params1, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> liveEditProfileData.setValue(Resource.loading()) }
                .subscribe({ responseModel -> liveEditProfileData.setValue(Resource.success(responseModel)) },
                    { throwable -> liveEditProfileData.setValue(Resource.error(throwable)) })
            )
        } else {
            compositeDisposable.add(repository.editProfileUserAPI(params2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> liveEditProfileData.setValue(Resource.loading()) }
                .subscribe({ responseModel -> liveEditProfileData.setValue(Resource.success(responseModel)) },
                    { throwable -> liveEditProfileData.setValue(Resource.error(throwable)) })
            )
        }
    }

    /**
     * CHECK BOX DATA
     */
    fun onCheckBoxClicked(isChecked: Boolean) {
        checkBoxChecked.value = isChecked

        if (checkBoxChecked.value == false) {
            binding!!.etCurrentPassword.setText("")
            binding!!.etNewPassword.setText("")
            binding!!.etConfirmPassword.setText("")
        }
    }
}