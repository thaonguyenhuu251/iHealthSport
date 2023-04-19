package com.htnguyen.ihealth.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.htnguyen.ihealth.util.PreferencesUtil

open class BaseViewModel : ViewModel() {
    open val name: MutableLiveData<String> = MutableLiveData(PreferencesUtil.userName)
    open val step: MutableLiveData<Int> = MutableLiveData(0)
    open val calories: MutableLiveData<Int> = MutableLiveData(0)
    open val meter: MutableLiveData<Int> = MutableLiveData(0)

    open val followStep: MutableLiveData<Int> = MutableLiveData(6000)
}