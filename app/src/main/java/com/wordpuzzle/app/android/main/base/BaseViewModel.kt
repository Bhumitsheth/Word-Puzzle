package com.wordpuzzle.app.android.main.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.utils.isOnline
import timber.log.Timber
import kotlin.reflect.KClass

open class BaseViewModel : ViewModel(), BaseContract.ViewModel {
    //    private val showHideProgressField = MutableLiveData(ProgressMetaData(title = null, isLoading = false))
    private val showHideProgressField = MutableLiveData<    Boolean>()
    private val showNoInternetConnectionField = LiveEvent()
    private val showErrorField = ErrorLiveEvent()

    // boolean = isTransparentStatus
    // if true, need to fill status bar by background color
    private val setStatusBarLightField = MutableLiveData<Boolean>()
    private val setStatusBarDarkField = MutableLiveData<Boolean>()
    private val showToastShortField = StringLiveEvent()
    private val showToastLongField = StringLiveEvent()

    private val showDialogLeftRightButtonsField = ShowDialogLiveEvent()
    private val showDialogRightButtonField = ShowDialogLiveEvent()

    private val startActivityForResultField = StartActivityForResultLiveEvent()
    private val startActivityField = StartActivityLiveEvent()
    private val finishField = LiveEvent()
    private val setResultField = SetResultLiveEvent()
    private val onBackPressedField = LiveEvent()

    private val replaceFragmentWithSaveStateFragmentField = FragmentLiveEvent()
    private val addFragmentWithBackStackField = FragmentLiveEvent()
    private val addFragmentWithBackStackForRealField = FragmentLiveEvent()
    private val addFragmentWithBackStackForRealFieldWithAnim = FragmentLiveEvent()
    private val replaceFragmentInActivityField = FragmentLiveEvent()

    private val hideBottomNavigationField = LiveEvent()
    private val hideBottomNavigationWithoutAnimField = LiveEvent()
    private val showBottomNavigationField = LiveEvent()

    private val addFragmentWithAnimField = FragmentLiveEvent()
    private val addFragmentWithAnimAndBackStackField = FragmentLiveEvent()
    private val addFragmentFromBottomField = FragmentLiveEvent()
    private val addFragmentFromBottomWithBackStackField = FragmentLiveEvent()
    private val replaceFragmentField = FragmentLiveEvent()
    private val replaceFragmentWithAnimField = FragmentLiveEvent()
    private val replaceFragmentWithAnimCloseField = FragmentLiveEvent()
    private val replaceFragmentWithBackStackField = FragmentLiveEvent()
    private val replaceFragmentWithOpenSlideField = FragmentLiveEvent()
    private val replaceFragmentWithCloseSlideField = FragmentLiveEvent()

    private val removeFromBackStackField = StringLiveEvent()
    private val popBackStackField = LiveEvent()
    private val popBackStackToFragmentField = StringLiveEvent()
    private val clearBackStackFragmentsField = LiveEvent()

    private val hideKeyboardField = LiveEvent()

    private val requestPermissionsField = RequestPermissionsLiveEvent()

    var argsFragment: Bundle? = null
    var argsActivity: Intent? = null

    open fun initArgsFragment(args: Bundle?) {
        this.argsFragment = args
    }

    open fun initArgsActivity(args: Intent?) {
        this.argsActivity = args
    }

    open fun setIntent(intent: Intent) {

    }

    open fun viewIsReady() {

    }

    open fun viewIsReady(args: Bundle?) {

    }

    open fun viewIsReady(intent: Intent) {

    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    open fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {}

    fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        requestPermissionsField.produce(permissions, requestCode)
    }

    fun getRequestPermissionsField() = requestPermissionsField

    fun setResult(result: Int, intent: Intent) {
        setResultField.produce(result, intent)
    }

    fun getSetResultField() = setResultField

    fun finish() {
        finishField.produce()
    }

    fun onBackPressed(){
        onBackPressedField.produce()
    }

    fun getFinishField() = finishField

    fun getOnBackPressedField() = onBackPressedField


    fun setStatusBarLight(isTransparentStatus: Boolean = false) {
        setStatusBarLightField.value = isTransparentStatus
    }

    fun getSetStatusBarLightField() = setStatusBarLightField

    fun setStatusBarDark(isTransparentStatus: Boolean = false) {
        setStatusBarDarkField.value = isTransparentStatus
    }

    fun getSetStatusBarDarkField() = setStatusBarDarkField

    fun showInternetConnectionError() {
        showNoInternetConnectionField.produce()
    }

    fun getShowNoInternetConnectionField() = showNoInternetConnectionField

    fun hideKeyboard() {
        hideKeyboardField.produce()
    }

    fun getHideKeyboardField() = hideKeyboardField

    fun showDialogOnlyRightButton(titleRes: Int, bodyRes: Int, rightButtonRes: Int? = null) {
        var rbTextRes = R.string.ok
        if (rightButtonRes != null) {
            rbTextRes = rightButtonRes
        }
        showDialogRightButtonField.produce(titleRes = titleRes, bodyRes = bodyRes, rightButtonTextRes = rbTextRes)
    }

    fun getShowDialogOnlyRightButtonField() = showDialogRightButtonField

    fun showDialogLeftAndRightButtons(
        titleRes: Int,
        bodyRes: Int,
        leftButtonRes: Int? = null,
        rightButtonRes: Int? = null
    ) {
        var rbTextRes = R.string.ok
        if (rightButtonRes != null) {
            rbTextRes = rightButtonRes
        }

        var lbTextRes = R.string.cancel
        if (leftButtonRes != null) {
            lbTextRes = leftButtonRes
        }
        showDialogLeftRightButtonsField.produce(
            titleRes = titleRes,
            bodyRes = bodyRes,
            leftButtonTextRes = lbTextRes,
            rightButtonTextRes = rbTextRes
        )
    }

    fun getShowDialogLeftRightButtonsField() = showDialogLeftRightButtonsField

    fun startActivity(clazz: KClass<*>, bundle: Bundle? = null) {
        startActivityField.produce(clazz, bundle)
    }

    fun getStartActivityField() = startActivityField

    fun startActivityForResult(clazz: KClass<*>, bundle: Bundle? = null, code: Int) {
        startActivityForResultField.produce(clazz, bundle, code)
    }

    fun getStartActivityForResultField() = startActivityForResultField

    override fun replaceFragmentWithSaveStateFragment(f: Fragment) {
        replaceFragmentWithSaveStateFragmentField.produce(f)
    }

    fun getReplaceFragmentWithSaveStateFragmentField() = replaceFragmentWithSaveStateFragmentField

    override fun replaceFragmentInActivity(fragment: Fragment) {
        replaceFragmentInActivityField.produce(fragment)
    }

    fun getReplaceFragmentInActivityField() = replaceFragmentInActivityField


    override fun addFragmentWithBackStack(f: Fragment) {
        addFragmentWithBackStackField.produce(f)
    }

    override fun addFragmentWithBackStackForReal(f: Fragment) {
        addFragmentWithBackStackForRealField.produce(f)
    }

    override fun addFragmentWithBackStackForRealWithAnim(f: Fragment) {
        addFragmentWithBackStackForRealFieldWithAnim.produce(f)
    }

    fun getAddFragmentWithBackStackField() = addFragmentWithBackStackField
    fun getAddFragmentWithBackStackForRealField() = addFragmentWithBackStackForRealField
    fun getAddFragmentWithBackStackForRealFieldWithAnim() = addFragmentWithBackStackForRealFieldWithAnim

    override fun addFragmentFromBottom(f: Fragment) {
        addFragmentFromBottomField.produce(f)
    }

    fun getAddFragmentFromBottomField() = addFragmentFromBottomField

    override fun addFragmentFromBottomWithBackStack(f: Fragment) {
        addFragmentFromBottomWithBackStackField.produce(f)
    }

    fun getAddFragmentFromBottomWithBackStackField() = addFragmentFromBottomWithBackStackField

    override fun replaceFragment(fragment: Fragment) {
        replaceFragmentField.produce(fragment)
    }

    fun getReplaceFragmentField() = replaceFragmentField

    override fun replaceFragmentWithAnim(fragment: Fragment) {
        replaceFragmentWithAnimField.produce(fragment)
    }

    fun getReplaceFragmentWithAnimField() = replaceFragmentWithAnimField

    override fun replaceFragmentWithAnimClose(fragment: Fragment) {
        replaceFragmentWithAnimCloseField.produce(fragment)
    }

    fun getReplaceFragmentWithAnimCloseField() = replaceFragmentWithAnimCloseField

    override fun replaceFragmentWithBackStack(fragment: Fragment) {
        replaceFragmentWithBackStackField.produce(fragment)
    }

    fun getReplaceFragmentWithBackStackField() = replaceFragmentWithBackStackField

    override fun replaceFragmentWithOpenSlide(fragment: Fragment) {
        replaceFragmentWithOpenSlideField.produce(fragment)
    }

    fun getReplaceFragmentWithOpenSlideField() = replaceFragmentWithOpenSlideField

    override fun replaceFragmentWithCloseSlide(fragment: Fragment) {
        replaceFragmentWithCloseSlideField.produce(fragment)
    }

    fun getReplaceFragmentWithCloseSlideField() = replaceFragmentWithCloseSlideField

    override fun removeFromBackStack(tag: String) {
        removeFromBackStackField.produce(tag)
    }

    override fun removeFromBackStack(fragment: Fragment) {
        removeFromBackStackField.produce(fragment::class.java.simpleName)
    }

    fun getRemoveFromBackStackField() = removeFromBackStackField

    override fun popBackStack() {
        popBackStackField.produce()
    }

    fun getPopBackStackField() = popBackStackField

    override fun popBackStackToFragment(tag: String) {
        popBackStackToFragmentField.produce(tag)
    }

    fun getPopBackStackToFragmentField() = popBackStackToFragmentField

    override fun clearBackStackFragments() {
        clearBackStackFragmentsField.produce()
    }

    fun getClearBackStackFragmentsField() = clearBackStackFragmentsField

    fun showToastLong(text: String) {
        showToastLongField.produce(text)
    }

    fun getShowToastLongField() = showToastLongField

    fun showToastShort(text: String) {
        showToastShortField.produce(text)
    }

    fun getShowToastShortField() = showToastShortField

    override fun hideBottomNavigation() {
        hideBottomNavigationField.produce()
    }

    fun getHideBottomNavigationField() = hideBottomNavigationField

    override fun hideBottomNavigationWithoutAnim() {
        hideBottomNavigationWithoutAnimField.produce()
    }

    fun getHideBottomNavigationWithoutAnimFieldField() = hideBottomNavigationWithoutAnimField


    override fun showBottomNavigation() {
        showBottomNavigationField.produce()
    }

    fun getShowBottomNavigationField() = showBottomNavigationField

    fun showError(
        title: String? = null,
        body: String? = null,
        exception: Throwable? = null,
        code: Int = -1
    ) {
        val err = AppError(title = title, body = body, ex = exception, code = code)
        showErrorField.produce(err)
    }

    fun getShowErrorField() = showErrorField

    fun showProgressBlocking() {
        showHideProgressField.value = true
    }

    fun hideProgressBlocking() {
        showHideProgressField.value = false
    }

    fun getShowHideProgressField() = showHideProgressField

    fun postDelayed(duration: Long = 100, callback: () -> Unit) {
        Handler().postDelayed(callback, duration)
    }

    fun isOnline(context: Context): Boolean {
        var isOnline = false
        try {
            isOnline = context.isOnline()
        } catch (ex: Throwable) {
            return isOnline
        }
        return isOnline
    }

//    fun handleServerError(context: Context,error: Throwable): String?{
//        val errorBody = (error as? HttpException)?.response()?.errorBody()?.string()
//        Timber.d("handleServerError " + error.message)
//
//        var errorCode =  AppConstants.ServerErrors.UNEXPECTED_ERROR.name
//        var errorMessageRaw = ""
//        try{
//            val jsonObject = JSONObject(errorBody)
//            if (jsonObject.has("message")) {
//                errorMessageRaw = jsonObject.optString("message", "")
//            }
//            errorCode = jsonObject.getString("errorCode")
////            errorMessageRaw = jsonObject.getString("message")
//        }
//        catch (e: java.lang.Exception){
//            AppConstants.ServerErrors.UNEXPECTED_ERROR
//        }
//
//        if (errorMessageRaw.isEmpty()) {
//            errorMessageRaw = context.getString(R.string.error_something_went_wrong)
//        }
//
//
//        val errorMessage = try{
//
//            when(AppConstants.ServerErrors.valueOf(errorCode)){
//                AppConstants.ServerErrors.EMAIL_TAKEN -> context.getString(R.string.the_email_is_already_taken)
//                AppConstants.ServerErrors.VALIDATION_ERROR -> context.getString(R.string.str_validation_error)
//                AppConstants.ServerErrors.WRONG_CREDENTIALS -> context.getString(R.string.wrong_credentials_provided)
//                AppConstants.ServerErrors.UNAUTHORIZED -> context.getString(R.string.the_request_is_unauthorized)
//                AppConstants.ServerErrors.PHONE_TAKEN -> context.getString(R.string.phone_taken)
//                AppConstants.ServerErrors.UNEXPECTED_ERROR -> context.getString(R.string.unexpected_error)
//                AppConstants.ServerErrors.NOT_FOUND_RESOURCE -> context.getString(R.string.not_found_resource)
//                AppConstants.ServerErrors.EMAIL_NOT_CONFIRMED -> context.getString(R.string.email_not_confirmed)
//                AppConstants.ServerErrors.CODE_INVALID -> context.getString(R.string.provided_code_is_invalid)
//                AppConstants.ServerErrors.CURRENT_PASSWORD_INVALID -> context.getString(R.string.provided_current_password_is_invalid)
//                AppConstants.ServerErrors.NOT_FOUND_SYMBOL -> context.getString(R.string.there_an_error_when_fetch_details_about_quote)
//                AppConstants.ServerErrors.MFA_CODE_INVALID -> context.getString(R.string.provided_mfa_code_is_invalid)
//                AppConstants.ServerErrors.IDENTITY_QUOTA_EXCEEDED -> context.getString(R.string.the_quota_exceeded)
//
//                AppConstants.ServerErrors.ACCOUNT_SUSPENDED -> errorMessageRaw
//                AppConstants.ServerErrors.RETAKE_NOT_PERMITTED -> if (errorMessageRaw.isNotEmpty()) errorMessageRaw else context.getString(
//                    R.string.retakeError
//                )
//                AppConstants.ServerErrors.QUESTION_RESOLVED -> if (errorMessageRaw.isNotEmpty()) errorMessageRaw else context.getString(
//                    R.string.alreadyAnsweredError
//                )
//            }
//        }catch (e: Exception){
//            error.message
//        }
//
//        return errorMessage
//    }

    override fun onCleared() {
        Timber.d(" !!! onCleared !!! viewModel ${this::class.java.simpleName} (${hashCode()})")
        super.onCleared()
    }
}