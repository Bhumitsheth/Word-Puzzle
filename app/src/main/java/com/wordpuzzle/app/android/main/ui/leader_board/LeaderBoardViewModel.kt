package com.wordpuzzle.app.android.main.ui.leader_board

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLeaderBoardBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.leader_board.adapter.MonthlyListAdapter
import com.wordpuzzle.app.android.main.ui.leader_board.adapter.WeeklyListAdapter
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.*
import com.wordpuzzle.app.android.service.model.response.LeaderBoardResponseModel
import com.wordpuzzle.app.android.service.model.response.MonthlyListModel
import com.wordpuzzle.app.android.service.model.response.WeeklyListModel
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LeaderBoardViewModel(val appPrefs: AppPrefs, val repository: UserRepository) : BaseViewModel() {
    //binding object
    private var binding: ActivityLeaderBoardBinding? = null
    private var activity: LeaderBoardActivity? = null

    //Live data
    var listEmpty = MutableLiveData<EmptyList>()
    private val liveWeeklyMonthlyList = MutableLiveData<Resource<LeaderBoardResponseModel?>>()
    var liveWeeklyList: MutableLiveData<ArrayList<WeeklyListModel>> = MutableLiveData<ArrayList<WeeklyListModel>>()
    var liveMonthlyList: MutableLiveData<ArrayList<MonthlyListModel>> = MutableLiveData<ArrayList<MonthlyListModel>>()

    var isWeeklyMonthlyBg = MutableLiveData<Boolean>(true)
    var playerName1 = MutableLiveData<String>()
    var playerName2 = MutableLiveData<String>()
    var playerName3 = MutableLiveData<String>()

    var playerScore1 = MutableLiveData<String>()
    var playerScore2 = MutableLiveData<String>()
    var playerScore3 = MutableLiveData<String>()

    var playerImage1 = MutableLiveData<String>()
    var playerImage2 = MutableLiveData<String>()
    var playerImage3 = MutableLiveData<String>()

    private val text2 = MutableLiveData<String>("2nd")
    val text2Data: LiveData<String> get() = text2
    private val text3 = MutableLiveData<String>("3rd")
    val text3Data: LiveData<String> get() = text3

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    //Adapter
    var weeklyListAdapter: WeeklyListAdapter? = null
    var monthlyListAdapter: MonthlyListAdapter? = null

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivityLeaderBoardBinding, activity: LeaderBoardActivity) {
        this.binding = binding
        this.activity = activity

        liveWeeklyList.value = ArrayList()
        liveMonthlyList.value = ArrayList()

        getMonthlyWeeklyList()
    }

    /**
     * WEEKLY LIST ADAPTER
     */
    fun setWeeklyAdapterData(weeklyList: ArrayList<WeeklyListModel>) {
        liveWeeklyList.value = weeklyList
        weeklyListAdapter = WeeklyListAdapter(liveWeeklyList.value!!)
        this.binding!!.rvWeekly.adapter = weeklyListAdapter
    }

    /**
     * MONTHLY LIST ADAPTER
     */
    fun setMonthlyAdapterData(monthlyList: ArrayList<MonthlyListModel>) {
        liveMonthlyList.value = monthlyList
        monthlyListAdapter = MonthlyListAdapter(liveMonthlyList.value!!)
        this.binding!!.rvMonthly.adapter = monthlyListAdapter
    }

    fun weeklyClick() {
        if (liveWeeklyList.value.isNullOrEmpty()){
            binding!!.constraintScoreBoardLayout.visibility=View.GONE
            binding!!.ivWordPuzzleSvg.visibility=View.GONE
            binding!!.tvScore.visibility=View.GONE
            listEmpty.value = EmptyList(true, activity!!.resources.getString(R.string.noRecordFound))
        }else{
            binding!!.constraintScoreBoardLayout.visibility=View.VISIBLE
            binding!!.ivWordPuzzleSvg.visibility=View.VISIBLE
            binding!!.tvScore.visibility=View.VISIBLE
            listEmpty.value = EmptyList(false, "")
        }

        isWeeklyMonthlyBg.value = true

        activity?.let {
            liveWeeklyList.observe(it, Observer { scoreList ->
                if (!scoreList.isNullOrEmpty()) {
                    playerName1.value = scoreList[0].name!!.capitalizeFirstChar()
                    playerName2.value = scoreList[1].name!!.capitalizeFirstChar()
                    playerName3.value = scoreList[2].name!!.capitalizeFirstChar()

                    playerImage1.value = scoreList[0].image
                    playerImage2.value = scoreList[1].image
                    playerImage3.value = scoreList[2].image

                    playerScore1.value = scoreList[0].score.toString()
                    playerScore2.value = scoreList[1].score.toString()
                    playerScore3.value = scoreList[2].score.toString()
                }
            })
        }

        binding!!.rvWeekly.visibility = View.VISIBLE
        binding!!.rvMonthly.visibility = View.GONE
    }

    fun monthlyClick() {
        if (liveMonthlyList.value.isNullOrEmpty()){
            binding!!.constraintScoreBoardLayout.visibility=View.GONE
            binding!!.ivWordPuzzleSvg.visibility=View.GONE
            binding!!.tvScore.visibility=View.GONE
            listEmpty.value = EmptyList(true, activity!!.resources.getString(R.string.noRecordFound))
        }else{
            binding!!.constraintScoreBoardLayout.visibility=View.VISIBLE
            binding!!.ivWordPuzzleSvg.visibility=View.VISIBLE
            binding!!.tvScore.visibility=View.VISIBLE
            listEmpty.value = EmptyList(false, "")
        }

        isWeeklyMonthlyBg.value = false

        activity?.let {
            liveMonthlyList.observe(it, Observer { scoreList ->
                if (!scoreList.isNullOrEmpty()) {
                    playerName1.value = scoreList[0].name!!.capitalizeFirstChar()
                    playerName2.value = scoreList[1].name!!.capitalizeFirstChar()
                    playerName3.value = scoreList[2].name!!.capitalizeFirstChar()

                    playerImage1.value = scoreList[0].image
                    playerImage2.value = scoreList[1].image
                    playerImage3.value = scoreList[2].image

                    playerScore1.value = scoreList[0].score.toString()
                    playerScore2.value = scoreList[1].score.toString()
                    playerScore3.value = scoreList[2].score.toString()
                }
            })
        }

        binding!!.rvWeekly.visibility = View.GONE
        binding!!.rvMonthly.visibility = View.VISIBLE
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getLiveMonthlyWeeklyList(): MutableLiveData<Resource<LeaderBoardResponseModel?>> {
        return liveWeeklyMonthlyList
    }

    /**
     * TODO: API---> /api/v1/leaderboard/
     * API REQUEST FOR POST LEADER BOARD
     */
    private fun getMonthlyWeeklyList() {
        compositeDisposable.add(repository.getLeaderBoardAPI()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveWeeklyMonthlyList.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveWeeklyMonthlyList.setValue(Resource.success(responseModel)) },
                { throwable -> liveWeeklyMonthlyList.setValue(Resource.error(throwable)) }
            )
        )
    }
}