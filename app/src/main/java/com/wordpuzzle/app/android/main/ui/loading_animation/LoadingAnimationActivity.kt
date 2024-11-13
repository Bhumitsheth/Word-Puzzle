package com.wordpuzzle.app.android.main.ui.loading_animation

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLoadingAnimationBinding
import com.wordpuzzle.app.android.domain.model.GameRound
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.loading_animation.di.LoadingAnimationComponent
import com.wordpuzzle.app.android.presentation.presenters.MainMenuPresenter
import com.wordpuzzle.app.android.presentation.views.MainMenuView
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.GenerateWordListResponseModel
import com.wordpuzzle.app.android.utils.showErrorSnackBar
import javax.inject.Inject

class LoadingAnimationActivity : BaseMainActivity<ActivityLoadingAnimationBinding>(), MainMenuView {
    @Inject
    lateinit var mPresenter: MainMenuPresenter
    private lateinit var mGameRoundDimVals: IntArray

    override fun getLayoutId() = R.layout.activity_loading_animation

    override fun create(savedInstanceState: Bundle?) {
        val handler = LoadingAnimationEventHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this, intent)
        mPresenter.setView(this)

        val dim = 12
        mPresenter.newGameRound(dim, dim)

        viewModel.getLiveGenerateWordList().observe(this) {
            response: Resource<GenerateWordListResponseModel?> -> consumeGenerateWordListResponse(response)
        }

        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: LoadingAnimationViewModel by lazy {
        ViewModelProvider(this, factory).get(LoadingAnimationViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<LoadingAnimationComponent>()?.inject(this)
    }

    override fun onResume() {
        super.onResume()
//        mPresenter.loadData()
    }

    override fun setNewGameLoading(enable: Boolean) {
        if (enable) {
            hideProgressBlocking()
//            mNewGameProgress.visibility = View.VISIBLE
//            mNewGameContent.visibility = View.INVISIBLE
        } else {
            hideProgressBlocking()
//            mNewGameProgress.visibility = View.INVISIBLE
//            mNewGameContent.visibility = View.VISIBLE
        }
    }

    override fun showGameInfoList(infoList: List<GameRound.Info?>?) {
        // Show game info list
    }

    override fun showNewlyCreatedGameRound(gameRound: GameRound?) {
        showGameRound(gameRound!!.info.id)
    }

    override fun showGameRound(gid: Int) {
        viewModel.setTimerCountDown(gid)
    }

    override fun clearInfoList() {
        //Clear the info list
    }

    override fun deleteInfo(info: GameRound.Info?) {
        //Delete the info
    }

    private fun consumeGenerateWordListResponse(response: Resource<GenerateWordListResponseModel?>) {
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
                    if (response.data.error == false) {
                        storeSingletonData.setWordListKey(response.data.data.wordsList)
                    }
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }
}