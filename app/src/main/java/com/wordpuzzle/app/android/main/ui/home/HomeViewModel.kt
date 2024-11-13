package com.wordpuzzle.app.android.main.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.checkbox.MaterialCheckBox
import com.wordpuzzle.app.android.BuildConfig
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityHomeBinding
import com.wordpuzzle.app.android.di.implementor.RecyclerViewItemClickListener
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.about_us.AboutUsActivity
import com.wordpuzzle.app.android.main.ui.home.adapter.NavigationDrawerAdapter
import com.wordpuzzle.app.android.main.ui.leader_board.LeaderBoardActivity
import com.wordpuzzle.app.android.main.ui.profile.ProfileActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.SelectBookPdfActivity
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.utils.openLinkInBrowser
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.NavigationDrawerViewDataModel
import com.wordpuzzle.app.android.service.model.response.GetProfileDataModel
import com.wordpuzzle.app.android.service.model.response.GetProfileResponseModel
import com.wordpuzzle.app.android.service.model.response.LogoutResponseModel
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository

) : BaseViewModel() {
    //binding object
    private var binding: ActivityHomeBinding? = null
    private var activity: HomeActivity? = null

    //Live data Object
    var userProfile = MutableLiveData<GetProfileDataModel>()
    var isCheckTick = MutableLiveData<Boolean>()
    var profileImage: MutableLiveData<String> = MutableLiveData<String>("")
    private var isSoundCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private var isMusicCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isShowPopup: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var list: MutableLiveData<List<NavigationDrawerViewDataModel>> = MutableLiveData<List<NavigationDrawerViewDataModel>>()

    //Adapter
    var adapter: NavigationDrawerAdapter? = null

    //Api Object
    private var compositeDisposable = CompositeDisposable()
    private val liveData = MutableLiveData<Resource<LogoutResponseModel?>>()
    private val liveGetProfileData = MutableLiveData<Resource<GetProfileResponseModel?>>()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityHomeBinding, activity: HomeActivity) {
        this.binding = binding
        this.activity = activity

        setDrawerData()
        getUserProfile()
    }
    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveData(): MutableLiveData<Resource<LogoutResponseModel?>> {
        return liveData
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveGetProfileData(): MutableLiveData<Resource<GetProfileResponseModel?>> {
        return liveGetProfileData
    }

    /**
     * SET DRAWER DATA
     */
private fun setDrawerData() {
        list.value = setNavigationDrawerDataModel()
        adapter = NavigationDrawerAdapter(list.value!!)
        this.binding!!.navigationDrawerLayout.rvNavigationDrawer.adapter = adapter
        adapter!!.setRecyclerViewItemClickListener(object : RecyclerViewItemClickListener {
            override fun onItemClick(position: Int, flag: Int, view: View) {
                try {
                    closeDrawerMenu()
                    when (list.value!![position].id) {
//                        1 -> activity!!.startActivity(Intent(activity, ProfileActivity::class.java))
                        1 -> activity!!.resultLauncher.launch(Intent(activity, ProfileActivity::class.java))
                        2 -> activity!!.startActivity(Intent(activity, LeaderBoardActivity::class.java))
                        3 -> activity!!.startActivity(Intent(activity, SelectBookPdfActivity::class.java))
                        4 -> activity!!.startActivity(Intent(activity, AboutUsActivity::class.java))
                        5 -> openLinkInBrowser(BuildConfig.howToPlayGuideLines, activity!!)
                        6 -> logOut()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * SET NAVIGATION DRAWER DATA
     */
    private fun setNavigationDrawerDataModel(): MutableList<NavigationDrawerViewDataModel> {
        val listNavigationDrawerViewDataModel: MutableList<NavigationDrawerViewDataModel> = ArrayList<NavigationDrawerViewDataModel>()
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(1, R.drawable.ic_profile, binding!!.root.context.getString(R.string.profile)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(2, R.drawable.ic_leader_board, binding!!.root.context.getString(R.string.leaderBoard)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(3, R.drawable.ic_page_book_selection, binding!!.root.context.getString(R.string.pageBookSelection)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(4, R.drawable.ic_about, binding!!.root.context.getString(R.string.about)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(5, R.drawable.ic_help, binding!!.root.context.getString(R.string.howToPlay)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(6, R.drawable.ic_logout, binding!!.root.context.getString(R.string.logout)))
        listNavigationDrawerViewDataModel.add(NavigationDrawerViewDataModel(7, R.drawable.ic_delete_account, binding!!.root.context.getString(R.string.deleteAccount)))
        return listNavigationDrawerViewDataModel
    }

    /**
     *  CLOSED DRAWER MENU
     */
    private fun closeDrawerMenu() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding!!.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * LOGOUT DIALOG
     */
    fun logOut() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_logout)

        val checkBox = dialog.findViewById(R.id.checkbox) as MaterialCheckBox
        val tvQuestion = dialog.findViewById(R.id.tvQuestion) as TextView
        val tvSubmit = dialog.findViewById(R.id.tvSubmit) as TextView
        val yesBtn = dialog.findViewById(R.id.constraintFreeLayout_logout) as ConstraintLayout
        val closeBtn = dialog.findViewById(R.id.image_close) as ImageView

        yesBtn.setOnClickListener {
            dialog.dismiss()
            isShowPopup.value=true
            isCheckTick.value = if (checkBox.isChecked) true else false
            val params = HashMap<String, Any>()
            params[AppConstants.generatePassword] = if (checkBox.isChecked) true else false
            params[AppConstants.alredyGeneratePassword] = userProfile.value?.alreadyHavePassword!!
            getLogoutApi(params)
        }
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        if (userProfile.value?.alreadyHavePassword!!) {
            checkBox.visibility = View.GONE
            tvSubmit.text = activity!!.resources.getString(R.string.yes)
            tvQuestion.text = activity!!.resources.getString(R.string.areYouSureYouWantToLeave)
            yesBtn.setOnClickListener {
                dialog.dismiss()
                isShowPopup.value=false
                isCheckTick.value = true
                val params = HashMap<String, Any>()
                params[AppConstants.generatePassword] = false
                params[AppConstants.alredyGeneratePassword] = userProfile.value?.alreadyHavePassword!!
                getLogoutApi(params)
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    /**
     * SETTINGS CLICK POPUP DIALOG
     */
    fun clSettingsClick() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_settings)

        val closeBtn = dialog.findViewById<ImageView>(R.id.image_close)
        val ivSoundOnOff = dialog.findViewById<ImageView>(R.id.ivSoundOnOff)
        val ivMusicOnOff = dialog.findViewById<ImageView>(R.id.ivMusicOnOff)

        isSoundCheck.value = appPrefs.getBoolean(AppPrefs.isSoundOnOff)
        isMusicCheck.value = appPrefs.getBoolean(AppPrefs.isMusicOnOff)

        ivSoundOnOff.setImageResource(if (appPrefs.getBoolean(AppPrefs.isSoundOnOff)) R.drawable.ic_audio_on else R.drawable.ic_audio_off)
        ivMusicOnOff.setImageResource(if (appPrefs.getBoolean(AppPrefs.isMusicOnOff)) R.drawable.ic_music_on else R.drawable.ic_music_off)

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        ivSoundOnOff.setOnClickListener {
            val newState = !(isSoundCheck.value ?: false)
            isSoundCheck.value = newState
            appPrefs.setBoolean(AppPrefs.isSoundOnOff, newState)
            ivSoundOnOff.setImageResource(if (newState) R.drawable.ic_audio_on else R.drawable.ic_audio_off)
        }

        ivMusicOnOff.setOnClickListener {
            val newState = !(isMusicCheck.value ?: false)
            isMusicCheck.value = newState
            appPrefs.setBoolean(AppPrefs.isMusicOnOff, newState)
            ivMusicOnOff.setImageResource(if (newState) R.drawable.ic_music_on else R.drawable.ic_music_off)
        }
        dialog.show()
    }

    /**
     * LOGOUT API
     */
    private fun getLogoutApi(params: HashMap<String, Any>) {
        compositeDisposable.add(repository.logoutUserAPI(params).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveData.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveData.setValue(Resource.success(responseModel)) },
                { throwable -> liveData.setValue(Resource.error(throwable)) }
            )
        )
    }

    /**
     * GET USER PROFILE API
     */
    fun getUserProfile() {
        compositeDisposable.add(repository.getProfileUserAPI()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveGetProfileData.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveGetProfileData.setValue(Resource.success(responseModel)) },
                { throwable -> liveGetProfileData.setValue(Resource.error(throwable)) }))
    }
}
