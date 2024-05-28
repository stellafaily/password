package com.example.encryptionmemo.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.example.encryptionmemo.MyApplication
import com.example.encryptionmemo.R
import com.example.encryptionmemo.databinding.ActivityAddmemoBinding
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AddMemoActivity : BaseActivity<ActivityAddmemoBinding>() {
    companion object {
        val TAG = "AddMemoActivity"
    }

    override val layoutResourceId: Int get() = R.layout.activity_addmemo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vmAddMemo = getViewModel()
        viewDataBinding.lifecycleOwner = this

        initObserve(viewDataBinding.vmAddMemo)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}