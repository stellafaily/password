package com.example.encryptionmemo.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.encryptionmemo.common.EventMemoUpdate
import com.example.encryptionmemo.common.NotNullMutableLiveData
import com.example.encryptionmemo.model.main.RoomPagingMemoDataSource
import com.example.encryptionmemo.model.database.MemoDao
import com.example.encryptionmemo.model.database.MemoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class MainViewModel(val db: MemoDao): BaseViewModel() {


    companion object {
        val TAG = "MainViewModel"
    }

    val addMemo: MutableLiveData<Boolean> = MutableLiveData()
    val showMemo: MutableLiveData<MemoData> = MutableLiveData()
    val showSetting: MutableLiveData<Boolean> = MutableLiveData()
    val hasEmptyMemo: NotNullMutableLiveData<Int> = NotNullMutableLiveData(View.GONE)
    val hasRefresing: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)

    val config: PagedList.Config
    val pagedListBuilder : LivePagedListBuilder<Long, MemoData>

    init{
        config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(20)
            .setPrefetchDistance(5)
            .setPageSize(20)
            .build()

        pagedListBuilder = LivePagedListBuilder(RoomPagingMemoDataSource.Factory(db), config)
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun load(): LiveData<PagedList<MemoData>> {
        hasRefresing.postValue(false)
        GlobalScope.launch(Dispatchers.IO) {
            if(db.getCount() <= 0) {
                hasEmptyMemo.postValue(View.VISIBLE)
            }else{
                hasEmptyMemo.postValue(View.GONE)
            }
        }
        return pagedListBuilder.setInitialLoadKey(0).build()
    }

    fun onClickSetting(view: View) {
        showSetting.postValue(true)
    }

    fun onClickMemo(data: MemoData) {
        showMemo.postValue(data)
    }

    fun onClickAddMemo(view: View){
        addMemo.postValue(true)
    }

    fun onRefresh(){
        GlobalScope.launch(Dispatchers.IO) {
            delay(1000L)
            EventBus.getDefault().post(EventMemoUpdate(true))
        }
    }
}