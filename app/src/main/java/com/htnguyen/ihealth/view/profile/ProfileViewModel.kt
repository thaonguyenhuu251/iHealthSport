package com.htnguyen.ihealth.view.profile

import androidx.lifecycle.MutableLiveData
import com.htnguyen.ihealth.base.BaseViewModel
import com.htnguyen.ihealth.support.Calendar
import com.htnguyen.ihealth.util.PreferencesUtil

class ProfileViewModel : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData(PreferencesUtil.userName)
    val birthDay: MutableLiveData<String> = MutableLiveData(null)
    val birthDayLong: MutableLiveData<Long> = MutableLiveData(PreferencesUtil.userBirthDay)
    val gender: MutableLiveData<Boolean> = MutableLiveData(PreferencesUtil.userGender)
    val progressWeight: MutableLiveData<Float> = MutableLiveData(PreferencesUtil.userWeight)
    val progressHeight: MutableLiveData<Float> = MutableLiveData(PreferencesUtil.userHeight)
}