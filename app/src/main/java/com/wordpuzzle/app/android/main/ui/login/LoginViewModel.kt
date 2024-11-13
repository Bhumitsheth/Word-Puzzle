package com.wordpuzzle.app.android.main.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLoginBinding
import com.wordpuzzle.app.android.main.base.BaseEventHandler
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.RegisterResponseModel
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.FormValidation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Objects

class LoginViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository
) : BaseViewModel() {
    //binding object
    private var binding: ActivityLoginBinding? = null
    private var activity: LoginActivity? = null

    //Live data Object Object
    private val liveData = MutableLiveData<Resource<RegisterResponseModel?>>()
    val isShowPopup: MutableLiveData<Boolean> = MutableLiveData(false)

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityLoginBinding, activity: LoginActivity, intent: Intent) {
        this.binding = binding
        this.activity = activity

        if (intent.hasExtra(AppConstants.isShowPopup)) {
            isShowPopup.value=intent.getBooleanExtra(AppConstants.isShowPopup,false)
            displayPopup()
        }
    }

    private fun displayPopup() {
        if (isShowPopup.value!!){
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_generate_password_info)

            val clOk = dialog.findViewById(R.id.clOk) as ConstraintLayout

            clOk.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }
    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveData(): MutableLiveData<Resource<RegisterResponseModel?>> {
        return liveData
    }

    /**
     * LOGIN
     */
    fun tvLogin() {
        hideKeyboard()
        if (isValidInput()) {
            val email: String = Objects.requireNonNull(binding!!.etEmail.text).toString().trim { it <= ' ' }
            val password: String = Objects.requireNonNull(binding!!.etPassword.text).toString().trim { it <= ' ' }

            val params = HashMap<String, Any>()
            params[AppConstants.email] = email
            params[AppConstants.password] = password

            compositeDisposable.add(repository.loginUserAPI(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> liveData.setValue(Resource.loading()) }
                .subscribe({ responseModel -> liveData.setValue(Resource.success(responseModel)) },
                    { throwable -> liveData.setValue(Resource.error(throwable)) }
                )
            )
        }
    }

    /**
     * ALL INPUT FILL VALIDATION CHECK
     */
    private fun isValidInput(): Boolean {
        return if (FormValidation.checkEmailView(
                binding!!.etEmail,
                binding!!.etEmail.resources.getString(R.string.emailAddressIsRequire),
                binding!!.etEmail.resources.getString(R.string.incorrectEmailAddress))) {
            binding!!.etEmail.requestFocus()
            false
        }
        else if (FormValidation.checkEmptyView(
                binding!!.etPassword,
                binding!!.etPassword.resources.getString(R.string.passwordIsRequire))) {
            binding!!.etPassword.requestFocus()
            false
        }
        else {
            true
        }
    }
}