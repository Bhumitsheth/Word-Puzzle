package com.wordpuzzle.app.android.main.ui.select_book_pdf

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wordpuzzle.app.android.R
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivitySelectBookPdfBinding
import com.wordpuzzle.app.android.main.base.BaseMainActivity
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.loading_animation.LoadingAnimationActivity
import com.wordpuzzle.app.android.main.ui.select_book_pdf.di.SelectBookPdfComponent
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.*
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.utils.showErrorSnackBar

class SelectBookPdfActivity : BaseMainActivity<ActivitySelectBookPdfBinding>() {
    override fun getLayoutId() = R.layout.activity_select_book_pdf

    override fun create(savedInstanceState: Bundle?) {
        if (viewModel.appPrefs.getBoolean(AppPrefs.IS_SELECT_BOOK_PDF)) {
            binding.headerData = CommonHeaderData(false, resources.getString(R.string.selectBookPdf), true, true)
        } else {
            binding.headerData = CommonHeaderData(true, resources.getString(R.string.selectBookPdf), true, true)
        }
        val handler = SelectBookPdfEventHandler(this)
        binding.eventHandler = handler
        binding.viewModel = viewModel
        viewModel.setBinding(binding, this)
        viewModel.getliveBookList().observe(this) {
            response: Resource<ListAllBookResponseModel?> -> consumeBookListResponse(response)
        }
        viewModel.getLiveGenerateWordList().observe(this) {
                response: Resource<GenerateWordListResponseModel?> -> consumeGenerateWordListResponse(response)
        }
        binding.lifecycleOwner = this
    }

    override fun getBaseVm(): BaseViewModel {
        return viewModel
    }

    val viewModel: SelectBookPdfViewModel by lazy {
        ViewModelProvider(this, factory).get(SelectBookPdfViewModel::class.java)
    }

    override fun initDependencies() {
        getComponent<SelectBookPdfComponent>()?.inject(this)
    }

    private fun consumeBookListResponse(response: Resource<ListAllBookResponseModel?>) {
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
                        viewModel.isShowRvList.value = true
                        viewModel.setAdapterData(response.data.data.bookList!!)
                        viewModel.listEmpty.value = EmptyList(false, "")
                    } else {
                        viewModel.isShowRvList.value = false
                        showErrorSnackBar(binding.rootLayout, response.data.message!!)
                        viewModel.listEmpty.value = EmptyList(true, resources.getString(R.string.noBookFound))
                    }
                } else {
                    viewModel.isShowRvList.value = false
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
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
                        val intent = Intent(this, LoadingAnimationActivity::class.java)
                        intent.putExtra(AppConstants.bookId, viewModel.bookId.value)
                        intent.putExtra(AppConstants.bookName, viewModel.bookName.value)
                        intent.putExtra(AppConstants.selectPageNo, viewModel.selectPageNo.value)
                        startActivity(intent)
                    }
                } else {
                    showErrorSnackBar(binding.rootLayout, response.data.message!!)
                }
            }
        }
    }
}