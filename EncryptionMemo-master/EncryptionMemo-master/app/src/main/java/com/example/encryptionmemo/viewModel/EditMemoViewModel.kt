package com.example.encryptionmemo.viewModel

import android.view.View
import android.widget.Toast
import com.example.encryptionmemo.R
import com.example.encryptionmemo.common.EventMemoUpdate
import com.example.encryptionmemo.common.Locker
import com.example.encryptionmemo.common.NotNullMutableLiveData
import com.example.encryptionmemo.common.Utils
import com.example.encryptionmemo.model.InstancePassword
import com.example.encryptionmemo.model.database.MemoDao
import com.example.encryptionmemo.model.database.MemoData
import com.example.encryptionmemo.view.dialog.DialogCheckPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class EditMemoViewModel(val index: Int, val password: String, val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "EditMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val titleString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val dataString: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val data2String: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    lateinit var memo: MemoData

    init{
        GlobalScope.launch(Dispatchers.IO) {
            memo = db.getMemoData(index)
            titleString.postValue(memo.title)
            hasEnc.postValue(memo.hasEnc)
            if(memo.hasEnc) {
                memo.encData?.let {
                    dataString.postValue(Utils.decData(password, it))
                }
                memo.encData2?.let {
                    data2String.postValue(Utils.decData2(password, it))
                }
            } else {
                dataString.postValue(memo.openData)
            }
        }
    }

    fun onClickEnc(view: View) {
        if (hasEnc.value) {
            hasEnc.postValue(false)
        } else {
            hasEnc.postValue(true)
        }
    }

    fun onClickRemove(view: View) {
        if(Locker.hasInfinityLocked()){
            Toast.makeText(view.context, R.string.infinity_locked, Toast.LENGTH_SHORT).show()
            return
        }
        DialogCheckPassword.Builder(view.context)
            .setMessage(R.string.enter_remove_password)
            .setOnClickYes(R.string.ok) { dlg, password ->
                removeMemo()
                dlg.dismiss()
            }.setOnClickNo(R.string.no) { dlg ->
                dlg.dismiss()
            }.build()
            .show()
    }

    fun onClickUpdate(view: View) {
        var titleString = titleString.value
        if(titleString.isNullOrEmpty()) {
            titleString = view.context.getString(R.string.empty_title)
        }

        val dataString = dataString.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }
        val data2String = data2String.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }

        if (!hasEnc.value) {
            updateMemo(false, titleString, dataString, data2String, "")
        } else if (!InstancePassword.getPassword().isNullOrEmpty()) {
            updateMemo(true, titleString, dataString, data2String, InstancePassword.getPassword())
        }else {
            if(Locker.hasInfinityLocked()){
                Toast.makeText(view.context, R.string.infinity_locked, Toast.LENGTH_SHORT).show()
                return
            }
            DialogCheckPassword.Builder(view.context)
                .setMessage(R.string.enter_the_password)
                .setOnClickYes(R.string.ok) { dlg, password ->
                    updateMemo(true, titleString, dataString, data2String, password)
                    dlg.dismiss()
                }.setOnClickNo(R.string.no) { dlg ->
                    dlg.dismiss()
                }.build()
                .show()
        }
    }

    private fun removeMemo() {
        GlobalScope.launch(Dispatchers.IO) {
            db.deleteMemo(index)
            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.remove_memo)
            back.postValue(true)
        }
    }

    private fun updateMemo(hasEncrypt: Boolean, title: String, data: String, data2: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            memo.hasEnc = hasEncrypt
            memo.title = title
            memo.editedAt = System.currentTimeMillis()
            if (hasEncrypt) {
                memo.openData = ""
                memo.encData = Utils.encData(password, data)
                memo.encData2 = Utils.encData(password, data2)

            } else {
                memo.openData = data
                memo.openData = data2
                memo.encData = null
                memo.encData2 = null
            }
            db.updateMemoData(memo)

            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.update_memo)
            back.postValue(true)
        }
    }
}