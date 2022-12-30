package com.htnguyen.ihealth.view.profile

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar

class ProfileViewModel : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData("You")
    val birthDay: MutableLiveData<String> = MutableLiveData(null)
    val birthDayLong: MutableLiveData<Long> = MutableLiveData(Calendar().timeInMillis)
    val gender: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressWeight: MutableLiveData<Float> = MutableLiveData(null)
    val progressHeight: MutableLiveData<Float> = MutableLiveData(null)
}