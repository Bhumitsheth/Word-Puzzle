package com.wordpuzzle.app.android.main.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.application.getApp
import com.wordpuzzle.app.android.di.implementor.DialogButtonClickListener
import com.wordpuzzle.app.android.di.module.ViewComponent
import com.wordpuzzle.app.android.main.dialogs.AppDialog
import com.wordpuzzle.app.android.main.dialogs.ProgressDialog
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.utils.*
import timber.log.Timber
import javax.inject.Inject

abstract class BaseMainActivity<B : ViewDataBinding> : AppCompatActivity(),
    BaseContract.ActivityView {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var binding: B
    private var baseViewModel: BaseViewModel? = null
    private var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var mRectInitial: Rect? = null
    private var mIsKeyboardOpened = false
    private var keyboardView: View? = null
    private var isStatusbarLight = false
    private var dialogProgress: Dialog? = null
    var firebaseToken = ""
    private var appPrefs: AppPrefs? = null
    lateinit var storeSingletonData: StoreSingletonData

    abstract fun getLayoutId(): Int

    abstract fun create(savedInstanceState: Bundle?)

    abstract fun getBaseVm(): BaseViewModel?

    abstract fun initDependencies()

    open fun getStatusBarColor() = R.color.white

    fun releaseViewComponent() {
        this.let { a ->
            getApp(a.applicationContext)?.componentsHolder?.releaseViewComponent(this::class.java)
        }
    }

    open fun init(view: View, savedInstanceState: Bundle?) {

    }

    fun tagSimple(): String? {
        return this::class.java.simpleName
    }

    fun removeMyself() {
        this?.let { a ->
            Timber.d("tagSimple()? = ${tagSimple()}")
            tagSimple()?.let { t ->
                (a as BaseContract.FragmentControlAbility).removeFromBackStack(t)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        initDependencies()
        hideKeyboard()
        create(savedInstanceState)
        getBaseVm()?.let { vm ->
            initBaseViewModel(vm)
        }
        ViewUtil.hideStatusBar(this)
        storeSingletonData = StoreSingletonData
//        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding.lifecycleOwner = this
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

        baseViewModel?.getHideKeyboardField()?.observe(this, EventObserver {
            hideKeyboard()
        })

        baseViewModel?.getRequestPermissionsField()?.observe(this, EventObserver {
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
            (this as AppCompatActivity).requestPermissions(permissions, requestCode)
        } else {
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
        baseViewModel?.getSetResultField()?.observe(this, EventObserver { setResultField ->
            setResult(setResultField.result, setResultField.intent)
        })
    }

    open fun initFinish() {
        baseViewModel?.getFinishField()?.observe(this, EventObserver {
            finish()
        })
    }

    open fun initOnBackPressed() {
        baseViewModel?.getOnBackPressedField()?.observe(this, EventObserver {
            onBackPressed()
        })
    }

    open fun initShowDialogRightButton() {
        baseViewModel?.getShowDialogOnlyRightButtonField()
            ?.observe(this, EventObserver { dialogInfo ->
                let { a ->
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
        baseViewModel?.getShowDialogLeftRightButtonsField()
            ?.observe(this, EventObserver { dialogInfo ->
                AppDialog()
                    .title(getString(dialogInfo.titleRe))
                    .body(getString(dialogInfo.bodyRe))
                    .leftButtonCallback {
                        leftButtonDialogCallback()
                    }
                    .rightButtonCallback {
                        rightButtonDialogCallback()
                    }
                    .build(this)
                    .show()

            })
    }

    open fun initFragmentAndActivityControls() {
        baseViewModel?.getStartActivityField()?.observe(this, EventObserver {
            let { a ->
                val intent = Intent(a, it.clazz.java)
                it.bundle?.let {
                    intent.putExtras(it)
                }
                startActivity(intent)
            }
        })

        baseViewModel?.getStartActivityForResultField()?.observe(this, EventObserver {
            let { a ->
                val intent = Intent(a, it.clazz.java)
                it.bundle?.let {
                    intent.putExtras(it)
                }
                val code = it.code ?: 0
                startActivityForResult(intent, code)
            }
        })
    }

    open fun initStatusBarListener() {
        baseViewModel?.getSetStatusBarDarkField()?.observe(this, Observer { isTransparent ->
            let { a ->
                if (a is BaseMainActivity) {
                    a.setStatusBarDark(isTransparent)
                }
            }
        })

        baseViewModel?.getSetStatusBarLightField()?.observe(this, Observer { isTransparent ->
            let { a ->
                if (a is BaseMainActivity) {
                    a.setStatusBarLight(isTransparent)
                }
            }
        })
    }

    open fun initShowToastListener() {
        baseViewModel?.getShowToastLongField()?.observe(this, EventObserver { text ->
            showToastLong(text)
        })

        baseViewModel?.getShowToastShortField()?.observe(this, EventObserver { text ->
            showToastShort(text)
        })
    }

    open fun initShowErrorObserver() {
        baseViewModel?.getShowErrorField()?.observe(this, EventObserver { muleError ->
            val a = this
            if (a != null && a is BaseContract.MVVMView) {
                a.showError(muleError.title, muleError.body, null)
            }
        })
    }

    open fun initConnectionErrorObserver() {
        baseViewModel?.getShowNoInternetConnectionField()?.observe(this, EventObserver {
            val a = this
            if (a != null && a is BaseContract.MVVMView) {
                a.showConnectionError(null)
            }
        })
    }

    open fun initProgressObserver() {
        baseViewModel?.getShowHideProgressField()?.observe(this, Observer { isShowing ->
            if (isShowing) {
                showProgressBlocking()
            } else {
                hideProgressBlocking()
            }
        })
    }

    override fun showProgressBlocking() {
        showProgressForSure()
    }

    //  private fun showProgressForSure(title: String) {
    private fun showProgressForSure() {
        try {
            Timber.d("showProgressForSure " + dialogProgress.hashCode())
            if (dialogProgress == null) {
                dialogProgress = ProgressDialog().build(this)
                dialogProgress?.setCancelable(true)
                dialogProgress?.show()
            }
        } catch (e: Throwable) {
            Timber.d("fatal -> showProgressForSure error (${e.message}) -> ")
        }
    }

    override fun hideProgressBlocking() {
        Timber.d("hideProgressBlocking " + dialogProgress.hashCode())
        dialogProgress?.dismiss()
        dialogProgress = null
    }

    override fun onDestroy() {
        keyboardView?.let { v ->
            removeKeyboardStateListener(v)
        }
        Timber.d("onDestroy !!! -> ${this::class.java.simpleName} (${hashCode()})")
        getApp(this)?.componentsHolder?.releaseViewComponent(this::class.java)
        super.onDestroy()
    }

    override fun showToastLong(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    override fun showToastShort(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun showKeyboard(view: View) {
        showKeyboard(view)
    }

    override fun showError(
        title: String?,
        body: String?,
        onOk: (() -> Unit)?
    ) {
        val a = this
        if (a != null && a is BaseContract.MVVMView) {
            a.showError(title, body, onOk)
        }
    }

    fun showInsufficientFundsAlert() {
//        showDialog(
//            getString(R.string.str_insu_fun_title),
//            getString(R.string.str_insu_fun_descr),
//            getString(R.string.ok),
//            isCancelable = false
//        )
    }

    fun showDeleteWatchListAlert(onOk: () -> Unit) {
//        showDialog(
//            getString(R.string.delete_watchlist),
//            getString(R.string.delete_watchlist_text),
//            getString(R.string.delete),
//            getString(R.string.cancel),
//            btnOkColor = R.color.colorRed,
//            isCancelable = false,
//            onOk = onOk
//        )
    }

    fun showConfirmationDialog(
        context: Context, listener: DialogButtonClickListener?,
        strMsg: String?, strPositive: String?, strNegative: String?
    ) {
        val builder = AlertDialog.Builder(
            context, R.style.MyAlertDialogStyle
        )
        builder.setTitle(context.resources.getString(R.string.app_name))
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage(strMsg)
        builder.setPositiveButton(
            strPositive
        ) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            listener?.onPositiveButtonClick()
        }
        builder.setNegativeButton(
            strNegative
        ) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            listener?.onNegativeButtonClick()
        }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.show()
    }

    override fun showWatchlistWasDeleted() {
        showDialog(getString(R.string.logout),
            getString(R.string.areYouSureYouWantToLeave),
            getString(R.string.yes),
            getString(R.string.no),
            isCancelable = false,
            onOk = {

            },
            onCancel = {

            }
        )
    }

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
        val a = this
        if (a != null && a is BaseContract.MVVMView) {
            a.showDialog(
                title,
                body,
                btnOkText,
                btCancelText,
                btnOkColor,
                isOnlyRightButton,
                isCancelable,
                { onCancel?.invoke() },
                { onOk?.invoke() })
        }
    }

    override fun showConnectionError(onOk: (() -> Unit)?) {
        val a = this
        if (a != null && a is BaseContract.MVVMView) {
            a.showConnectionError(onOk)
        }
    }

    override fun hideKeyboard() {
        try {
            applicationContext.hideKeyboard(window.decorView)
        } catch (ex: Throwable) {
            Timber.d(ex)
        }
    }

//    fun isViewAttached() = isAdded && (this != null)

    protected fun getTextFromHtml(id: Int): Spanned {
        return Html.fromHtml(getString(id))
    }

    protected fun backClick() {
        onBackPressed()
    }

    fun postDelayed(duration: Long = 100, callback: () -> Unit) {
        Handler().postDelayed(callback, duration)
    }

    fun <T : ViewComponent<*>> getComponent(): T? {
        try {
            let { a ->
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
        let { a ->
            return getApp(a)
                ?.componentsHolder
                ?.getViewComponent(clazz) as T?
        }
        return null
    }


//    fun isFragmentVisible(): Boolean{
//        return (parentFragment?.childFragmentManager?.fragments?.last() as BaseFragment<FragmentSplashBinding>)::class.java.simpleName ==  this::class.java.simpleName
//    }

    fun putResult(
        resultCode: Int,
        requestCode: Int,
        data: Intent?
    ) {
//        val countFragments = parentFragment?.childFragmentManager?.fragments?.size ?: 1
//        Timber.d("countFragments " + countFragments)
//        val prevFragment = (parentFragment?.childFragmentManager?.fragments?.get(countFragments - 2) as BaseFragment<FragmentSplashBinding>)
//        Timber.d("prevFragment " + prevFragment::class.java)

//        prevFragment.onFragmentResult(resultCode,requestCode,data)
    }

    fun onFragmentResult(resultCode: Int, requestCode: Int, data: Intent?) {

    }

    override fun setFullSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
    }

    override fun setStatusBarTranslucent(isTransparentStatus: Boolean) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (isTransparentStatus) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }

    override fun setStatusBarTransparent() {
        val callback: (bits: Int, on: Boolean) -> Unit = { bits, on ->
            val win = this.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }

        if (Build.VERSION.SDK_INT in 19..20) {
            callback(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            this.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            callback(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            this.window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun showStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        setStatusBarLight()
    }

    override fun setStatusBarLight(isTransparentStatus: Boolean) {
        Timber.d("setStatusBarLight")
        isStatusbarLight = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ViewUtil.setSystemBarTheme(this, false)
            if (!isTransparentStatus) {
                this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhiteTransparent)
        }
        setStatusBarTranslucent(isTransparentStatus)
    }

    override fun setStatusBarDark(isTransparentStatus: Boolean) {
        Timber.d("setStatusBarDark")
        isStatusbarLight = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ViewUtil.setSystemBarTheme(this, true)
            if (!isTransparentStatus) {
                this.window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhiteTransparent)
        }

        setStatusBarTranslucent(isTransparentStatus)
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // ViewUtil.setSystemBarTheme(this, true)
            this.window.statusBarColor = ContextCompat.getColor(this, color)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhiteTransparent)
        }
    }

    override fun isStatusLight(): Boolean? {
        return isStatusbarLight
    }

    open fun startWithClearStack(activity: Class<out Activity?>?) {
        startActivity(
            Intent(
                this,
                activity
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    override fun setStatusBarGradiant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val window: Window = this.window
//            val background =ContextCompat.getDrawable(this, R.drawable.app_background)
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//
//            window.statusBarColor = ContextCompat.getColor(this,android.R.color.transparent)
//            window.navigationBarColor = ContextCompat.getColor(this,android.R.color.transparent)
//            window.setBackgroundDrawable(background)
        }
    }

    fun startActivity(activity: Class<out Activity>) {
        startActivity(Intent(this, activity))
    }

    fun startActivityWithClearStack(activity: Class<out Activity>) {
        startActivity(Intent(this, activity))
        finish()
    }

    fun startWithClearStack1(activity: Class<out Activity>) {
        val intent = Intent(this, activity).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
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