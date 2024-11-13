package com.wordpuzzle.app.android.main.base

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment

interface BaseContract {

    interface MVVMView {
        fun showToastLong(text: String)
        fun showToastShort(text: String)
        fun showError(
            title: String? = null,
            body: String? = null,
            onOk: (() -> Unit)? = null
        )

        fun showDialog(
            title: String?,
            body: String?,
            btnOkText: String? = null,
            btCancelText: String? = null,
            btnOkColor: Int? = null,
            isOnlyRightButton: Boolean = false,
            isCancelable: Boolean = true,
            onCancel: (() -> Unit)? = null,
            onOk: (() -> Unit)? = null
        )

        fun showConnectionError(onOk: (() -> Unit)? = null)
        fun showKeyboard(view: View)
        fun hideKeyboard()
        fun finish()
        fun showProgressBlocking()
        fun hideProgressBlocking()
        fun tryTorequestPermissions(permissions: Array<String>, requestCode: Int)
    }

    interface FragmentControlAbility {
        fun replaceFragmentWithSaveStateFragment(f: Fragment)
        fun addFragmentWithBackStack(f: Fragment)
        fun addFragmentWithBackStackForReal(f: Fragment)
        fun addFragmentWithBackStackForRealWithAnim(f: Fragment)


        fun addFragmentFromBottom(f: Fragment)
        fun addFragmentFromBottomWithBackStack(f: Fragment)

        fun replaceFragmentInActivity(fragment: Fragment)
        fun replaceFragment(fragment: Fragment)
        fun replaceFragmentWithAnim(fragment: Fragment)
        fun replaceFragmentWithAnimClose(fragment: Fragment)
        fun replaceFragmentWithBackStack(fragment: Fragment)

        fun replaceFragmentWithOpenSlide(fragment: Fragment)
        fun replaceFragmentWithCloseSlide(fragment: Fragment)

        fun removeFromBackStack(tag: String)
        fun removeFromBackStack(fragment: Fragment)
        fun popBackStack()
        fun popBackStackToFragment(tag: String)

        fun clearBackStackFragments()

    }

    interface MainFragmentControl {
        fun hideBottomNavigation()
        fun hideBottomNavigationWithoutAnim()
        fun showBottomNavigation()

    }

    interface ActivityView : MVVMView {
        fun setFullSize()
        fun setStatusBarTranslucent(isTransparentStatus: Boolean)
        fun setStatusBarTransparent()
        fun hideStatusBar()
        fun showStatusBar()
        fun setStatusBarLight(isTransparentStatus: Boolean = false)
        fun setStatusBarDark(isTransparentStatus: Boolean = false)
        fun setStatusBarColor(color: Int)
        fun isStatusLight(): Boolean?
        fun setStatusBarGradiant()
        fun showWatchlistWasDeleted()
    }

    interface ViewModel : FragmentControlAbility, MainFragmentControl {

    }

    interface FragmentActivityView :
        ActivityView, FragmentControlAbility {
        fun getCurrentFragment(): Fragment?
        fun getFragmentByTag(tag: String): Fragment?
    }

    interface FragmentView : MVVMView {
        fun isViewAttached(): Boolean
        fun onFragmentResult(resultCode: Int, requestCode: Int, data: Intent?)
        fun tagSimple(): String?
        fun removeMyself()
        fun removeMyselfChild()
    }

    interface BottomTwoButtonClick {
        fun onLeftClicked()
        fun onRightClicked()
    }
}