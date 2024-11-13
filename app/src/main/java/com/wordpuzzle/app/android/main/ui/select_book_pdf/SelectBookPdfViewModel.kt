package com.wordpuzzle.app.android.main.ui.select_book_pdf

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.wordpuzzle.app.android.constants.AppConstants
import com.wordpuzzle.app.android.databinding.ActivitySelectBookPdfBinding
import com.wordpuzzle.app.android.main.base.BaseViewModel
import com.wordpuzzle.app.android.main.ui.select_book_pdf.adapter.SelectBookPdfAdapter
import com.wordpuzzle.app.android.preferences.AppPrefs
import com.wordpuzzle.app.android.service.main.Resource
import com.wordpuzzle.app.android.service.model.common.*
import com.wordpuzzle.app.android.service.model.response.*
import com.wordpuzzle.app.android.service.repository.UserRepository
import com.wordpuzzle.app.android.utils.extractPageNumbers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectBookPdfViewModel(
    val appPrefs: AppPrefs,
    val repository: UserRepository
) : BaseViewModel() {
    //binding object
    private var binding: ActivitySelectBookPdfBinding? = null
    private var activity: SelectBookPdfActivity? = null

    //Live data
    val bookId: MutableLiveData<Int> = MutableLiveData(0)
    val bookName: MutableLiveData<String> = MutableLiveData("")
    val selectPageNo: MutableLiveData<String> = MutableLiveData("")
    val isShowRvList: MutableLiveData<Boolean> = MutableLiveData(false)
    var listEmpty = MutableLiveData<EmptyList>()
    private var liveListData: MutableLiveData<List<BookListModel>> = MutableLiveData<List<BookListModel>>()

    private val filteredList: ArrayList<BookListModel> = ArrayList<BookListModel>()
    private val liveBookList = MutableLiveData<Resource<ListAllBookResponseModel?>>()
    private val liveGenerateWordList = MutableLiveData<Resource<GenerateWordListResponseModel?>>()

    //Api Object
    private var compositeDisposable = CompositeDisposable()

    //Adapter
    var adapter: SelectBookPdfAdapter? = null

    /**
     * @param binding use view file
     */
    fun setBinding(binding: ActivitySelectBookPdfBinding, activity: SelectBookPdfActivity) {
        this.binding = binding
        this.activity = activity

        liveListData.value = ArrayList()

        getAllBookList()
        userSearch()
    }

    /**
     * LIVE DATA FOR HANDLE API RESPONSE
     */
    fun getliveBookList(): MutableLiveData<Resource<ListAllBookResponseModel?>> {
        return liveBookList
    }

    fun getLiveGenerateWordList(): MutableLiveData<Resource<GenerateWordListResponseModel?>> {
        return liveGenerateWordList
    }

    /**
     * LIVE DATA FOR HANDLE BOOK LIST API RESPONSE
     */
    private fun getAllBookList() {
        compositeDisposable.add(repository.getListAllBookAPI()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> liveBookList.setValue(Resource.loading()) }
            .subscribe({ responseModel -> liveBookList.setValue(Resource.success(responseModel)) },
                { throwable -> liveBookList.setValue(Resource.error(throwable)) }))
    }

    /**
     * BOOK LIST ADAPTER
     */
    fun setAdapterData(bookList: ArrayList<BookListModel>) {
        liveListData.value = bookList
        adapter = SelectBookPdfAdapter(liveListData.value!!, activity!!)
        this.binding!!.rvSelectBookPdf.adapter = adapter
//        adapter!!.setRecyclerViewItemClickListener(object : RecyclerViewItemClickListener {
//            override fun onItemClick(position: Int, flag: Int, view: View) {
//                try {
//                    when (flag) {
//                        AppConstants.ITEM_CLICK -> {
//                           /* selectPosition.value = position
//                            if (flag == AppConstants.ITEM_CLICK) {
//                                binding!!.rvSelectBookPdf.post { adapter!!.notifyDataSetChanged() }
//                            } else if (flag == AppConstants.ITEM_CLICK_OK) {
//                                val intent = Intent(activity, LoadingAnimationActivity::class.java)
//                                activity!!.startActivity(intent)
//                            }*/
//                        }
//                        AppConstants.ITEM_CLICK_OK -> {
////                            val intent = Intent(activity, LoadingAnimationActivity::class.java)
////                            activity!!.startActivity(intent)
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        })
    }

    /**
     * USER SEARCH via etSearch
     */
    private fun userSearch() {
        binding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing before text changed
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing on text changed
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing on after changed
                try {
                    if (binding!!.etSearch.text.toString().isNotEmpty()) {
                        searchByPatientFilter(binding!!.etSearch.text.toString().trim())
                    } else {
                        searchByPatientFilter("")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * SEARCH FILTER
     */
    fun searchByPatientFilter(chartId: String) {
        try {
            filteredList.clear()
            if (chartId.isNotEmpty()) {
                liveListData.value?.let { cityListDataModels ->
                    for (item in cityListDataModels) {
                        if (item.ebookName.toString().lowercase().contains(chartId.toLowerCase()) && item !in filteredList) {
                            filteredList.add(item)
                        }
                    }
                }
                if (filteredList.isNotEmpty()) {
                    listEmpty.value = EmptyList(false, "")
                    adapter!!.filterList(filteredList)
                } else {
                    listEmpty.value = EmptyList(true, "")
                }
            } else {
                listEmpty.value = EmptyList(false, "")
                adapter!!.filterList(liveListData.value!! as ArrayList<BookListModel>)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bookData(id: Int?, ebookName: String?, toString: String) {
        bookId.value = id!!
        bookName.value = ebookName!!
        selectPageNo.value = toString
        val (startPageNo, endPageNo) = extractPageNumbers(toString)
        generateWordListAPI(startPageNo,endPageNo )
    }

    /**
     * TODO: API---> /api/v1/generate/wordlist/
     * API REQUEST FOR GET USER PAYMENT
     */
    private fun generateWordListAPI(startPageNo: String, endPageNo: String) {
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