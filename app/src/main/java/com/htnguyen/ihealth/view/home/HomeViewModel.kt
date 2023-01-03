package com.htnguyen.ihealth.view.home

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.util.PreferencesUtil

class HomeViewModel : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData(PreferencesUtil.userName)
}