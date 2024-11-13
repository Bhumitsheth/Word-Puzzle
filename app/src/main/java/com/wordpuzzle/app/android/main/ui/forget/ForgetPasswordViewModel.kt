package com.wordpuzzle.app.android.main.ui.forget

import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityForgetPasswordBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.FormValidation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Objects


class ForgetPasswordViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository
) : BaseViewModel() {

    //binding object
    private var binding: ActivityForgetPasswordBinding? = null
    private var activity: ForgetPasswordActivity? = null

    //Live data Object Object
    private val liveForgotPasswordData = MutableLiveData<Resource<ForgotPasswordResponseModel?>>()

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityForgetPasswordBinding, activity: ForgetPasswordActivity) {
        this.binding = binding
        this.activity = activity
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveForgotPasswordData(): MutableLiveData<Resource<ForgotPasswordResponseModel?>> {
        return liveForgotPasswordData
    }

    /**
     * FORGOT PASSWORD API
     */
    fun tvSend() {
        hideKeyboard()
        if (isValidInput()) {
            val email: String = Objects.requireNonNull(binding!!.etEmail.text).toString().trim { it <= ' ' }

            val params = HashMap<String, Any>()
            params[AppConstants.email] = email

            compositeDisposable.add(repository.forgotPasswordAPI(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> liveForgotPasswordData.setValue(Resource.loading()) }
                .subscribe({ responseModel ->
                    liveForgotPasswordData.setValue(
                        Resource.success(
                            responseModel
                        )
                    )
                },
                    { throwable -> liveForgotPasswordData.setValue(Resource.error(throwable)) }
                )
            )
        }
    }

    /**
     * INOUT TYPE VALIDATION
     */
    private fun isValidInput(): Boolean {
        return if (FormValidation.checkEmptyView(
                binding!!.etEmail,
                binding!!.etEmail.resources.getString(R.string.emailAddressIsRequire)
            )
        ) {
            binding!!.etEmail.requestFocus()
            false
        } else {
            true
        }
    }
}