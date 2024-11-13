package com.wordpuzzle.app.android.main.ui.sign_up

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivitySignUpBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Objects

class SignUpViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository
) : BaseViewModel() {

    //binding object
    private var binding: ActivitySignUpBinding? = null
    @SuppressLint("StaticFieldLeak")
    private var activity: SignUpActivity? = null

    //Live data Object Object
    private val liveData = MutableLiveData<Resource<RegisterResponseModel?>>()

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivitySignUpBinding, activity: SignUpActivity) {
        this.binding = binding
        this.activity = activity

        this.binding!!.numberPicker.minValue = 1
        this.binding!!.numberPicker.maxValue = 99
        this.binding!!.numberPicker.value = 13
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveData(): MutableLiveData<Resource<RegisterResponseModel?>> {
        return liveData
    }

    /**
     * FREE BUTTON CLICK
     */
    fun ivFreeClick() {
        if (isValidInput()) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_paid_version)
            val continueBtn: ConstraintLayout = dialog.findViewById(R.id.cl_continue)
            val closeBtn: ImageView = dialog.findViewById(R.id.image_close)
            continueBtn.setOnClickListener {
                dialog.dismiss()
                getSignInFreeApiResponse()
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    /**
     * SIGN IN FREE API
     */
    private fun getSignInFreeApiResponse() {
        hideKeyboard()
        val name: String =
            Objects.requireNonNull(binding!!.etName.text).toString().trim { it <= ' ' }
        val email: String =
            Objects.requireNonNull(binding!!.etEmail.text).toString().trim { it <= ' ' }
        val age: Int = binding!!.numberPicker.value

        val params = HashMap<String, Any>()
        params[AppConstants.fullName] = name
        params[AppConstants.email] = email
        params[AppConstants.age] = age

        compositeDisposable.add(repository.registerUserAPI(params).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveData.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveData.setValue(Resource.success(responseModel)) },
                { throwable -> liveData.setValue(Resource.error(throwable)) }
            )
        )
    }

    /**
     * ALL INPUT FILL VALIDATION CHECK
     */
    private fun isValidInput(): Boolean {
        return if (FormValidation.checkEmptyView(
                binding!!.etName,
                binding!!.etName.resources.getString(R.string.nameIsRequire)
            )
        ) {
            binding!!.etName.requestFocus()
            false
        } else if (FormValidation.checkEmailView(
                binding!!.etEmail,
                binding!!.etEmail.resources.getString(R.string.emailAddressIsRequire),
                binding!!.etEmail.resources.getString(R.string.incorrectEmailAddress)
            )
        ) {
            binding!!.etEmail.requestFocus()
            false
        } else {
            true
        }
    }
}