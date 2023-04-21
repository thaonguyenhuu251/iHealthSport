package com.htnguyen.ihealth.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.htnguyen.ihealth.util.PreferencesUtil

open class BaseViewModel : ViewModel() {
    open val name: MutableLiveData<String> = MutableLiveData(PreferencesUtil.userName)

}