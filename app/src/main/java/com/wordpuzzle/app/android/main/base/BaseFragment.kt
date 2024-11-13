package com.wordpuzzle.app.android.main.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.application.getApp
import com.wordpuzzle.app.android.di.implementor.DialogButtonClickListener
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.main.dialogs.AppDialog
import com.wordpuzzle.app.android.utils.StringValidation
import com.wordpuzzle.app.android.utils.hideKeyboard
import com.wordpuzzle.app.android.utils.showKeyboard
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.base.EventObserver
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Philipp Marchenko on 22.07.2020.
 * Copyright (c) 2020 mova.io. All rights reserved.
 */
abstract class BaseFragment<B: ViewDataBinding> : Fragment(), BaseContract.FragmentView {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    protected lateinit var binding: B
    private var baseViewModel: BaseViewModel? = null
    private var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var mRectInitial: Rect? = null
    private var mIsKeyboardOpened = false
    private var keyboardView: View? = null
    var TAG = ""
    abstract fun getLayoutId(): Int

    abstract fun viewCreated(savedInstanceState: Bundle?)

    abstract fun getBaseVm(): BaseViewModel?

    abstract fun initDependencies()

    fun releaseViewComponent(){
        activity?.let { a ->
            getApp(a.applicationContext)?.componentsHolder?.releaseViewComponent(this::class.java)
        }
    }

    open fun init(view: View, savedInstanceState: Bundle?) {

    }

    open fun onBackPressed(){
        activity?.onBackPressed()
    }

    override fun tagSimple(): String? {
        return this::class.java.simpleName
    }

    override fun removeMyself() {
        activity?.let { a ->
            Timber.d("tagSimple()? = ${tagSimple()}")
            tagSimple()?.let { t ->
                (a as BaseContract.FragmentControlAbility).removeFromBackStack(t)

            }
        }
    }

    override fun removeMyselfChild() {
        try {
            parentFragment?.childFragmentManager?.popBackStack(
                tagSimple(),
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        } catch (ex: Throwable) {
            Timber.e(ex)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        //     binding.executePendingBindings()
        init(binding.root, savedInstanceState)
        initDependencies()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        viewCreated(savedInstanceState)
        getBaseVm()?.let { vm ->
            initBaseViewModel(vm)
        }
    }

    fun initBaseViewModel(vm: BaseViewModel) {

        baseViewModel = vm
        initProgressObserver()
        initConnectionErrorObserver()
        initShowErrorObserver()
        initShowToastListener()
        initStatusBarListener()
        initFragmentAndActivityControls()
        initShowDialogRightButton()
        initShowDialogLeftRightButton()
        initFinish()
        initOnBackPressed()
        initSetResult()

        baseViewModel?.getHideKeyboardField()?.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
        })

        baseViewModel?.getRequestPermissionsField()?.observe(viewLifecycleOwner, EventObserver {
            tryTorequestPermissions(it.permissions, it.requestCode)
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        baseViewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Timber.d("onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        baseViewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun tryTorequestPermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (activity as AppCompatActivity).requestPermissions(permissions, requestCode)
        }
        else{
            requestPermissions(permissions, requestCode)
        }
    }



    fun setKeyboardStateListener(v: View, onShow: () -> Unit, onHide: () -> Unit) {
        keyboardView?.let { prevView ->
            removeKeyboardStateListener(prevView)
        }
        keyboardView = v
        mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            if (mRectInitial == null) {
                mRectInitial = Rect()
                v.getWindowVisibleDisplayFrame(mRectInitial)
                Timber.d("mRectInitial = ${mRectInitial?.bottom}")
            }
            val rect = Rect()
            v.getWindowVisibleDisplayFrame(rect)
            if (!mIsKeyboardOpened && rect.height() != mRectInitial!!.height()) {
                mIsKeyboardOpened = true
                onShow()

            } else if (mIsKeyboardOpened && rect.height() == mRectInitial!!.height()) {
                mIsKeyboardOpened = false
                onHide()
            }
        }
        v.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    fun removeKeyboardStateListener(v: View) {
        try {
            v.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    open fun initSetResult() {
        baseViewModel?.getSetResultField()?.observe(viewLifecycleOwner, EventObserver { setResultField ->
            activity?.setResult(setResultField.result, setResultField.intent)
        })
    }

    open fun initFinish() {
        baseViewModel?.getFinishField()?.observe(viewLifecycleOwner, EventObserver {
            activity?.finish()
        })
    }

    open fun initOnBackPressed() {
        baseViewModel?.getOnBackPressedField()?.observe(viewLifecycleOwner, EventObserver {
            activity?.onBackPressed()
        })
    }


    open fun initShowDialogRightButton() {
        baseViewModel?.getShowDialogOnlyRightButtonField()?.observe(viewLifecycleOwner, EventObserver { dialogInfo ->
            activity?.let { a ->
                AppDialog()
                    .title(getString(dialogInfo.titleRe))
                    .body(getString(dialogInfo.bodyRe))
                    .onlyRightButton()
                    .build(a)
                    .show()
            }
        })
    }

    var leftButtonDialogCallback = {}
    var rightButtonDialogCallback = {}

    open fun initShowDialogLeftRightButton() {
        baseViewModel?.getShowDialogLeftRightButtonsField()?.observe(viewLifecycleOwner, EventObserver { dialogInfo ->
            activity?.let { a ->
                AppDialog()
                    .title(getString(dialogInfo.titleRe))
                    .body(getString(dialogInfo.bodyRe))
                    .leftButtonCallback {
                        leftButtonDialogCallback()
                    }
                    .rightButtonCallback {
                        rightButtonDialogCallback()
                    }
                    .build(a)
                    .show()
            }

        })
    }

    open fun initFragmentAndActivityControls() {
        baseViewModel?.getStartActivityField()?.observe(viewLifecycleOwner, EventObserver {
            activity?.let { a ->
                val intent = Intent(a, it.clazz.java)
                it.bundle?.let {
                    intent.putExtras(it)
                }
                startActivity(intent)
            }
        })

        baseViewModel?.getStartActivityForResultField()?.observe(viewLifecycleOwner, EventObserver {
            activity?.let { a ->
                val intent = Intent(a, it.clazz.java)
                it.bundle?.let {
                    intent.putExtras(it)
                }
                val code = it.code ?: 0
                startActivityForResult(intent, code)
            }
        })

        baseViewModel?.getAddFragmentWithBackStackField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.addFragmentWithBackStack(it)
            }
        })


        baseViewModel?.getAddFragmentWithBackStackForRealFieldWithAnim()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.addFragmentWithBackStackForRealWithAnim(it)
            }
        })

        baseViewModel?.getReplaceFragmentWithSaveStateFragmentField()?.observe(viewLifecycleOwner, EventObserver {
            val a = if(parentFragment is BaseContract.FragmentControlAbility){
                parentFragment
            }
            else{
                parentFragment?.parentFragment
            }

//            Timber.d("getReplaceFragmentWithSaveStateFragmentField " + a!!::class.java.simpleName)


            if (a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithSaveStateFragment(it)
            }
        })

        baseViewModel?.getReplaceFragmentInActivityField()?.observe(viewLifecycleOwner, EventObserver {
            val a = activity
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentInActivity(it)
            }
        })

        baseViewModel?.getAddFragmentFromBottomField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.addFragmentFromBottom(it)
            }
        })
        baseViewModel?.getAddFragmentFromBottomWithBackStackField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.addFragmentFromBottomWithBackStack(it)
            }
        })
        baseViewModel?.getReplaceFragmentField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragment(it)
            }
        })
        baseViewModel?.getReplaceFragmentWithAnimField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithAnim(it)
            }
        })
        baseViewModel?.getReplaceFragmentWithAnimCloseField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithAnimClose(it)
            }
        })
        baseViewModel?.getReplaceFragmentWithBackStackField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithBackStack(it)
            }
        })
        baseViewModel?.getReplaceFragmentWithOpenSlideField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithOpenSlide(it)
            }
        })
        baseViewModel?.getReplaceFragmentWithCloseSlideField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.replaceFragmentWithCloseSlide(it)
            }
        })

        baseViewModel?.getShowBottomNavigationField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment?.parentFragment
            if (a != null && a is BaseContract.MainFragmentControl) {
                a.showBottomNavigation()
            }
        })

        baseViewModel?.getHideBottomNavigationField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment?.parentFragment
            if (a != null && a is BaseContract.MainFragmentControl) {
                a.hideBottomNavigation()
            }
        })

        baseViewModel?.getHideBottomNavigationWithoutAnimFieldField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment?.parentFragment
            if (a != null && a is BaseContract.MainFragmentControl) {
                a.hideBottomNavigationWithoutAnim()
            }
        })

        baseViewModel?.getPopBackStackToFragmentField()?.observe(viewLifecycleOwner, EventObserver {
            val a = parentFragment
            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.popBackStackToFragment(it)
            }
        })

        baseViewModel?.getPopBackStackField()?.observe(viewLifecycleOwner, EventObserver {
            val a = if(parentFragment is BaseContract.FragmentControlAbility){
                parentFragment
            }
            else{
                parentFragment?.parentFragment
            }
            Timber.d("getPopBackStackField " + a!!::class.java)

            if (a != null && a is BaseContract.FragmentControlAbility) {
                a.popBackStack()
            }
        })

    }

    open fun initStatusBarListener() {
        baseViewModel?.getSetStatusBarDarkField()?.observe(viewLifecycleOwner, Observer { isTransparent ->
            activity?.let { a ->
                if (a is BaseMainActivity<*>) {
                    a.setStatusBarDark(isTransparent)
                }
            }
        })

        baseViewModel?.getSetStatusBarLightField()?.observe(viewLifecycleOwner, Observer { isTransparent ->
            activity?.let { a ->
                if (a is BaseMainActivity<*>) {
                    a.setStatusBarLight(isTransparent)
                }
            }
        })
    }

    open fun initShowToastListener() {
        baseViewModel?.getShowToastLongField()?.observe(viewLifecycleOwner, EventObserver { text ->
            showToastLong(text)
        })

        baseViewModel?.getShowToastShortField()?.observe(viewLifecycleOwner, EventObserver { text ->
            showToastShort(text)
        })
    }

    open fun initShowErrorObserver() {
        baseViewModel?.getShowErrorField()?.observe(viewLifecycleOwner, EventObserver { muleError ->
            val a = activity
            if (a != null && a is BaseContract.MVVMView) {
                a.showError(muleError.title, muleError.body,  null)
            }
        })
    }

    open fun initConnectionErrorObserver() {
        baseViewModel?.getShowNoInternetConnectionField()?.observe(viewLifecycleOwner, EventObserver {
            val a = activity
            if (a != null && a is BaseContract.MVVMView) {
                a.showConnectionError(null)
            }
        })
    }

    open fun initProgressObserver() {
        baseViewModel?.getShowHideProgressField()?.observe(viewLifecycleOwner, Observer { isShowing ->
            if (isShowing) {
                val a = activity
                if (a != null && a is BaseContract.ActivityView) {
                    a.showProgressBlocking()
                }

            } else {
                val a = activity
                if (a != null && a is BaseContract.ActivityView) {
                    a.hideProgressBlocking()
                }
            }
        })
    }

    override fun showProgressBlocking() {
        Timber.d("hideProgressBlocking " + this::class.java.simpleName)

        val a = activity
        if (a != null && a is BaseMainActivity<*>) {
            a.showProgressBlocking()
        }
    }

    override fun hideProgressBlocking() {
        Timber.d("hideProgressBlocking " + this::class.java.simpleName)
        val a = activity
        if (a != null && a is BaseMainActivity<*>) {
            a.hideProgressBlocking()
        }
    }


    override fun onDestroyView() {
        keyboardView?.let { v ->
            removeKeyboardStateListener(v)
        }
        Timber.d("onDestroyView !!! -> ${this::class.java}}")
        val cls = this::class.java
        activity?.run {
            getApp(this)?.componentsHolder?.releaseViewComponent(cls)
        }
        super.onDestroyView()
    }

    override fun showToastLong(text: String) {
        val a = activity
        if (a != null && a is BaseContract.MVVMView) {
            a.showToastLong(text)
        }
    }

    override fun showToastShort(text: String) {
        val a = activity
        if (a != null && a is BaseContract.MVVMView) {
            a.showToastShort(text)
        }
    }

    fun showKeyboard() {
        view?.let { v ->
            showKeyboard(v)
        }
    }

    override fun showKeyboard(view: View) {
        context?.showKeyboard(view)
    }
    override fun showError(
        title: String?,
        body: String?,
        onOk: (() -> Unit)?
    ) {
        val a = activity
        if (a != null && a is BaseContract.MVVMView) {
            a.showError(title, body,  onOk)
        }
    }

//    override fun showInsufficientFundsAlert() {
//        showDialog(getString(R.string.str_insu_fun_title),
//            getString(R.string.str_insu_fun_descr),
//            getString(R.string.ok),
//            isCancelable = false
//        )
//    }
//
//    override fun showDeleteWatchListAlert(onOk: () -> Unit) {
//        showDialog(getString(R.string.delete_watchlist),
//            getString(R.string.delete_watchlist_text),
//            getString(R.string.delete),
//            getString(R.string.cancel),
//            btnOkColor = R.color.colorRed,
//            isCancelable = false,
//            onOk = onOk
//        )
//    }
//
//    override fun showWatchlistWasDeleted() {
//        showDialog(getString(R.string.success),
//            getString(R.string.watchlist_was_deleted),
//            getString(R.string.ok),
//            isCancelable = true
//        )
//    }

    override fun showDialog(
        title: String?,
        body: String?,
        btnOkText: String?,
        btCancelText: String?,
        btnOkColor: Int?,
        isOnlyRightButton: Boolean,
        isCancelable: Boolean,
        onCancel: (() -> Unit)?,
        onOk: (() -> Unit)?
    ) {
        val a = activity
        if (a != null && a is BaseContract.MVVMView) {
            a.showDialog(title, body, btnOkText,btCancelText,btnOkColor, isOnlyRightButton,isCancelable,{onCancel?.invoke()},{onOk?.invoke()})
        }
    }

    override fun showConnectionError(onOk: (() -> Unit)?) {
        val a = activity
        if (a != null && a is BaseContract.MVVMView) {
            a.showConnectionError(onOk)
        }
    }

    override fun hideKeyboard() {
        view?.let {
            context?.hideKeyboard(it)
        }
    }

    override fun finish() {
        activity?.finish()
    }

    override fun isViewAttached() = isAdded && (activity != null)

    protected fun getTextFromHtml(id: Int): Spanned {
        return Html.fromHtml(getString(id))
    }

    protected fun backClick() {
        activity?.onBackPressed()
    }

    fun postDelayed(duration: Long = 100, callback: () -> Unit) {
        Handler().postDelayed(callback, duration)
    }

    fun <T : ViewComponent<*>> getComponent(): T? {
        try {
            activity?.let { a ->
                return getApp(a)?.componentsHolder?.getViewComponent(this::class.java) as T?
            }
            return null
        } catch (ex: Throwable) {
            Timber.d(ex)
            return null
        }
    }

    fun <T : ViewComponent<*>> getComponentByClass(clazz: Class<*>): T? {
        Timber.d("clazz " + clazz)
        activity?.let { a ->
            return getApp(a)
                ?.componentsHolder
                ?.getViewComponent(clazz) as T?
        }
        return null
    }


//    fun isFragmentVisible(): Boolean{
//        return (parentFragment?.childFragmentManager?.fragments?.last() as BaseFragment<FragmentSplashBinding>)::class.java.simpleName ==  this::class.java.simpleName
//    }

//    override fun putResult(
//        resultCode: Int,
//        requestCode: Int,
//        data: Intent?
//    ) {
//        val countFragments = parentFragment?.childFragmentManager?.fragments?.size ?: 1
//        Timber.d("countFragments " + countFragments)
//        val prevFragment = (parentFragment?.childFragmentManager?.fragments?.get(countFragments - 2) as BaseFragment<FragmentSplashBinding>)
//        Timber.d("prevFragment " + prevFragment::class.java)
//
//        prevFragment.onFragmentResult(resultCode,requestCode,data)
//    }

    override fun onFragmentResult(resultCode: Int, requestCode: Int, data: Intent?) {

    }

    open fun showCommonDialogWithMessage(
        context: Context?, title: String?,
        message: String?, strPositive: String?, strNegative: String?,
        listener: DialogButtonClickListener
    ) {
        val builder =
            MaterialAlertDialogBuilder(context!!, R.style.themeOverlay_App_MaterialAlertDialog)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(
            strPositive
        ) { _: DialogInterface?, _: Int -> listener.onPositiveButtonClick() }
        if (!StringValidation.isStringEmpty(strNegative)) builder.setNegativeButton(
            strNegative
        ) { _: DialogInterface?, _: Int -> listener.onNegativeButtonClick() }
        builder.show()
    }
}