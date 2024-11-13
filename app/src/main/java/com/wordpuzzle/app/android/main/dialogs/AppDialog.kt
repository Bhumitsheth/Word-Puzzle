package com.wordpuzzle.app.android.main.dialogs

import android.app.Dialog
import android.content.Context
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.main.dialogs.base.BaseDialog
import timber.log.Timber

class AppDialog : BaseDialog() {

    private var title: String? = null
    private var body: String? = null
    private var rightButtonText: String? = null
    private var leftButtonText: String? = null
    private var rightCallback: (() -> Unit)? = {}
    private var rightCallbackWithEditText: ((String) -> Unit)? = {}
    private var leftCallback: (() -> Unit)? = {}
    private var isCancelable: Boolean = false
    private var dismissAfterClick: Boolean = true
    private var withEditText: Boolean = false
    private var btnOkColor: Int? = null

    fun title(t: String?) = apply { title = t }
    fun body(b: String?) = apply { body = b }
    fun rightButtonText(txt: String?) = apply { rightButtonText = txt }
    fun leftButtonText(txt: String?) = apply { leftButtonText = txt }
    fun rightButtonCallback(call: (() -> Unit)) = apply { rightCallback = call }
    fun rightButtonCallbackWithEditText(call: ((String) -> Unit)) = apply { rightCallbackWithEditText = call }
    fun btnOkColor(int: Int?) = apply { btnOkColor = int }

    fun leftButtonCallback(call: (() -> Unit)) = apply { leftCallback = call }
    fun onlyRightButton() = apply { leftCallback = null }
    fun isCancelable(c: Boolean) = apply { isCancelable = c }
    fun doNotDismissWhenClick() = apply { dismissAfterClick = false }
    fun doWithEditText() = apply { withEditText = true }

    fun build(activity: Context): Dialog {

        if (rightButtonText == null || rightButtonText?.length == 0) {
            rightButtonText = activity.getString(R.string.ok)
        }

        if(btnOkColor == null){
            btnOkColor = R.color.colorDark
        }

        return buildDialog(activity)
    }

    private fun buildDialog(activityContext: Context): Dialog {

        val dialog = getDialog(activityContext, R.layout.dialog_base_new, isCancelable)

        if ((title == null || title!!.isEmpty()) && (body == null || body!!.isEmpty())) {
//            dialog.tv_title.gone()
//            dialog.tv_body.text = activityContext.getString(R.string.error_something_went_wrong)
//            dialog.tv_body.visible()

        } else if (title == null || title!!.isEmpty()) {
//            dialog.tv_title.gone()

        } else if (body == null || body!!.isEmpty()) {
//            dialog.tv_body.gone()
        }

        title?.let { t ->
//            dialog.tv_title.text = t
        }

        body?.let { b ->
//            dialog.tv_body.text = b
        }

//        dialog.tv_btn_left_text.setOnClickListener {
//            leftCallback?.invoke()
//
//            if (dismissAfterClick) {
//                dialog.dismiss()
//            }
//        }

//        dialog.tv_btn_right_text.setOnClickListener {
//            rightCallback?.invoke()
//
//
//            if (dismissAfterClick) {
//                dialog.dismiss()
//            }
//        }

        rightButtonText?.let { t ->
//            dialog.tv_btn_right_text.text = t
        }
        Timber.d("leftButtonText " + leftButtonText)
        if(leftButtonText.isNullOrEmpty()){
//            dialog.tv_btn_left_text.gone()
        }
        else{
            leftButtonText?.let { t ->
//                dialog.tv_btn_left_text.text = t
            }
//            dialog.tv_btn_left_text.visible()

        }

     //   btnOkColor?.let { ContextCompat.getColor(activityContext, it) }?.let { dialog.tv_btn_right_text.setTextColor(it) }

        return dialog
    }
}