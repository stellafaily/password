package com.example.encryptionmemo.view

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.encryptionmemo.viewModel.BaseViewModel

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    abstract val layoutResourceId: Int
//    var hasFullScreen : Boolean = true
//    var hasSecureViolationOnlyActivityFinish : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
//        setFullScreen()
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
    }


    fun initObserve(viewModel : BaseViewModel?){

        viewModel?.back?.observe(this, Observer {
            this.finish()
        })

        viewModel?.toastMessage?.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel?.toastMessage2?.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

}