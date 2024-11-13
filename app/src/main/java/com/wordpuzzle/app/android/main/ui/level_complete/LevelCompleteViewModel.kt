package com.wordpuzzle.app.android.main.ui.level_complete

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivityLevelCompleteBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.service.repository.WordRepository
import com.wordpuzzle.app.android.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class LevelCompleteViewModel (
    val appPrefs: AppPrefs,
    val repository: WordRepository
) : BaseViewModel() {
    //binding object
    private var binding: ActivityLevelCompleteBinding? = null
    private var activity: LevelCompleteActivity? = null

    //Live data Object Object
    val pageNo: MutableLiveData<String> = MutableLiveData("")
    val score: MutableLiveData<Int> = MutableLiveData(0)
    val playTime: MutableLiveData<String> = MutableLiveData("")
    val bookId: MutableLiveData<Int> = MutableLiveData(0)
    val bookName: MutableLiveData<String> = MutableLiveData("")
    val selectPageNo: MutableLiveData<String> = MutableLiveData("")
    val durationMillisecond: MutableLiveData<String> = MutableLiveData("")
    private val liveGameCompletion = MutableLiveData<Resource<GameCompletionResponseModel?>>()

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityLevelCompleteBinding, activity: LevelCompleteActivity, intent: Intent) {
        this.binding = binding
        this.activity = activity

        if (intent.hasExtra(AppConstants.pageNo)) {
            pageNo.value = intent.getStringExtra(AppConstants.pageNo)
        }

        if (intent.hasExtra(AppConstants.score)) {
            score.value = intent.getIntExtra(AppConstants.score, 0)
        }

        if (intent.hasExtra(AppConstants.playTime)) {
            playTime.value = intent.getStringExtra(AppConstants.playTime)
        }

        if (intent.hasExtra(AppConstants.bookId)) {
            bookId.value = intent.getIntExtra(AppConstants.bookId, 0)
        }

        if (intent.hasExtra(AppConstants.bookName)) {
            bookName.value = intent.getStringExtra(AppConstants.bookName)
        }

        if (intent.hasExtra(AppConstants.selectPageNo)) {
            selectPageNo.value = intent.getStringExtra(AppConstants.selectPageNo)
        }

        if (intent.hasExtra(AppConstants.durationMillisecond)) {
            durationMillisecond.value = intent.getStringExtra(AppConstants.durationMillisecond)
        }

//        gameCompletionAPI()
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveGameCompletion(): MutableLiveData<Resource<GameCompletionResponseModel?>> {
        return liveGameCompletion
    }

    /**
     * TODO: API---> /api/v1/game_complition/
     * API REQUEST FOR POST GAME COMPLETION
     */
    private fun gameCompletionAPI() {
        val params = HashMap<String, Any>()
        params[AppConstants.bookid] = bookId.value!!
        params[AppConstants.pageRange] = pageNo.value!!
        params[AppConstants.completionTime] = durationMillisecond.value!!
        params[AppConstants.score] = score.value!!
        params[AppConstants.puzzleId] = "p1"
        params[AppConstants.isRepetitive] = "false"
        params[AppConstants.completionDate] = getCurrentDate(AppConstants.DATE_YYYY_MM_DD_FORMAT)

        compositeDisposable.add(repository.gameCompletionAPI(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveGameCompletion.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveGameCompletion.setValue(Resource.success(responseModel)) },
                { throwable -> liveGameCompletion.setValue(Resource.error(throwable)) }
            ))
    }
}