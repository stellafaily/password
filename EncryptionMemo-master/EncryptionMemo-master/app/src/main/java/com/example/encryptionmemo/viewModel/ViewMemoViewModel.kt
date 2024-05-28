package com.example.encryptionmemo.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.encryptionmemo.common.NotNullMutableLiveData
import com.example.encryptionmemo.common.Utils
import com.example.encryptionmemo.model.database.MemoDao
import com.example.encryptionmemo.model.database.MemoData
import com.example.encryptionmemo.model.database.MemoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class ViewMemoViewModel(val index: Int, val password: String, val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "ViewMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val hintString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val dataString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val data2String: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val editMemo: MutableLiveData<Boolean> = MutableLiveData()

    init {
        load()
    }

    fun load() {
        GlobalScope.launch(Dispatchers.IO) {
            val memo = db.getMemoData(index)
            if (memo == null) {
                back.postValue(true)
            }
            else {
                hasEnc.postValue(memo.hasEnc)
                hintString.postValue(memo.title)
                if (memo.hasEnc) {
                    memo.encData?.let {
                        dataString.postValue(Utils.decData(password, it))
                    }
                    memo.encData2?.let {
                        data2String.postValue(Utils.decData2(password, it))
                    }
                }
                else {
                     dataString.postValue(memo.openData)
                }
            }
        }
    }

    fun onClickEdit(view: View) {
        editMemo.postValue(true)
    }

}