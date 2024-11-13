package com.wordpuzzle.app.android.main.ui.leader_board

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLeaderBoardBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.leader_board.di.LeaderBoardComponent
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.CommonHeaderData
import com.wordpuzzle.app.android.service.model.common.EmptyList
import com.wordpuzzle.app.android.service.model.response.LeaderBoardResponseModel
import com.wordpuzzle.app.android.utils.showErrorSnackBar

class LeaderBoardActivity : BaseMainActivity<ActivityLeaderBoardBinding>() {
    override fun getLayoutId() = R.layout.activity_leader_board

    override fun create(savedInstanceState: Bundle?) {
        binding.headerData = CommonHeaderData(true, resources.getString(R.string.leaderBoard), true, true)
        val handler = LeaderBoardEventHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        viewModel.getLiveMonthlyWeeklyList().observe(this) {
            response: Resource<LeaderBoardResponseModel?> -> consumeBookListResponse(response)
        }
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: LeaderBoardViewModel by lazy {
        ViewModelProvider(this, factory).get(LeaderBoardViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<LeaderBoardComponent>()?.inject(this)
    }

    private fun consumeBookListResponse(response: Resource<LeaderBoardResponseModel?>) {
        when (response.status) {
            Resource.Status.LOADING -> showProgressBlocking()

            Resource.Status.ERROR -> {
                hideProgressBlocking()
                if (response.error?.message != null) {
                    showErrorSnackBar(binding.rootLayout, response.error.message!!)
                }
            }

            Resource.Status.SUCCESS -> {
                hideProgressBlocking()
                if (response.data!!.error == false && response.data.data != null) {
                    hideProgressBlocking()
                    if (response.data.error == false) {
                        if (response.data.data.weeklyList.isNullOrEmpty()) {
                            viewModel.listEmpty.value = EmptyList(true, resources.getString(R.string.noRecordFound))
                        } else {
                            viewModel.listEmpty.value = EmptyList(false, "")
                            viewModel.setWeeklyAdapterData(response.data.data.weeklyList!!)
                        }

                        if (response.data.data.monthlyList.isNullOrEmpty()) {
                            viewModel.listEmpty.value = EmptyList(true, resources.getString(R.string.noRecordFound))
                        } else {
                            viewModel.listEmpty.value = EmptyList(false, "")
                            viewModel.setMonthlyAdapterData(response.data.data.monthlyList!!)
                        }
                        viewModel.weeklyClick()
                    } else {
                        showErrorSnackBar(binding.rootLayout, response.data.message!!)
                        viewModel.weeklyClick()
                    }
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                    viewModel.weeklyClick()
                }
            }
        }
    }
}