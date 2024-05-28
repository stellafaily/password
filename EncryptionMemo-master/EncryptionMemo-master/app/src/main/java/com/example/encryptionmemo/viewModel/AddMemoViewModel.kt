package com.example.encryptionmemo.viewModel

import android.view.View
import android.widget.Toast
import com.example.encryptionmemo.MyApplication
import com.example.encryptionmemo.R
import com.example.encryptionmemo.common.EventMemoUpdate
import com.example.encryptionmemo.common.Locker
import com.example.encryptionmemo.common.NotNullMutableLiveData
import com.example.encryptionmemo.common.Utils
import com.example.encryptionmemo.model.InstancePassword
import com.example.encryptionmemo.model.database.MemoDao
import com.example.encryptionmemo.model.database.MemoData
import com.example.encryptionmemo.view.dialog.DialogCheckPassword
import com.example.encryptionmemo.view.dialog.DialogInputPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class AddMemoViewModel(val db: MemoDao): BaseViewModel() {

    companion object {
        val TAG = "AddMemoViewModel"
    }

    val hasEnc: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(true)
    val title: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val data: NotNullMutableLiveData<String> = NotNullMutableLiveData("")
    val data2: NotNullMutableLiveData<String> = NotNullMutableLiveData("")

    fun onClickEnc(view: View) {
        if (hasEnc.value) {
            hasEnc.postValue(false)
        } else {
            hasEnc.postValue(true)
        }
    }

    fun onClickSave(view: View) {
        var titleString = title.value
        if(titleString.isNullOrEmpty()) {
            titleString = view.context.getString(R.string.empty_title)
        }

        val dataString = data.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }
        val data2String = data2.value
        if(dataString.isNullOrEmpty()){
            toastMessage.postValue(R.string.empty_data_plz_enter_a_data)
            return
        }

        if (!hasEnc.value) {
            saveMemo(false, titleString, dataString, data2String, "")
        } else if (!InstancePassword.getPassword().isNullOrEmpty()) {
            saveMemo(true, titleString, dataString, data2String, InstancePassword.getPassword())
        }else {
            if(Locker.hasInfinityLocked()){
                Toast.makeText(view.context, R.string.infinity_locked, Toast.LENGTH_SHORT).show()
                return
            }
            DialogCheckPassword.Builder(view.context)
                .setMessage(R.string.enter_the_password)
                .setOnClickYes(R.string.ok) { dlg, password ->
                    saveMemo(true, titleString, dataString, data2String, password)
                    dlg.dismiss()
                }.setOnClickNo(R.string.no) { dlg ->
                    dlg.dismiss()
                }.build()
                .show()
        }
    }

    private fun saveMemo(hasEncrypt: Boolean, title: String, data: String, data2: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            if (hasEncrypt) {
                val memo = MemoData(hasEnc.value,
                    title,
                    System.currentTimeMillis(),
                    Utils.getRandomResourceId(),
                    "",
                    Utils.encData(password, data),
                    Utils.encData2(password, data2)
                )
                db.insertMemoData(memo)
            } else {
                val memo = MemoData(
                    hasEnc.value,
                    title,
                    System.currentTimeMillis(),
                    Utils.getRandomResourceId(),
                    "",
                    null,
                    null
                )
                db.insertMemoData(memo)
            }

            EventBus.getDefault().post(EventMemoUpdate(true))
            toastMessage.postValue(R.string.save_memo)
            back.postValue(true)
        }
    }
}