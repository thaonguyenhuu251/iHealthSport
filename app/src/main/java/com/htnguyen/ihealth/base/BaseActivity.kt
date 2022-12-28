package com.htnguyen.ihealth.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.htnguyen.ihealth.support.InputMethodManager
import com.htnguyen.ihealth.support.hideKeyboard

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    abstract val layout: Int
    lateinit var binding: T
    abstract val viewModel: R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this@BaseActivity
        binding.setVariable(getBindingVariable(), viewModel)
    }

    fun bindHideKeyboardListener(vararg view: View) {
        val input = InputMethodManager(this) ?: return
        for (v in view) {
            v.setOnClickListener { input.hideKeyboard(it) }
            v.isClickable = true
        }
    }

    abstract fun getBindingVariable(): Int

}