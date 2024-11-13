package com.wordpuzzle.app.android.main.ui.about_us

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.databinding.ActivityAboutUsBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.about_us.di.AboutUsComponent
import com.wordpuzzle.app.android.service.model.common.CommonHeaderData
import com.wordpuzzle.app.android.utils.ViewUtil

class AboutUsActivity : BaseMainActivity<ActivityAboutUsBinding>() {

    override fun getLayoutId() = R.layout.activity_about_us

    @RequiresApi(Build.VERSION_CODES.R)
    override fun create(savedInstanceState: Bundle?) {
        binding.headerData = CommonHeaderData(true, resources.getString(R.string.about), true, true)
        ViewUtil.setFullScreen(this, true)
//        ViewUtil.hideStatusBar(this)
        val handler = AboutUsEventHandler(this, binding)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: AboutUsViewModel by lazy {
        ViewModelProvider(this, factory).get(AboutUsViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<AboutUsComponent>()?.inject(this)
    }
}