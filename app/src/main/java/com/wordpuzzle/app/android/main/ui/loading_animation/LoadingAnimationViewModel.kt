package com.wordpuzzle.app.android.main.ui.loading_animation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLoadingAnimationBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.play_game.PlayGameActivity
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.GenerateWordListResponseModel
import com.wordpuzzle.app.android.service.repository.WordRepository
import com.wordpuzzle.app.android.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoadingAnimationViewModel (val appPrefs: AppPrefs, val repository: WordRepository) : BaseViewModel() {
    //binding object
    private var binding: ActivityLoadingAnimationBinding? = null
    private var activity: LoadingAnimationActivity? = null

    //Live data Object Object
    val bookId: MutableLiveData<Int> = MutableLiveData(0)
    val bookName: MutableLiveData<String> = MutableLiveData("")
    val selectPageNo: MutableLiveData<String> = MutableLiveData("")
    private val liveGenerateWordList = MutableLiveData<Resource<GenerateWordListResponseModel?>>()

    private var countDownTimer: CountDownTimer? = null
    private var count = 3 // Starting countdown number
    private var scaleAnimation: Animation? = null

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityLoadingAnimationBinding, activity: LoadingAnimationActivity, intent: Intent) {
        this.binding = binding
        this.activity = activity

        if (intent.hasExtra(AppConstants.bookId)) {
            bookId.value = intent.getIntExtra(AppConstants.bookId, 0)
        }

        if (intent.hasExtra(AppConstants.bookName)) {
            bookName.value = intent.getStringExtra(AppConstants.bookName)
        }

        if (intent.hasExtra(AppConstants.selectPageNo)) {
            selectPageNo.value = intent.getStringExtra(AppConstants.selectPageNo)
        }

        val (startPageNo, endPageNo) = extractPageNumbers(selectPageNo.value!!)
//        generateWordListAPI(startPageNo, endPageNo)
    }

    fun setTimerCountDown(gid: Int) {
        scaleAnimation = AnimationUtils.loadAnimation(activity, R.anim.countdown_animation)
        countDownTimer = object : CountDownTimer(3000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Update TextView with the current countdown number
                when (count) {
                    3 -> binding!!.rootLayout.setBackgroundResource(R.drawable.ic_three_loading_animation_svg)
                    2 -> binding!!.rootLayout.setBackgroundResource(R.drawable.ic_two_loading_animation_svg)
                    1 -> binding!!.rootLayout.setBackgroundResource(R.drawable.ic_one_loading_animation_svg)
                    else -> { binding!!.rootLayout.setBackgroundResource(R.drawable.ic_one_loading_animation_svg)}
                }

                binding?.tvCount?.text = count.toString() + if (count == 1) "\n${activity?.resources?.getString(R.string.letsPlay)}" else ""

                binding!!.tvCount.startAnimation(scaleAnimation)
                count-- // Decrement countdown number
            }

            override fun onFinish() {
                // Update TextView when countdown finishes
//                binding!!.tvCount.text = activity!!.resources.getString(R.string.letsPlay)
//                binding!!.tvCount.startAnimation(scaleAnimation)
                // Here you can add code to start your app or perform any other action
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(activity, PlayGameActivity::class.java)
                    intent.putExtra(AppConstants.gameRoundId, gid)
                    intent.putExtra(AppConstants.bookId, bookId.value)
                    intent.putExtra(AppConstants.bookName, bookName.value)
                    intent.putExtra(AppConstants.selectPageNo, selectPageNo.value)
                    activity!!.startActivity(intent)
                    activity!!.finishAffinity()
                }, 1000)
            }
        }

        // Start the countdown timer
        (countDownTimer as CountDownTimer).start()
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveGenerateWordList(): MutableLiveData<Resource<GenerateWordListResponseModel?>> {
        return liveGenerateWordList
    }

    /**
     * TODO: API---> /api/v1/generate/wordlist/
     * API REQUEST FOR GET USER PAYMENT
     */
    fun generateWordListAPI(startPageNo: String, endPageNo: String) {
        val params = HashMap<String, Any>()
        params[AppConstants.bookId] = bookId.value!!
        params[AppConstants.startPageNo] = startPageNo.toInt()
        params[AppConstants.endPageNo] = endPageNo.toInt()

        compositeDisposable.add(repository.generateWordListAPI(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveGenerateWordList.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveGenerateWordList.setValue(Resource.success(responseModel)) },
                { throwable -> liveGenerateWordList.setValue(Resource.error(throwable)) }
            ))
    }
}