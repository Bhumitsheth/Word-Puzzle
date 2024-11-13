package com.wordpuzzle.app.android.main.ui.level_complete

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityLevelCompleteBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.level_complete.di.LevelCompleteComponent
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.response.*

class LevelCompleteActivity : BaseMainActivity<ActivityLevelCompleteBinding>() {

    override fun getLayoutId() = R.layout.activity_level_complete

    override fun create(savedInstanceState: Bundle?) {
        val handler = LevelCompleteHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this, intent)
        viewModel.getLiveGameCompletion().observe(this) {
                response: Resource<GameCompletionResponseModel?> -> consumeGameCompletionResponse(response)
        }
//        binding.gifImageView.setImageResource(R.raw.level_complete_animation)
//        Glide.with(this)
//            .asGif()
//            .load(R.raw.level_complete_animation)
//            .into(binding.imageView)
//
//        Glide.with(this)
//            .asGif()
//            .load(R.raw.level_complete_animation)
//            .into(binding.imageView1)
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: LevelCompleteViewModel by lazy {
        ViewModelProvider(this, factory).get(LevelCompleteViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<LevelCompleteComponent>()?.inject(this)
    }

    private fun consumeGameCompletionResponse(response: Resource<GameCompletionResponseModel?>) {
        when (response.status) {
            Resource.Status.LOADING -> hideProgressBlocking()
            Resource.Status.ERROR -> hideProgressBlocking()
            Resource.Status.SUCCESS -> hideProgressBlocking()
        }
    }
}